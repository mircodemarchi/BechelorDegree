(* An interpreter for Fasto. *)

structure Interpreter = struct

(*

An interpreter executes a (Fasto) program by inspecting the abstract syntax
tree of the program, and doing what needs to be done in another programming
language (SML).

As mentioned in Fasto.sml, some Fasto expressions are implicitly typed. The
interpreter infers the missing types, and checks the types of the operands
before performing any Fasto operation.

Any valid Fasto program must contain a "main" function, which is the entry
point of the program. The return value of this function is the result of the
Fasto program.

The main function of interest in this module is:

  val evalProg : Fasto.UnknownTypes.Prog -> Fasto.Value

*)

open Fasto

(* An exception for reporting run-time errors. *)
exception Error of string * pos

open Fasto.UnknownTypes

(* Build a function table, which associates a function names with function
   declarations. *)
fun buildFtab [] =
    let val p  = (0, 0)
        val ch = #"a"
    in SymTab.fromList [ ("chr", FunDec ("chr", Char, [Param ("n", Int)],
                                         Constant (CharVal ch, p), p))
                       , ("ord", FunDec ("ord", Int, [Param ("c", Char)],
                                         Constant (IntVal 1, p), p)) ]
    end
  | buildFtab ( fdcl::fs ) =
    (* Bind the user-defined functions, in reverse order. *)
    let
      val fid   = getFunName fdcl
      val pos   = getFunPos fdcl
      val ftab  = buildFtab fs
    in
      case SymTab.lookup fid ftab of
        NONE => SymTab.bind fid fdcl ftab
      | SOME ofdecl =>
          (* Report the first occurrence of the name. *)
          raise Error ("Already defined function: " ^ fid, getFunPos ofdecl)
    end

(* Check whether a value matches a type. *)
fun typeMatch ( Int,  IntVal _ ) = true
  | typeMatch ( Bool, BoolVal _) = true
  | typeMatch ( Char, CharVal _) = true
  | typeMatch ( Array t, ArrayVal (vals, tp) ) =
    (t = tp) andalso List.all (fn value => typeMatch (t, value)) vals
  | typeMatch (_, _) = false

fun invalidOperand str tp e pos =
    raise Error(str ^ "; expected " ^ (ppType tp) ^ ", got " ^ (ppType (valueType e)) ^
      " (" ^ (ppVal 0 e) ^ ") instead", pos)

fun invalidOperands str tps e0 e1 pos =
    let
      val types = map (fn (tp0, tp1) => (ppType tp0) ^ " and " ^ (ppType tp1)) tps
    in
      raise Error(
        str ^ "; expected " ^ (String.concatWith ", or " types) ^ ", got " ^
        (ppType (valueType e0)) ^ " (" ^ (ppVal 0 e0) ^ ") and " ^
        (ppType (valueType e1)) ^ " (" ^ (ppVal 0 e1) ^ ") instead", pos)
    end

(* Index into an array. Check that the index is not out of bounds. *)
fun applyIndexing( ArrayVal(lst, tp), IntVal ind, pos ) =
        let val len = List.length(lst)
        in if( len > ind andalso ind >= 0 )
             then List.nth(lst,ind)
             else raise Error("Array index out of bounds! Array length: "^
                              Int.toString(len)^" Index: "^Int.toString(ind), pos )
        end
  | applyIndexing( arr, IntVal ind, pos ) =
    raise Error("Indexing Error: " ^ (ppVal 0 arr) ^ " is not an array", pos)
  | applyIndexing( arr, e, pos ) = (* Order of clauses is important here. *)
    invalidOperand "Indexing error, non-integral index" Int e pos

(* Bind the formal parameters of a function declaration to actual parameters in
   a new vtab. *)
