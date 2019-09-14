(* A register allocator for MIPS. *)

structure RegAlloc = struct


(*

  exception error of string

  val registerAlloc : Mips.mips list -> string list -> int -> int -> int -> int
                      -> Mips.mips list * string list * int * int

(* registerAlloc takes a list of MIPS instructions, a list of
   registers that are live at the end of the code, three register
   numbers:
     1) The lowest allocatable register (typically 2).
     2) The highest caller-saves register.
     3) The highest allocatable register (typically 25).
   and the number of already spilled variables.  This should be 0 in the initial
   call unless some variables are forced to spill before register allocation.
   Registers up to (and including) the highest caller-saves
   register are assumed to be caller-saves. Those above are assumed to
   be callee-saves.

   registerAlloc returns:
   a modified instruction list where null moves have been removed,
   a list of the variables that are live at entry,
   plus a number indicating the highest used register number.

   The latter can be used for deciding which callee-saves registers
   need to be saved.

   Limitations:

    - Works for a single procedure body only.

    - Assumes all JALs eventually return to the next instruction and
      preserve callee-saves registers when doing so.

    - Does caller-saves preservation only by allocating variables that
      are live across procedure calls to callee-saves registers and
      variables not live across call preferably to caller-saves.

    - Can only remove null moves if they are implemented by ORI (rx,ry,"0").
      Use the pseudo-instruction MOVE (rx,ry) for this.

*)


*)

open Mips;

exception error of string

exception not_colourable of string

val spilledVars = ref []

(* Functions to implement naive sets of strings *)

val emptyset = []

fun addset (a:string) [] = [a]
  | addset a (b :: l) =
      if a<b then a :: b :: l
      else if a=b then b :: l
      else b :: addset a l

fun addsetL [] s = s
  | addsetL (a :: l) s = addset a (addsetL l s)

fun union [] l2 = l2
  | union (a::l1) l2 = addset a (union l1 l2)

fun remove (a:string) [] = []
  | remove a (b :: l)  =
      if a<b then b :: l
      else if a=b then l
      else b :: remove a l

fun setdiff l1 [] = l1
  | setdiff l1 (a::l2) = remove a (setdiff l1 l2)

fun member (a:string) [] = false
  | member a (b :: l)  =
      if a<b then false
      else if a=b then true
      else member a l

fun intersect [] l2 = []
  | intersect (a::l1) l2 =
      if member a l2 then a :: intersect l1 l2
      else intersect l1 l2

fun zip [] _ = []
  | zip _ [] = []
  | zip (a :: aas) (b :: bs) = (a,b) :: zip aas bs

fun destRegs [] = []
  | destRegs (i::ilist) = union (destReg i) (destRegs ilist)

and destReg i = (* variables and registers that can be overwritten *)
  case i of
    LA (rt,v) => addset rt emptyset
  | LUI (rt,v) => addset rt emptyset
  | ADD (rd,rs,rt) => addset rd emptyset
  | ADDI (rd,rs,v) => addset rd emptyset
  | SUB (rd,rs,rt) => addset rd emptyset
  | MUL (rd,rs,rt) => addset rd emptyset
  | DIV (rd,rs,rt) => addset rd emptyset
  | AND (rd,rs,rt) => addset rd emptyset
  | ANDI (rd,rs,v) => addset rd emptyset
  | OR (rd,rs,rt) => addset rd emptyset
  | ORI (rd,rs,v) => addset rd emptyset
  | XOR (rd,rs,rt) => addset rd emptyset
  | XORI (rd,rs,v) => addset rd emptyset
  | SLL (rd,rt,v) => addset rd emptyset
  | SRA (rd,rt,v) => addset rd emptyset
  | SLT (rd,rs,rt) => addset rd emptyset
  | SLTI (rd,rs,v) => addset rd emptyset
  | JAL (lab,argRegs) => addset "31" argRegs
  | LW (rd,rs,v) => addset rd emptyset
  | LB (rd,rs,v) => addset rd emptyset
  | SYSCALL => addset "2" emptyset (* return value is in $2 *)
  | _ => emptyset


