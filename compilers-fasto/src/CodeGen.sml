(* Code generator for Fasto *)

structure CodeGen = struct

(*
    exception Error of string * Fasto.pos

    val compile : Fasto.KnownTypes.Prog -> Mips.Instruction list

    (* for debugging *)
    val compileExp : Fasto.KnownTypes.Exp
                     -> string SymTab.SymTab
                     -> string
                     -> Mips.Instruction list
*)

open Fasto.KnownTypes
open Fasto

exception Error of string * pos

(* Name generator for labels and temporary symbolic registers *)
(* Example usage: val tmp = newName "tmp"  (* might produce _tmp_5_ *) *)
val counter = ref 0

fun newName reg_name =
    let val () = counter := !counter + 1
        val n = Int.toString (!counter)
    in "_" ^ reg_name ^ "_" ^ n ^ "_" end

(* Number to text with "normal" sign symbol *)
fun makeConst n = if n>=0 then Int.toString n
                  else "-" ^ Int.toString (~n)

(* Table storing all string literals, with labels given to them *)
val stringTable = ref []
(* could also contain "\n", ",", "Index out of bounds in line ", but the
   format is a bit different (length and dummy pointer come first) *)

(* Building a string in the heap, including initialisation code *)
fun buildString (label, str)
  = let val data = [ Mips.ALIGN "2"   (* means: word-align *)
                   , Mips.LABEL label (* pointer *)
                   , Mips.SPACE "4"   (* size(int) *)
                   , Mips.ASCIIZ str]
        val initR = label ^ "_init"
        val addrR = label ^ "_addr"
        val initcode = [ Mips.LA(addrR, label)
                       , Mips.LI(initR, makeConst (String.size str))
                       , Mips.SW(initR, addrR, makeConst 0) ]
    in (initcode, data)
    end

(* Link register *)
val RA = "31"
(* Register for stack pointer *)
val SP = "29"
(* Register for heap pointer *)
val HP = "28"

(* Suggested register division *)
val minReg = 2       (* lowest usable register *)
val maxCaller = 15   (* highest caller-saves register *)
val maxReg = 25      (* highest allocatable register *)

(* Determine the size of an element in an array based on its type *)
datatype ElemSize = One | Four
fun getElemSize Char  = One
  | getElemSize Bool  = One
  | getElemSize other = Four

fun elemSizeToInt One = 1
  | elemSizeToInt Four = 4

(* Pick the correct store instruction from the element size. *)
fun mipsStore elem_size = case elem_size of
                              One => Mips.SB
                            | Four => Mips.SW

(* generates the code to check that the array index is within bounds *)
 fun checkBounds(arr_beg, ind_reg, (line,c)) =
      let val size_reg = newName "size_reg"
          val tmp_reg  = newName "tmp_reg"
          val err_lab  = newName "error_lab"
          val safe_lab = newName "safe_lab"
      in [ Mips.LW(size_reg, arr_beg, "0")
         , Mips.BGEZ(ind_reg, safe_lab)
         , Mips.LABEL(err_lab)
         , Mips.LI("5", makeConst line)
         , Mips.J "_IllegalArrSizeError_"
         , Mips.LABEL(safe_lab)
         , Mips.SUB(tmp_reg, ind_reg, size_reg)
         , Mips.BGEZ(tmp_reg, err_lab)
         ]
      end

(* dynalloc(size_reg, place, ty) generates code for allocating arrays of heap
   memory by incrementing the HP register (heap pointer) by a number of words.
   The arguments for this function are as follows:

     size_reg: contains the logical array size (number of array elements)
     place: will contain the address of new allocation (old HP)
     ty: char/bool elements take 1 byte, int elements take 4 bytes
 *)