fun bindParams ([], [], fid, pd, pc) = SymTab.empty()
  | bindParams ([], a,  fid, pd, pc) =
        raise Error("Number of formal and actual params do not match in call to "^fid, pc)
  | bindParams (b,  [], fid, pd, pc) =
        raise Error("Number of formal and actual params do not match in call to "^fid, pc)
  | bindParams ( Param (faid, fatp)::fargs, a::aargs, fid, pd, pc ) =
        let val vtab   = bindParams( fargs, aargs, fid, pd, pc )
        in  if( typeMatch(fatp, a) )
              then case SymTab.lookup faid vtab of
                     NONE   => SymTab.bind faid a vtab
                   | SOME m => raise Error("Formal argument is already in symbol table!"^
                                           " In function: "^fid^" formal argument: "^faid, pd)
              else raise Error("Actual and formal argument type do not match"^
                               " in function: "^fid^"; formal argument: "^faid^
                               " of type: "^ppType(fatp)^
                               " does not matches actual argument: "^
                               ppVal 0 a, pc)
        end


(* Interpreter for Fasto expressions:
    1. vtab holds bindings between variable names and
       their interpreted value (Fasto.Value).
    2. ftab holds bindings between function names and
       function declarations (Fasto.FunDec).
    3. Returns the interpreted value. *)
fun evalExp ( Constant (v,_), vtab, ftab ) = v
  | evalExp ( ArrayLit (l, t, pos), vtab, ftab ) =
        let val els = (map (fn x => evalExp(x, vtab, ftab)) l)
            val elt = case els of []   => Int (* Arbitrary *)
                                | v::_ => valueType v
        in ArrayVal (els, elt)
        end

  | evalExp ( StringLit(s, pos), vtab, ftab ) =
        let val str  = String.explode s
            val exps = map (fn c => CharVal c) str
        in ArrayVal (exps, Char)
        end

  | evalExp ( Var(id, pos), vtab, ftab ) =
        let val res = SymTab.lookup id vtab
        in case res of
             NONE   => raise Error("Unknown variable "^id, pos)
           | SOME m => m
        end

  | evalExp ( Plus(e1, e2, pos), vtab, ftab ) =
        let val res1   = evalExp(e1, vtab, ftab)
            val res2   = evalExp(e2, vtab, ftab)
        in  case (res1, res2) of
              (IntVal n1, IntVal n2) => IntVal (n1+n2)
            | _ => invalidOperands "Plus on non-integral args: " [(Int, Int)] res1 res2 pos
        end

  | evalExp ( Minus(e1, e2, pos), vtab, ftab ) =
        let val res1   = evalExp(e1, vtab, ftab)
            val res2   = evalExp(e2, vtab, ftab)
        in  case (res1, res2) of
              (IntVal n1, IntVal n2) => IntVal (n1-n2)
            | _ => invalidOperands "Minus on non-integral args: " [(Int, Int)] res1 res2 pos
        end