fun usedRegs i = (* variables and register that can be read by i *)
  case i of
    ADD (rd,rs,rt) => addsetL [rs,rt] emptyset
  | ADDI (rd,rs,v) => addset rs emptyset
  | SUB (rd,rs,rt) => addsetL [rs,rt] emptyset
  | MUL (rd,rs,rt) => addsetL [rs,rt] emptyset
  | DIV (rd,rs,rt) => addsetL [rs,rt] emptyset
  | AND (rd,rs,rt) => addsetL [rs,rt] emptyset
  | ANDI (rd,rs,v) => addset rs emptyset
  | OR (rd,rs,rt) => addsetL [rs,rt] emptyset
  | ORI (rd,rs,v) => addset rs emptyset
  | XOR (rd,rs,rt) => addsetL [rs,rt] emptyset
  | XORI (rd,rs,v) => addset rs emptyset
  | SLL (rd,rt,v) => addset rt emptyset
  | SRA (rd,rt,v) => addset rt emptyset
  | SLT (rd,rs,rt) => addsetL [rs,rt] emptyset
  | SLTI (rd,rs,v) => addset rs emptyset
  | BEQ (rs,rt,v) => addsetL [rs,rt] emptyset
  | BNE (rs,rt,v) => addsetL [rs,rt] emptyset
  | BGEZ (rs,v) => addsetL [rs] emptyset
  | J lab => emptyset
  | JAL (lab,argRegs) => addsetL argRegs emptyset
                (* argRegs are argument registers *)
  | JR (r,resRegs) => addsetL (r::resRegs) emptyset
                (* r is jump register,
                   resRegs are registers required to be live *)
  | LW (rd,rs,v) => addset rs emptyset
  | SW (rd,rs,v) => addsetL [rs,rd] emptyset
  | LB (rd,rs,v) => addset rs emptyset
  | SB (rd,rs,v) => addsetL [rs,rd] emptyset
  | SYSCALL => addsetL ["2","4","5"] emptyset
                (* $2 is control register and $4, $5 are arguments *)
  | _ => emptyset