fun dynalloc (size_reg : string,
              place    : string,
              ty       : Type) : Mips.Instruction list =
    let val tmp_reg = newName "tmp"

        (* Use old HP as allocation address. *)
        val code1 = [ Mips.MOVE (place, HP) ]

        (* For char/bool: Align address to 4-byte boundary by rounding up. *)
        (*                (By adding 3 and rounding down using SRA/SLL.) *)
        (* For int and arrays: Multiply logical size by 4, no alignment. *)
        val code2 =
            if ty = Char orelse ty = Bool
            then [ Mips.ADDI(tmp_reg, size_reg, "3")
                 , Mips.SRA (tmp_reg, tmp_reg, "2")
                 , Mips.SLL (tmp_reg, tmp_reg, "2") ]
            else [ Mips.SLL (tmp_reg, size_reg, "2") ]

        (* Make space for array size (+4). Increase HP. *)
        (* Save size of allocation in header. *)
        val code3 =
            [ Mips.ADDI (tmp_reg, tmp_reg, "4")
            , Mips.ADD (HP, HP, tmp_reg)
            , Mips.SW (size_reg, place, "0") ]

    in code1 @ code2 @ code3
    end

(* Pushing arguments on the stack: *)
(* For each register 'r' in 'rs', copy them to registers from
'firstReg' and counting up. Return the full code and the next unused
register (firstReg + num_args).  *)
fun applyRegs( fid: string,
               args: string list,
               place: string, pos) : Mips.Prog =
    let val regs_num = length args
        val caller_regs =
            List.tabulate (regs_num, fn n => makeConst (n + minReg))
        (* zipWith Mips.MOVE =
           zipWith (fn (regDest, regSrc) => Mips.MOVE (regDest, regSrc)) *)
        val move_code = ListPair.map Mips.MOVE (caller_regs, args)
    in
      if regs_num > maxCaller - minReg
      then raise Error("Number of arguments passed to "^fid^
                       " exceeds number of caller registers", pos)
      else move_code @ [ Mips.JAL(fid,caller_regs), Mips.MOVE(place, "2") ]
  end