(*============================ Start of my code ==============================*)
(* @author De Marchi Mirco
   @date   25-05-2019
   @brief  Interpreter for Times, Divide, And, Or, Not and Negate.
           Boolean (true and false) are implemented in Constant branch        *)
  | evalExp ( Times(e1, e2, pos), vtab, ftab ) =
        let val res1   = evalExp(e1, vtab, ftab)
            val res2   = evalExp(e2, vtab, ftab)
        in  case (res1, res2) of
            (IntVal n1, IntVal n2) => IntVal (n1*n2)
          | _ => invalidOperands "Times on non-integral args: " [(Int, Int)] res1 res2 pos
        end
    (*raise Fail "Unimplemented feature multiplication"    *)

  | evalExp ( Divide(e1, e2, pos), vtab, ftab ) =
        let val res1   = evalExp(e1, vtab, ftab)
            val res2   = evalExp(e2, vtab, ftab)
        in  case (res1, res2) of
            (IntVal n1, IntVal 0 ) => raise Error("Trying to divide by 0", pos)
          | (IntVal n1, IntVal n2) => IntVal (Int.quot(n1,n2))
          | _ => invalidOperands "Divide on non-integral args: " [(Int, Int)] res1 res2 pos
        end
    (* raise Fail "Unimplemented feature division" *)

  | evalExp (And (e1, e2, pos), vtab, ftab) =
        let val res1   = evalExp(e1, vtab, ftab)
        in  case (res1) of
            (BoolVal false) => BoolVal (false)
          | (BoolVal true ) => let val res2   = evalExp(e2, vtab, ftab)
                               in  case (res2) of
                                   (BoolVal b) => BoolVal (b)
                                 | _ => invalidOperand "And on non-integral arg: " Bool res2 pos
                               end
          | _ => invalidOperand "And on non-integral arg: " Bool res1 pos
        end
    (* raise Fail "Unimplemented feature &&" *)

   | evalExp (Or (e1, e2, pos), vtab, ftab) =
        let val res1 = evalExp(e1, vtab, ftab)
        in  case (res1) of
            (BoolVal true ) => BoolVal (true)
          | (BoolVal false) => let val res2   = evalExp(e2, vtab, ftab)
                               in  case res2 of
                                   (BoolVal b) => BoolVal (b)
                                 | _ => invalidOperand "Or on non-integral arg: " Bool res2 pos
                               end
          | _ => invalidOperand "Or on non-integral arg: " Bool res1 pos
        end
    (* raise Fail "Unimplemented feature ||" *)

  | evalExp ( Not(e, pos), vtab, ftab ) =
        let val res    = evalExp(e, vtab, ftab)
        in  case res of
            (BoolVal b) => BoolVal(not(b))
          | _ => invalidOperand "Not on non-integral arg: " Bool res pos
        end
    (* raise Fail "Unimplemented feature not" *)

  | evalExp ( Negate(e, pos), vtab, ftab ) =
        let val res    = evalExp(e, vtab, ftab)
        in  case res of
            (IntVal n) => IntVal(0-n-1)
          | _ => invalidOperand "Negate on non-integral arg: " Int res pos
        end
    (* raise Fail "Unimplemented feature negate" *)
(*============================================================================*)
  | evalExp ( Equal(e1, e2, pos), vtab, ftab ) =
        let val r1 = evalExp(e1, vtab, ftab)
            val r2 = evalExp(e2, vtab, ftab)
        in  case (r1, r2) of
              (IntVal  n1, IntVal  n2) => BoolVal (n1 = n2)
            | (BoolVal b1, BoolVal b2) => BoolVal (b1 = b2)
            | (CharVal c1, CharVal c2) => BoolVal (c1 = c2)
            | (_, _) => invalidOperands "Invalid equality operand types" [(Int, Int), (Bool, Bool), (Char, Char)] r1 r2 pos
        end

  | evalExp ( Less(e1, e2, pos), vtab, ftab ) =
        let val r1 = evalExp(e1, vtab, ftab)
            val r2 = evalExp(e2, vtab, ftab)
        in  case (r1, r2) of
              (IntVal  n1,    IntVal  n2  ) => BoolVal (n1 < n2)
            | (BoolVal false, BoolVal true) => BoolVal true
            | (BoolVal _,     BoolVal _   ) => BoolVal false
            | (CharVal c1,    CharVal c2  ) => BoolVal ( Char.ord(c1) < Char.ord(c2) )
            | (_, _) => invalidOperands "Invalid less-than operand types" [(Int, Int), (Bool, Bool), (Char, Char)] r1 r2 pos
        end

  | evalExp ( If(e1, e2, e3, pos), vtab, ftab ) =
        let val cond = evalExp(e1, vtab, ftab)
        in case cond of
              BoolVal true  => evalExp(e2, vtab, ftab)
           |  BoolVal false => evalExp(e3, vtab, ftab)
           |  other         => raise Error("If condition is not a boolean", pos)
        end

  | evalExp ( Apply(fid, args, pos), vtab, ftab ) =
        let val evargs = map (fn e => evalExp(e, vtab, ftab)) args
        in case SymTab.lookup fid ftab of
               SOME f => callFunWithVtable(f, evargs, SymTab.empty(), ftab, pos)
             | NONE   => raise Error("Call to unkwn function "^fid, pos)
        end

  | evalExp ( Let(Dec(id,e,p), exp, pos), vtab, ftab ) =
        let val res   = evalExp(e, vtab, ftab)
            val nvtab = SymTab.bind id res vtab
        in  evalExp(exp, nvtab, ftab)
        end

  | evalExp ( Index(id, e, tp, pos), vtab, ftab ) =
        let val indv= evalExp(e, vtab, ftab)
            val arr = SymTab.lookup id vtab
        in case (arr, indv) of
             (NONE, _)   => raise Error("Unknown array "^id, pos)
           | (SOME (ArrayVal(lst, tp)), IntVal ind) =>
                let val len = List.length(lst)
                in if( len > ind andalso ind >= 0 )
                   then List.nth(lst,ind)
                   else raise Error( "Array index out of bounds: array length: "^
                                     Int.toString(len)^", index: "^Int.toString(ind), pos )
                end
           | (SOME m, IntVal _) => raise Error("Indexing error: " ^ (ppVal 0 m) ^ " is not an array", pos)
           | (_, _) => invalidOperand "Indexing error, non-integral index" Int indv pos

        end