fun live_step ilist llist liveAtEnd =
  let fun scan [] = []
        | scan (i::is) =
            let val ls1 = scan is
            in if null ls1 then [instruct i liveAtEnd]
               else instruct i (hd ls1) :: ls1
            end

      and instruct i live = (* live variables and registers *)
        case i of
          BEQ (rs,rt,v) => addsetL [rs,rt] (union live (live_at v))
        | BNE (rs,rt,v) => addsetL [rs,rt] (union live (live_at v))
        | BGEZ (rs,v)   => addsetL [rs] (union live (live_at v))
        | J lab => live_at lab
        | JR (r,resRegs) => addsetL (r::resRegs) emptyset
                (* r is jump register,
                   resRegs are registers required to be live *)
        | _ => union (usedRegs i) (setdiff live (destReg i))

      and live_at lab = search ilist llist lab

      and search [] [] lab = emptyset
        | search (LABEL k :: is) (l :: ls) lab =
            if k = lab then l else search is ls lab
        | search (_ :: is) (_ :: ls) lab = search is ls lab
        | search a b l = search a b l (* shouldn't happen *)
  in
    scan ilist
  end

fun iterate_live ilist llist liveAtEnd =
  let val llist1 = live_step ilist llist liveAtEnd
  in if llist1 = llist then llist
     else iterate_live ilist llist1 liveAtEnd
  end

fun init_list [] = []
  | init_list (i::is) = emptyset :: init_list is

(* live_regs finds for each instruction those symbolic register names *)
(* that are live at entry to this instruction *)

fun live_regs ilist liveAtEnd =
      iterate_live ilist (init_list ilist) liveAtEnd

fun regs [] rs = rs
  | regs (l :: llist) rs = union l (regs llist rs)

fun findRegs llist = filterSymbolic (regs llist [])

and filterSymbolic [] = []
  | filterSymbolic (a::l) =
      if numerical a then filterSymbolic l
      else a :: filterSymbolic l

(* conflicts ilist llist callerSaves r *)
(* finds those variables that interferere with r *)
(* in instructions ilist with live-out specified by llist *)
(* callerSaves are the caller-saves registers *)

fun conflicts [] [] callerSaves r =
      if numerical r then remove r callerSaves else []
                (* all numerical interfere with all other caller-saves *)
  | conflicts (ORI (rd,rs,"0") :: ilist) (l :: llist) callerSaves r =
      if r=rd  (* if destination *)
      then union (remove rs (remove r l)) (* interfere with live except rs *)
                 (conflicts ilist llist callerSaves r)
      else if r=rs  (* if source, no interference *)
      then conflicts ilist llist callerSaves r
      else if member r l (* otherwise, live interfere with rd *)
      then union [rd] (conflicts ilist llist callerSaves r)
      else conflicts ilist llist callerSaves r
  | conflicts (JAL (f,argRegs) :: ilist) (l :: llist) callerSaves r =
      if (member r l)  (* live vars interfere with caller-saves regs *)
      then union (remove r callerSaves)
                 (conflicts ilist llist callerSaves r)
      else if member r callerSaves
      then union (remove r l)
                 (conflicts ilist llist callerSaves r)
      else conflicts ilist llist callerSaves r
  | conflicts (i :: ilist) (l :: llist) callerSaves r =
      if (member r (destReg i)) (* destination register *)
      then union (remove r l)   (* conflicts with other live vars *)
                 (conflicts ilist llist callerSaves r)
      else if member r l        (* all live vars *)
      then union (destReg i)    (* conflict with destination *)
                 (conflicts ilist llist callerSaves r)
      else conflicts ilist llist callerSaves r
  | conflicts _ _ _ _ = raise error "conflicts used at undefined instance"

(* Interference graph is represented as a list of registers *)
(* each paired with a list of the registers with which it conflicts *)

fun graph ilist llist callerSaves =
  let val rs = callerSaves @ findRegs llist
  in
    zip rs (map (conflicts ilist ((tl llist)@[[]]) callerSaves) rs)
  end

(* finds move-related registers *)

fun findMoves ilist llist =
  let val rs = findRegs llist
  in
    zip rs (map (findMoves1 ilist) rs)
  end

and findMoves1 [] r = []
  | findMoves1 (ORI (rd,rs,"0") :: ilist) r =
      union (if rd=r then [rs]
             else if rs=r then [rd]
             else [])
            (findMoves1 ilist r)
  | findMoves1 (i :: ilist) r = findMoves1 ilist r

(* sorts by number of conflicts, but with numeric registers last *)

fun be4 (a, ac) (b, bc) =
      case (numerical a, numerical b) of
        (true,true) => a<=b
      | (true,false) => false
      | (false,true) => true
      | (false,false) =>
          (case (member a (!spilledVars), member b (!spilledVars)) of
             (false, false) => length ac <= length bc
           | (true, false) => false
           | (false,true) => true
           | (true,true) => length ac <= length bc)

fun sortByOrder [] = []
  | sortByOrder (g : (string * 'b list) list) =
     let fun split [] = ([],[])
           | split (a::g) =
               let val (l,g1) = ascending a g []
                   val (g2,g3) = split g1
               in (rev2 l g3, g2) end
         and ascending a [] l = (a::l,[])
           | ascending a (g as (b::g1)) l =
               if be4 a b
               then ascending b g1 (a::l)
               else (a::l,g)
         and rev2 [] l2 = l2
           | rev2 (a::l1) l2 = rev2 l1 (a::l2)
         fun merge [] l2 = l2
           | merge l1 [] = l1
           | merge (l1 as (a::r1)) (l2 as (b::r2)) =
               if be4 a b
               then a :: merge r1 l2
               else b :: merge l1 r2
         val (g1,g2) = split g
     in
       if null g1 then g2
       else if null g2 then g1
       else merge (sortByOrder g1) (sortByOrder g2)
     end

(* n-colour graph using Briggs' algorithm *)

fun colourGraph g rmin rmax moveRelated =
  select (simplify (sortByOrder g) [])
         (mList rmin rmax) moveRelated []

and simplify [] l = l
  | simplify ((a as (r,c)) :: g) l =
      simplify (sortByOrder (removeNode r g)) (a::l)

and removeNode r [] = []
  | removeNode r ((r1,c)::g) =
      (r1,remove r c) :: removeNode r g

and select [] regs moveRelated sl = sl
  | select ((r,c)::l) regs moveRelated sl =
      let val rnum =
               if numerical r then r
               else let val possible = NotIn c sl regs
                        val related = lookUp2 r moveRelated
                        val related2 = map (fn r=>lookUp r sl) related
                        val mPossible = intersect possible related2
                    in
                      if null possible then raise not_colourable r
                      else if null mPossible then hd possible
                      else hd mPossible
                    end
      in
        select l regs moveRelated ((r,rnum)::sl)
      end

and NotIn [] sl regs = regs
  | NotIn (r::cs) sl regs =
      NotIn cs sl (delete (lookUp r sl) regs)

and lookUp r [] = "0"
  | lookUp r ((r1,n)::sl) =
      if numerical r then r
      else if r=r1 then n else lookUp r sl

and lookUp2 r [] = []
  | lookUp2 r ((r1,ms)::sl) =
      if r=r1 then ms else lookUp2 r sl

and delete x [] = []
  | delete x (y::l) = if x=y then delete x l else y :: delete x l

and mList m n =
  if m > n then []
  else makestring m :: mList (m+1) n

fun filterNullMoves [] allocs = []
  | filterNullMoves (ORI (rd,rs,"0") :: ilist) allocs =
      let val rd1 = lookUp rd allocs
          val rs1 = lookUp rs allocs
      in
        if rd1 = rs1 orelse rd1 = "0"
        then COMMENT ("\tori\t"^rd^","^rs^",0")
             :: filterNullMoves ilist allocs
        else ORI (rd,rs,"0") :: filterNullMoves ilist allocs
      end
  | filterNullMoves (i :: ilist) allocs =
      i :: filterNullMoves ilist allocs

and printList [] = ""
  | printList (r :: rs) = r^" "^ printList rs

fun printGraph [] = []
  | printGraph ((r,rs) :: g) =
     [COMMENT ("interferes: "^r^" with "^printList rs)]
     @ printGraph g

fun renameReg allocs inst =
    case inst of
      LA (rt,v) =>
        [LA (lookUp rt allocs, v),
         COMMENT ("was:\tla\t"^rt^", "^v)]
    | LUI (rt,v) =>
        [LUI (lookUp rt allocs, v),
         COMMENT ("was:\tlui\t"^rt^", "^v)]
    | ADD (rd,rs,rt) =>
        [ADD (lookUp rd allocs, lookUp rs allocs, lookUp rt allocs),
         COMMENT ("was:\tadd\t"^rd^", "^rs^", "^rt)]
    | ADDI (rd,rs,v) =>
        [ADDI (lookUp rd allocs, lookUp rs allocs, v),
         COMMENT ("was:\taddi\t"^rd^", "^rs^", "^v)]
    | SUB (rd,rs,rt) =>
        [SUB (lookUp rd allocs, lookUp rs allocs, lookUp rt allocs),
         COMMENT ("was:\tsub\t"^rd^", "^rs^", "^rt)]
    | MUL (rd,rs,rt) =>
        [MUL (lookUp rd allocs, lookUp rs allocs, lookUp rt allocs),
         COMMENT ("was:\tmul\t"^rd^", "^rs^", "^rt)]
    | DIV (rd,rs,rt) =>
        [DIV (lookUp rd allocs, lookUp rs allocs, lookUp rt allocs),
         COMMENT ("was:\tdiv\t"^rd^", "^rs^", "^rt)]
    | AND (rd,rs,rt) =>
        [AND (lookUp rd allocs, lookUp rs allocs, lookUp rt allocs),
         COMMENT ("was:\tand\t"^rd^", "^rs^", "^rt)]
    | ANDI (rd,rs,v) =>
        [ANDI (lookUp rd allocs, lookUp rs allocs, v),
         COMMENT ("was:\tandi\t"^rd^", "^rs^", "^v)]
    | OR (rd,rs,rt) =>
        [OR (lookUp rd allocs, lookUp rs allocs, lookUp rt allocs),
         COMMENT ("was:\tor\t"^rd^", "^rs^", "^rt)]
    | ORI (rd,rs,v) =>
        [ORI (lookUp rd allocs, lookUp rs allocs, v),
         COMMENT ("was:\tori\t"^rd^", "^rs^", "^v)]
    | XOR (rd,rs,rt) =>
        [XOR (lookUp rd allocs, lookUp rs allocs, lookUp rt allocs),
         COMMENT ("was:\txor\t"^rd^", "^rs^", "^rt)]
    | XORI (rd,rs,v) =>
        [XORI (lookUp rd allocs, lookUp rs allocs, v),
         COMMENT ("was:\txori\t"^rd^", "^rs^", "^v)]
    | SLL (rd,rt,v) =>
        [SLL (lookUp rd allocs, lookUp rt allocs, v),
         COMMENT ("was:\tsll\t"^rd^", "^rt^", "^v)]
    | SRA (rd,rt,v) =>
        [SRA (lookUp rd allocs, lookUp rt allocs, v),
         COMMENT ("was:\tsra\t"^rd^", "^rt^", "^v)]
    | SLT (rd,rs,rt) =>
        [SLT (lookUp rd allocs, lookUp rs allocs, lookUp rt allocs),
         COMMENT ("was:\tslt\t"^rd^", "^rs^", "^rt)]
    | SLTI (rd,rs,v) =>
        [SLTI (lookUp rd allocs, lookUp rs allocs, v),
         COMMENT ("was:\tandi\t"^rd^", "^rs^", "^v)]
    | BEQ (rs,rt,v) =>
        [BEQ (lookUp rs allocs, lookUp rt allocs, v),
         COMMENT ("was:\tbeq\t"^rs^", "^rt^", "^v)]
    | BGEZ(rs,v) =>
        [BGEZ(lookUp rs allocs, v),
         COMMENT ("was:\tbgez\t"^rs^", "^v)]
    | BNE (rs,rt,v) =>
        [BNE (lookUp rs allocs, lookUp rt allocs, v),
         COMMENT ("was:\tbne\t"^rs^", "^rt^", "^v)]
    | JAL (lab,argRegs) =>
        [JAL (lab, List.map (fn r=>lookUp r allocs) argRegs),
         COMMENT ("was:\tjal\t"^lab^", "^String.concat argRegs)]
    | JR (r, resRegs) =>
        [JR (lookUp r allocs, List.map (fn r=>lookUp r allocs) resRegs),
         COMMENT ("was:\tjr\t"^r^", "^String.concat resRegs)]
    | LW (rd,rs,v) =>
        [LW (lookUp rd allocs, lookUp rs allocs, v),
         COMMENT ("was:\tlw\t"^rd^", "^v^"("^rs^")")]
    | SW (rd,rs,v) =>
        [SW (lookUp rd allocs, lookUp rs allocs, v),
         COMMENT ("was:\tsw\t"^rd^", "^v^"("^rs^")")]
    | LB (rd,rs,v) =>
        [LB (lookUp rd allocs, lookUp rs allocs, v),
         COMMENT ("was:\tlb\t"^rd^", "^v^"("^rs^")")]
    | SB (rd,rs,v) =>
        [SB (lookUp rd allocs, lookUp rs allocs, v),
         COMMENT ("was:\tsb\t"^rd^", "^v^"("^rs^")")]
    | _ => [inst]


fun spill1 i r offset =
  let
    val d = destReg i
    val u = usedRegs i
  in
    (if member r u
     then [Mips.LW (r,"29",offset)]
     else [])
    @ [i] @
    (if member r d
     then [Mips.SW (r,"29",offset)]
     else [])
  end

fun spill [] r offset = []
  | spill (i::is) r offset =
      spill1 i r offset @ spill is r offset

fun maxreg [] m = m
  | maxreg ((r,n)::rs) m =
      maxreg rs (if m< intOfString n then intOfString n else m)

(* arguments:
   ilist is list of MIPS instructions
   liveAtEnd is list of variables that are live at the end of ilist
   rmin is first allocable register (caller-saves)
   callerMax is highest caller-saves register
   rmax is highest allocable register
   spilled is number of registers spilled so far -- should be 0 initially
*)

fun registerAlloc ilist liveAtEnd rmin callerMax rmax spilled =
  let
      val llist = live_regs ilist liveAtEnd
      val callerSaves = mList rmin callerMax
      val iGraph = graph ilist llist callerSaves
      val moveRelated = findMoves ilist llist
      val allocs = colourGraph iGraph rmin rmax moveRelated
      val deadRegs = setdiff (filterSymbolic (destRegs ilist))
                             (map (#1) allocs)
      val allocs1 = allocs @ (map (fn r => (r,"0")) deadRegs)
      val ilist1 = filterNullMoves ilist allocs1
      val ilist2 = List.concat (List.map (renameReg allocs1) ilist1)
  in
    (ilist2, hd llist, maxreg allocs 0, spilled)
  end
  handle not_colourable r =>
    let
      val _ = TextIO.output (TextIO.stdOut, r ^ " spilled\n")
      val _ = spilledVars := r :: (!spilledVars)
      val offset = Int.toString (4*spilled)
      val ilist' = spill ilist r offset
      val ilist'' = [Mips.SW (r,"29",offset)]
                    @ ilist' @
                    (if member r liveAtEnd
                     then [Mips.LW (r,"29",offset)]
                     else [])
    in
      registerAlloc ilist'' liveAtEnd rmin callerMax rmax (spilled + 1)
    end


end