(* Compile 'e' under bindings 'vtable', putting the result in its 'place'. *)
fun compileExp e vtable place =
  case e of
      Constant (IntVal n, pos) =>
      if n < 0 then
          compileExp (Negate (Constant (IntVal (~n), pos), pos)) vtable place
      else if n < 32768 then
          [ Mips.LI (place, makeConst n) ]
      else
        [ Mips.LUI (place, makeConst (n div 65536))
        , Mips.ORI (place, place, makeConst (n mod 65536)) ]
  | Constant (CharVal c, pos) => [ Mips.LI (place, makeConst (ord c)) ]

  | Constant (BoolVal b, pos) => raise Fail "Unimplemented feature boolean constants"
  
  (* Create/return a label here, collect all string literals of the program
     (in stringTable), and create them in the data section before the heap
     (Mips.ASCIIZ) *)
  | StringLit (strLit, pos) =>
    let (* Convert string literal into label; only use valid characters. *)
        fun translateC c = if Char.isAlphaNum c then str c else ""
        val normalChars = String.translate translateC strLit ^ "__str__"
        val label = newName (String.substring (normalChars, 0, 7))
        val ()    = stringTable := (label, strLit)::(!stringTable)
    in [ Mips.LA (place, label),
         Mips.COMMENT (label^": string \""^ String.toCString strLit ^ "\"") ]
    end

  | Constant (ArrayVal (vs, tp), pos) =>
    (* Create corresponding ArrayLit expression to re-use code. *)
    let val arraylit =
            ArrayLit (map (fn v => Constant (v, pos)) vs, tp, pos)
    in compileExp arraylit vtable place end

  | ArrayLit (elems, tp, pos) =>
    let val elem_size = getElemSize tp
        val size_reg  = newName "size_reg"
        val addr_reg  = newName "addr_reg"
        val tmp_reg   = newName "tmp_reg"

        (* Store size of literal in size_reg, dynamically allocate that. *)
        (* Let addr_reg contain the address for the first array element. *)
        val header  = [ Mips.LI (size_reg, makeConst (length elems)) ]  @
                      dynalloc (size_reg, place, tp) @
                      [ Mips.ADDI (addr_reg, place, "4") ]

        fun compileElem elem_exp =
            let val elem_code = compileExp elem_exp vtable tmp_reg
                val storeInst = case elem_size of
                                    One => Mips.SB
                                  | Four => Mips.SW
            in elem_code @
               [ storeInst (tmp_reg, addr_reg, "0")
               , Mips.ADDI (addr_reg, addr_reg,
                            makeConst (elemSizeToInt elem_size)) ]
            end

        val elems_code = List.concat (map compileElem elems)
    in header @ elems_code
    end

  | Var (vname, pos) =>
      (case SymTab.lookup vname vtable of
           NONE          => raise Error ("Name " ^ vname ^ " not found", pos)
         | SOME reg_name => [Mips.MOVE (place, reg_name)]
      )
  | Plus (e1, e2, pos) =>
      let val t1 = newName "plus_L"
          val t2 = newName "plus_R"
          val code1 = compileExp e1 vtable t1
          val code2 = compileExp e2 vtable t2
      in code1 @ code2 @ [Mips.ADD (place,t1,t2)]
      end
  | Minus (e1, e2, pos) =>
      let val t1 = newName "minus_L"
          val t2 = newName "minus_R"
          val code1 = compileExp e1 vtable t1
          val code2 = compileExp e2 vtable t2
      in  code1 @ code2 @ [Mips.SUB (place,t1,t2)]
      end

  | Times (e1, e2, pos) =>
    raise Fail "Unimplemented feature multiplication"
  | Divide (e1, e2, pos) =>
    raise Fail "Unimplemented feature division"
  | Not (e', pos) =>
    raise Fail "Unimplemented feature not"
  | Negate (e', pos) =>
    raise Fail "Unimplemented feature negate"

  | Let (dec, e1, pos) =>
      let val (code1, vtable1) = compileDec dec vtable
          val code2 = compileExp e1 vtable1 place
      in code1 @ code2
      end
  | If (e1, e2, e3, pos) =>
      let val thenLabel = newName "then"
          val elseLabel = newName "else"
          val endLabel = newName "endif"
          val code1 = compileCond e1 vtable thenLabel elseLabel
          val code2 = compileExp e2 vtable place
          val code3 = compileExp e3 vtable place
      in code1 @ [Mips.LABEL thenLabel] @ code2  @
         [ Mips.J endLabel, Mips.LABEL elseLabel ] @
         code3 @ [Mips.LABEL endLabel]
      end
  | Apply (f, args, pos) =>
      let
          (* Convention: args in regs (2..15), result in reg 2 *)
          fun compileArg arg =
              let val arg_reg = newName "arg"
              in (arg_reg,
                  compileExp arg vtable arg_reg)
              end
          val (arg_regs, argcode) = ListPair.unzip (map compileArg args)
          val applyCode = applyRegs(f, arg_regs, place, pos)
      in  List.concat argcode @  (* Evaluate args *)
          applyCode              (* Jump to function and store result in place *)
      end

  (* dirty I/O. Read and Write: supported for basic types: Int, Char,
     Bool via system calls. Write of an Array(Chars) is also
     supported. The others are the user's responsibility.
  *)
  | Read(tp, pos) => (
      case tp of
        Int  => [ Mips.JAL ("getint",["2"]),
                  Mips.MOVE(place,"2")
                ]
      | Char => [ Mips.JAL ("getchar", ["2"]),
                  Mips.MOVE(place, "2")
                ]
      | Bool =>
          let val tl = newName "true_lab"
              val fl = newName "false_lab"
              val ml = newName "merge_lab"
              val v  = newName "bool_var"
          in [ Mips.JAL ("getint", ["2"])
             , Mips.MOVE(v, "2")
             , Mips.BEQ (v,"0",fl)
             , Mips.J tl
             , Mips.LABEL fl
             , Mips.MOVE(place, "0")
             , Mips.J ml
             , Mips.LABEL tl
             , Mips.LI (place, "1")
             , Mips.J ml
             , Mips.LABEL ml ]
          end
      | _ => raise Error("Read on an incompatible type: " ^ ppType tp, pos)
    )
  | Write(e, tp, pos) =>
    let val tmp = newName "tmp"
        val codeexp = compileExp e vtable tmp @ [ Mips.MOVE (place, tmp) ]
      in case tp of
           Int  => codeexp @ [ Mips.MOVE("2",place), Mips.JAL("putint", ["2"]) ]
         | Char => codeexp @ [ Mips.MOVE("2",place), Mips.JAL("putchar",["2"]) ]
         | Bool =>
              let
                val tlab = newName "wBoolF"
              in
                codeexp @
                [ Mips.LA ("2","_true")
                , Mips.BNE (place,"0",tlab)
                , Mips.LA ("2","_false")
                , Mips.LABEL tlab
                , Mips.JAL("putstring", ["2"]) ]
              end
         | Array Char =>
             codeexp @ [ Mips.MOVE ("2", tmp)
                       , Mips.JAL("putstring", ["2"]) ]

         | Array elem_type => (* for Array(Int) and Array(Array(Int)) *)
             let val arr_reg   = newName "arr_reg"
                 val size_reg  = newName "size_reg"
                 val tmp_reg   = newName "tmp_reg"
                 val i_reg     = newName "ind_var"
                 val loop_beg  = newName "loop_beg"
                 val loop_end  = newName "loop_end"

                 val header1 = [ Mips.LW(size_reg, place, "0")
                               , Mips.ADDI(arr_reg, place, "4")
                               , Mips.MOVE(i_reg,"0")
                               , Mips.LABEL(loop_beg)
                               , Mips.SUB(tmp_reg,i_reg,size_reg)
                               , Mips.BGEZ(tmp_reg, loop_end) ]

                 val header2 = case getElemSize elem_type of
                                   One => [ Mips.LB(tmp_reg,arr_reg,"0")
                                          , Mips.ADDI(arr_reg,arr_reg,"1") ]
                                 | Four => [ Mips.LW(tmp_reg,arr_reg,"0")
                                           , Mips.ADDI(arr_reg,arr_reg,"4") ]

                 (* create a fake Fasto node corresponding to the array elem
                 and call compileExp recursively to generate code to print the
                 element *)
                 val elem_reg  = newName "ft_elem_reg"
                 val new_vtab  = SymTab.bind elem_reg tmp_reg vtable
                 val fastoexp : Exp = Write(Var(elem_reg, pos), elem_type, pos)
                 val elem_code = compileExp fastoexp new_vtab tmp_reg

             in codeexp @ header1 @ header2 @ elem_code @
                [ Mips.ADDI(i_reg, i_reg, "1")
                , Mips.J loop_beg
                , Mips.LABEL loop_end ]
             end
      end

  (* Comparison checking, later similar code for And and Or. *)
  | Equal (e1, e2, pos) =>
      let val t1 = newName "eq_L"
          val t2 = newName "eq_R"
          val code1 = compileExp e1 vtable t1
          val code2 = compileExp e2 vtable t2
          val falseLabel = newName "false"
      in  code1 @ code2 @
          [ Mips.LI (place,"0")
          , Mips.BNE (t1,t2,falseLabel)
          , Mips.LI (place,"1")
          , Mips.LABEL falseLabel ]
      end
  | Less (e1, e2, pos) =>
      let val t1 = newName"lt_L"
          val t2 = newName"lt_R"
          val code1 = compileExp e1 vtable t1
          val code2 = compileExp e2 vtable t2
      in  code1 @ code2 @
          [Mips.SLT (place,t1,t2)]
      end

  | And (e1, e2, pos) =>
    raise Fail "Unimplemented feature &&"
  | Or (e1, e2, pos) =>
    raise Fail "Unimplemented feature ||"

  (* Indexing:
     1. generate code to compute the index
     2. check index within bounds
     3. add the start address with the index
     4. get the element at that address
   *)
  | Index (arr_name, i_exp, ty, pos) =>
      let val ind_reg  = newName "arr_ind"
          val ind_code = compileExp i_exp vtable ind_reg
          val arr_reg  = newName "arr_reg"

          (* Let arr_reg be the start of the data segment *)
          val arr_beg =
              case SymTab.lookup arr_name vtable of
                  NONE => raise Error ("Name "^arr_name^" not found", pos)
                | SOME reg_name => reg_name
          val init_code = [ Mips.ADDI(arr_reg, arr_beg, "4") ]

          (* code to check bounds *)
          val check_code = checkBounds(arr_beg, ind_reg, pos)

          (* for INT/ARRAY: ind *= 4 else ind is unchanged *)
          (* array_var += index; place = *array_var *)
          val load_code =
              if ty = Char orelse ty = Bool
              then [ Mips.ADD(arr_reg, arr_reg, ind_reg)
                   , Mips.LB(place, arr_reg, "0") ]
              else [ Mips.SLL(ind_reg, ind_reg, "2")
                   , Mips.ADD(arr_reg, arr_reg, ind_reg)
                   , Mips.LW(place, arr_reg, "0") ]

      in ind_code @ init_code @ check_code @ load_code
      end

  (* Second-Order Array Combinators (SOACs):
     iota, map, reduce
  *)
  | Iota (n_exp, pos as (line, _)) =>
    raise Fail "Unimplemented feature iota"

  | Map (farg, arr_exp, elem_type, ret_type, pos) =>
    raise Fail "Unimplemented feature map"

  (* reduce(f, acc, {x1, x2, ...}) = f(..., f(x2, f(x1, acc))) *)
  | Reduce (binop, acc_exp, arr_exp, tp, pos) =>
    raise Fail "Unimplemented feature reduce"

(* compile condition *)
and compileCond c vtable tlab flab =
  let val t1 = newName "cond"
      val code1 = compileExp c vtable t1
  in  code1 @ [Mips.BNE (t1, "0", tlab), Mips.J flab]
  end

and compileDec (Dec (s,e,pos)) vtable =
      let val t = newName "letBind"
          val code = compileExp e vtable t
          val new_vtable = SymTab.bind s t vtable
      in (code, new_vtable)
      end

(* code for saving and restoring callee-saves registers *)
fun stackSave currentReg maxReg savecode restorecode offset =
  if currentReg > maxReg
  then (savecode, restorecode, offset)  (* done *)
  else stackSave (currentReg+1)
                 maxReg
                 (Mips.SW (makeConst currentReg,
                               SP,
                               makeConst offset)
                  :: savecode) (* save register *)
                 (Mips.LW (makeConst currentReg,
                               SP,
                               makeConst offset)
                  :: restorecode) (* restore register *)
                 (offset-4) (* adjust offset *)

(* add function arguments to symbol table *)
and getArgs     []      vtable   _     = ([], vtable)
  | getArgs (Param (v,_)::vs) vtable nextReg =
           if nextReg > maxCaller
           then raise Error ("Passing too many arguments!", (0,0))
           else
             let val vname = newName ("param_" ^ v)
                 val vtable1 = SymTab.bind v vname vtable (*   (v,vname)::vtable   *)
                 val (code2,vtable2) = getArgs vs vtable1 (nextReg + 1)
             in ([Mips.MOVE (vname, makeConst nextReg)]
                 @ code2, vtable2)
             end

(* compile function declaration *)
and compileFun (FunDec (fname, resty, args, exp, (line,col))) =
      let (* make a vtable from bound formal parameters,
             then evaluate expression in this context, return it *)
        (* arguments passed in registers, "move" into local vars. *)
        val (argcode, vtable_local) = getArgs args (SymTab.empty ()) minReg
        (* return value in register 2 *)
        val rtmp = newName (fname ^ "res")
        val returncode  = [Mips.MOVE ("2",rtmp)] (* move return val to R2 *)
        val body = compileExp exp vtable_local rtmp (* target expr *)
        val (body1, _, maxr, spilled)
                   = RegAlloc.registerAlloc   (* call register allocator *)
                      (argcode @ body @ returncode)
                      ["2"] 2 maxCaller maxReg 0
        val (savecode, restorecode, offset) (* save/restore callee-saves *)
            = stackSave (maxCaller+1) maxr [] [] (~8 + (~4 * spilled))
      in  [Mips.COMMENT ("Function " ^ fname),
           Mips.LABEL fname,       (* function label *)
           Mips.SW (RA, SP, "-4")] (* save return address *)
        @ savecode                 (* save callee-saves registers *)
        @ [Mips.ADDI (SP,SP,makeConst offset)]   (* SP adjustment *)
        @ body1                    (* code for function body *)
        @ [Mips.ADDI (SP,SP,makeConst (~offset))] (* move SP up *)
        @ restorecode              (* restore callee-saves registers *)
        @ [Mips.LW (RA, SP, "-4"), (* restore return addr *)
           Mips.JR (RA, [])]       (* return *)
      end

(* compile program *)
fun compile funs =
  let val () = stringTable := [("_true","true"), ("_false","false")]
      val funsCode = List.concat (List.map compileFun funs)
      val (stringinit_sym, stringdata)
        = ListPair.unzip (List.map buildString (!stringTable))
      val (stringinit,_,_,_)
        = case stringinit_sym of
              [] => ([],[],0,0)
            | other => RegAlloc.registerAlloc (* call register allocator *)
                           (List.concat stringinit_sym)
                           ["2"] 2 maxCaller maxReg 0
  in
      [Mips.TEXT "0x00400000",
       Mips.GLOBL "main"]
      (* initialisation: heap pointer and string pointers *)
    @ (Mips.LA (HP, "_heap_"):: stringinit)
      (* jump to main (and stop after returning) *)
    @ [Mips.JAL ("main",[])]
    @ (* stop code *)
      [Mips.LABEL "_stop_",
       Mips.LI ("2","10"), (* syscall exit = 10 *)
       Mips.SYSCALL]
    @ (* code for functions *)
    funsCode
    (* pre-defined ord: char -> int and chr: int -> char *)
    @ [Mips.LABEL "ord", (* char returned unmodified, interpreted as int *)
       Mips.JR (RA,[]),
       Mips.LABEL "chr", (* int values are truncated to 8 bit (ASCII), *)
       Mips.ANDI ("2", "2", makeConst 255),
       Mips.JR (RA,[])]
      (* built-in read and write functions *)
    @ [Mips.LABEL "putint",     (* putint function *)
       Mips.ADDI(SP,SP,"-8"),
       Mips.SW ("2",SP,"0"),    (* save used registers *)
       Mips.SW ("4",SP,"4"),
       Mips.MOVE ("4","2"),     (* convention: number to be written in r2 *)
       Mips.LI ("2","1"),       (* write_int syscall *)
       Mips.SYSCALL,
       Mips.LI ("2","4"),       (* writestring syscall *)
       Mips.LA("4","_space_"),
       Mips.SYSCALL,            (* write CR *)
       Mips.LW ("2",SP,"0"),    (* reload used registers *)
       Mips.LW ("4",SP,"4"),
       Mips.ADDI(SP,SP,"8"),
       Mips.JR (RA,[]),

       Mips.LABEL "getint",     (* getint function *)
       Mips.LI ("2","5"),       (* read_int syscall *)
       Mips.SYSCALL,
       Mips.JR (RA,[])]
    @ (* putchar *)
      [ Mips.LABEL "putchar",
        Mips.ADDI(SP,SP,"-8"),   (* make space for 2 registers on the stack *)
        Mips.SW ("2",SP,"0"),    (* save registers $2 and $4 to stack *)
        Mips.SW ("4",SP,"4"),
        Mips.MOVE ("4","2"),     (* put char in $4 for syscall to work on *)
        Mips.LI("2", "11"),      (* put syscall 11 (= putchar) in $2 *)
        Mips.SYSCALL,
        Mips.LI ("2","4"),       (* put syscall 4 (= putstring) in $2 *)
        Mips.LA("4","_space_"),  (* the string we'll write is a space *)
        Mips.SYSCALL,
        Mips.LW ("2",SP,"0"),    (* reload registers $2 and $4 from stack *)
        Mips.LW ("4",SP,"4"),
        Mips.ADDI(SP,SP,"8"),    (* free stack space again *)
        Mips.JR (RA,[])
      ]
    @ (* getchar *)
      [ Mips.LABEL "getchar",
        Mips.ADDI(SP,SP,"-8"),   (* make space for 2 registers on the stack *)
        Mips.SW ("4",SP,"0"),    (* save registers $4 and $5 to stack *)
        Mips.SW ("5",SP,"4"),
        Mips.LI("2", "12"),      (* put syscall 12 (= getchar) in $2 *)
        Mips.SYSCALL,
        Mips.MOVE("5","2"),      (* temporarily move the result in reg $5*)
        Mips.LI ("2","4"),       (* writestring syscall *)
        Mips.LA("4","_cr_"),
        Mips.SYSCALL,            (* write CR *)
        Mips.MOVE("2", "5"),     (* put the result back in $2*)
        Mips.LW ("4", SP, "0"),  (* restore registers *)
        Mips.LW ("5", SP, "4"),
        Mips.ADDI(SP,SP,"8"),    (* free stack space again *)
        Mips.JR (RA,[])
      ]
    @ (* putstring *)
      [ Mips.LABEL "putstring",
        Mips.ADDI(SP,  SP, "-16"),   (* make space on stack for registers *)
        Mips.SW  ("2", SP, "0"),     (* save registers $2,$4,$5,$6 to stack *)
        Mips.SW  ("4", SP, "4"),
        Mips.SW  ("5", SP, "8"),
        Mips.SW  ("6", SP, "12"),
        Mips.LW  ("4", "2", "0"),    (* $4 := size($2)    *)
        Mips.ADDI("5", "2", "4"),    (* $5 := $4 + 4      *)
        Mips.ADD ("6", "5", "4"),    (* $6 := $4 + $5     *)
        Mips.LI  ("2", "11"),        (* put syscall 11 (= putchar) in $2 *)
        Mips.LABEL "putstring_begin",
        Mips.SUB ("4", "5", "6"),         (* $4 := $5 - $6     *)
        Mips.BGEZ("4", "putstring_done"), (* while ($4 > 0) {  *)
          Mips.LB("4", "5", "0"),         (*   $4 := M[$5]     *)
          Mips.SYSCALL,                   (*   putchar($4)     *)
          Mips.ADDI("5", "5", "1"),       (*   $5 := $5 + 1    *)
          Mips.J "putstring_begin",       (* }                 *)
        Mips.LABEL "putstring_done",
        Mips.LW ("2", SP, "0"),      (* restore registers $2,$4,$5,$6 *)
        Mips.LW ("4", SP, "4"),
        Mips.LW ("5", SP, "8"),
        Mips.LW ("6", SP, "12"),
        Mips.ADDI(SP, SP, "16"),     (* free stack space again *)
        Mips.JR (RA,[])
      ]
    @ (* fixed error code for indexing errors *)
      [Mips.LABEL "_IllegalArrSizeError_",
       Mips.LA ("4","_IllegalArrSizeString_"),
       Mips.LI ("2","4"), Mips.SYSCALL, (* print string *)
       Mips.MOVE ("4","5"),
       Mips.LI ("2","1"), Mips.SYSCALL, (* print line number *)
       Mips.LA ("4","_cr_"),
       Mips.LI ("2","4"), Mips.SYSCALL, (* print CR *)
       Mips.J "_stop_",
      (* Fixed data (for error message) *)
       Mips.DATA "",
       Mips.LABEL "_cr_",       (* carriage return string *)
       Mips.ASCIIZ "\n",
       Mips.LABEL "_space_",
       Mips.ASCIIZ " ",
       Mips.LABEL "_IllegalArrSizeString_",
       Mips.ASCIIZ "Error: Array size less or equal to 0 at line "]
      (* String literals *)
     @ (Mips.COMMENT "String Literals" ::
        List.concat stringdata)
      (* Heap (to allocate arrays in, word-aligned) *)
     @ [Mips.ALIGN "2",
        Mips.LABEL "_heap_",
        Mips.SPACE "100000"]
  end
end