(*============================ Start of my code ==============================*)
(* @author De Marchi Mirco
   @date   25-05-2019
   @brief  Interpreter for functions Iota, Map and Reduce.                    *)
  | evalExp ( Iota (e, pos), vtab, ftab ) =
        let val res = evalExp(e, vtab, ftab)
            fun tmp n (ArrayVal(arr, Int)) = tmp (n-1) (ArrayVal((IntVal(n-1))::arr, Int))
              | tmp 0 (ArrayVal(arr, Int)) = arr
              | tmp _ _ = raise Error("Array type error", pos)
        in case res of
            (IntVal n) => ArrayVal( tmp n (ArrayVal([], Int)), Int )
          | _ => invalidOperand "Iota error, non-integer arg" Int res pos
        end
    (* raise Fail "Unimplemented feature iota" *)

  | evalExp ( Map (farg, arrexp, _, _, pos), vtab, ftab ) =
        let val res = evalExp(arrexp, vtab, ftab)
            fun tmp farg (n::arrn) vtab ftab pos
                      = let val ret = evalFunArg(farg, vtab, ftab, pos, [n])
                        in ret::(tmp farg arrn vtab ftab pos)
                        end
              | tmp _ _ _ _ _ = []
        in case res of
            (ArrayVal(arr, Int)) => ArrayVal( tmp farg arr vtab ftab pos, Int)
          | _ => invalidOperand "Map error, non array of integer arg" (Array(Int)) res pos
        end
    (* raise Fail "Unimplemented feature map" *)

  | evalExp ( Reduce (farg, ne, arrexp, tp, pos), vtab, ftab ) =
        let val res1 = evalExp(arrexp, vtab, ftab)
            val res2 = evalExp(ne, vtab, ftab)
            fun tmp farg ne (n::arrn) vtab ftab pos
                      = let val ret = evalFunArg(farg, vtab, ftab, pos, [ne, n])
                        in tmp farg ret arrn vtab ftab pos
                        end
              | tmp _ ne _ _ _ _ = ne
        in case res1 of
            (ArrayVal(arr, Int)) => tmp farg res2 arr vtab ftab pos
          | _ => invalidOperand "Reduce error, non array of integer arg" (Array(Int)) res1 pos
        end
    (* raise Fail "Unimplemented feature reduce" *)
(*============================================================================*)
  | evalExp ( Read (t,p), vtab, ftab ) =
        let val str =
                case TextIO.inputLine(TextIO.stdIn) of
                    SOME line => line
                  | NONE      =>
                    raise Error("End of line when reading input", p)
        in case t of
             Int => let val v = Int.fromString(str)
                    in case v of
                           SOME n    => IntVal n
                         | otherwise => raise Error("read(int) Failed! ", p)
                    end
           | Bool => let val v = Int.fromString(str) (* Bool.fromString(str) *)
                     in case v of
                            SOME b    => if( b=0 ) then BoolVal false else BoolVal true
                          | otherwise => raise Error("read(bool) Failed; 0==false, 1==true! ", p)
                     end
           | Char => let val v = Char.fromCString(str)
                     in case v of
                            SOME c    => CharVal c
                          | otherwise => raise Error("read(char) Failed!  ", p)
                     end
           | otherwise => raise Error("Read operation is valid only on basic types ", p)
        end

  | evalExp ( Write(exp,t,p), vtab, ftab ) =
        let val v  = evalExp(exp, vtab, ftab)
            val () =
            case v of
              IntVal n => print( (Int.toString n) ^ " " )
            | BoolVal b => let val res = if(b) then "True " else "False " in print( res ) end
            | CharVal c => print( (Char.toCString c)^" " )
            | ArrayVal (a, Char) =>
              let fun mapfun e =
                      case e of
                          CharVal c => c
                        | otherwise => raise Error("Write argument " ^
                                                   ppVal 0 v ^
                                                   " should have been evaluated to string", p)
              in print( String.implode (map mapfun a) )
              end
            | otherwise => raise Error("Write can be called only on basic and array(char) types ", p)
        in v
        end

(* finds the return type of a function argument *)
and rtpFunArg (FunName fid, ftab, callpos) =
    ( case SymTab.lookup fid ftab of
        NONE   => raise Error("Call to unknown function "^fid, callpos)
      | SOME (FunDec (_, rettype, _, _, _)) => rettype
    )
  | rtpFunArg (Lambda (rettype, _, _, _), _, _) = rettype

(* evalFunArg takes as argument a FunArg, a vtable, an ftable, the
position where the call is performed, and the list of actual arguments.
It returns a pair of two values: the result of calling the (lambda) function,
and the return type of the FunArg.
 *)
and evalFunArg (FunName fid, vtab, ftab, callpos, aargs) =
    let
      val fexp = SymTab.lookup fid ftab
    in
      case fexp of
        NONE   => raise Error("Call to known function "^fid, callpos)
      | SOME f => callFunWithVtable(f, aargs, SymTab.empty(), ftab, callpos)
    end
  | evalFunArg (Lambda (rettype, params, body, fpos), vtab, ftab, callpos, aargs) =
    callFunWithVtable ( FunDec ("<anonymous>", rettype, params, body, fpos)
                      , aargs, vtab, ftab, callpos )

(* Interpreter for Fasto function calls:
    1. f is the function declaration.
    2. args is a list of (already interpreted) arguments.
    3. vtab is the variable symbol table
    4. ftab is the function symbol table (containing f itself).
    5. pcall is the position of the function call. *)
and callFunWithVtable (FunDec (fid, rtp, fargs, body, pdcl), aargs, vtab, ftab, pcall) =
    case fid of
      (* treat the special functions *)
        "ord" => (case aargs of
                      [CharVal c] => IntVal (ord(c))
                    | otherwise => raise Error("Argument of \"ord\" does not evaluate to Char: "^
                                               String.concat(map (ppVal 0) aargs), pcall)
                 )
      | "chr" => (case aargs of
                      [IntVal n] => CharVal (chr(n))
                    | otherwise => raise Error("Argument of \"chr\" does not evaluate to Num: "^
                                               String.concat(map (ppVal 0) aargs), pcall)
                 )
      | _ =>
        let
            val vtab' = SymTab.combine (bindParams (fargs, aargs, fid, pdcl, pcall)) vtab
            val res  = evalExp (body, vtab', ftab)
        in
            if typeMatch (rtp, res)
            then res
            else raise Error("Result type does not match the return type"^
                             " in function: "^fid^" return type: "^ppType(rtp)^
                             " result: "^ppVal 0 res, pcall)
        end

(* Interpreter for Fasto programs:
    1. builds the function symbol table,
    2. interprets the body of "main", and
    3. returns its result. *)
and evalProg prog =
    let
      val ftab  = buildFtab prog
      val mainf = SymTab.lookup "main" ftab
    in
      case mainf of
        NONE   => raise Error("Could not find the main function", (0,0))
      | SOME m =>
          case getFunArgs m of
            [] => callFunWithVtable(m, [], SymTab.empty(), ftab, (0,0))
          | _  => raise Error("The main function is not allowed to have parameters", getFunPos m)
    end

end
