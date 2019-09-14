(* Types and utilities for the abstract syntax of Fasto. *)

(*

Fasto er et funktionelt array-sprog til oversættelse, F-A-S-T-O.
Fasto er også et spansk ord, der betyder "pomp" eller "pragt".
Derfor skal vi programmere en "pragtfuld" oversætter for Fasto.

*)

structure Fasto = struct

(*

The abstract syntax of a (Fasto) program is a representation of the (Fasto)
program in terms of a data type in another programming language (SML).

Some expressions in Fasto (e.g. array constants, indexing operations, map,
reduce) are implicitly typed, their types are not explicitly stated in the
program text. Their types are inferred at run-time by an interpreter, or at
compile-time by a type-checker.

In support of this, this module defines two abstract syntax, UnknownTypes and
KnownTypes. To avoid code duplication when defining the syntax, we make use
of a functor, FastoFn.  This requires support for higher-order modules, which
is a Moscow ML extension to the SML Modules language. To avoid compliance
warnings, this module must be compiled with the -liberal option[1].

Our abstract syntax store not just the program structure, but also the
positions of the program substructures in the original program text. This is
useful for reporting errors at later passes of the compiler, e.g. type errors.

This module also provides pretty printing functionality, printing a valid Fasto
program given its abstract syntax. "pp" is used in this module as a shorthand
for "prettyPrint".

[1] See also the Moscow ML Owner's Manual [http://moscowml.org/manual.pdf].

*)

type pos = int * int  (* position: (line, column) *)

datatype Type =
    Int
  | Bool
  | Char
  | Array of Type

datatype Value =
    IntVal of int
  | BoolVal of bool
  | CharVal of char
  | ArrayVal of Value list * Type

fun valueType (IntVal _)          = Int
  | valueType (BoolVal _)         = Bool
  | valueType (CharVal _)         = Char
  | valueType (ArrayVal (_, tp))  = Array tp

(* pretty printing a type *)
fun ppType Int = "int"
  | ppType Bool = "bool"
  | ppType Char = "char"
  | ppType (Array tp) = "[" ^ ppType tp ^ "]"

datatype Param = Param of string * Type

functor FastoFn (T : sig eqtype TypeAnnot end) = struct

  datatype Exp =
      Constant of Value * pos
    | StringLit of string * pos
    | ArrayLit of Exp list * T.TypeAnnot * pos
    | Var of string * pos
    | Plus of Exp * Exp * pos
    | Minus of Exp * Exp * pos
    | Equal of Exp * Exp * pos
    | Less of Exp * Exp * pos
    | If of Exp * Exp * Exp * pos
    | Apply of string * Exp list * pos
    | Let of Dec * Exp * pos
    | Index of string * Exp * T.TypeAnnot * pos                   (* arr[3]  *)

    (* dirty I/O *)
    | Read of Type * pos
    | Write of Exp * T.TypeAnnot * pos

    (* The following are project tasks. *)
    | Iota of Exp * pos                                           (* iota(n) *)
    | Map of FunArg * Exp * T.TypeAnnot * T.TypeAnnot * pos   (* map(f, arr) *)
    | Reduce of FunArg * Exp * Exp * T.TypeAnnot * pos  (* reduce(f, 0, arr) *)

    | Times of Exp * Exp * pos
    | Divide of Exp * Exp * pos
    | And of Exp * Exp * pos
    | Or of Exp * Exp * pos
    | Not of Exp * pos
    | Negate of Exp * pos

  and Dec = Dec of string * Exp * pos

  and FunArg = Lambda of Type * Param list * Exp * pos
             | FunName of string

  (* A function declaration is a tuple of:
      (i) function name,
      (ii) return type,
      (iii) formal arguments names & types,
      (iv) function's body,
      (v) position. *)
  datatype FunDec = FunDec of string * Type * Param list * Exp * pos

  (* Some helper functions for accessing the fields of a function
     declaration. *)
  fun getFunName (FunDec (fid,_,_,_,_)) = fid
  fun getFunRTP  (FunDec (_,rtp,_,_,_)) = rtp
  fun getFunArgs (FunDec (_,_,arg,_,_)) = arg
  fun getFunBody (FunDec (_,_,_,bdy,_)) = bdy
  fun getFunPos  (FunDec (_,_,_,_,pos)) = pos

  type Prog = FunDec list

  (****************************************************)
  (********** Pretty-Printing Functionality ***********)
  (****************************************************)

  fun indent 0 = ""
    | indent n = "  " ^ indent (n-1)

  (* pretty printing a single function parameter *)
  fun ppParam  (Param (id, tp)) = ppType tp ^ " " ^ id

  (* pretty printing function parameters *)
  fun ppParams []      = ""
    | ppParams [bd]    = ppParam bd
    | ppParams (bd::l) = ppParam bd ^ ", " ^ ppParams l

  (* pretty printing an expression *)
  fun ppExp d (Constant (v,  pos))  = ppVal d v
    | ppExp d (StringLit (s,  pos)) = concat [ "\"", String.toCString s, "\"" ]
    | ppExp d (ArrayLit (es, t, pos)) =
        concat [ "{ ", String.concatWith ", " (map (ppExp d) es), " }" ]
    | ppExp d (Var (id, pos)) = id
    | ppExp d (Plus (e1, e2, pos))  =
        concat [ "(", ppExp d e1, " + ", ppExp d e2, ")" ]
    | ppExp d (Minus (e1, e2, pos)) =
        concat [ "(", ppExp d e1, " - ", ppExp d e2, ")" ]
    | ppExp d (Times (e1,e2,_))  = concat [ ppExp d e1, " * ", ppExp d e2 ]
    | ppExp d (Divide (e1,e2,_)) = concat [ ppExp d e1, " / ", ppExp d e2 ]
    | ppExp d (And (e1,e2,_))    = concat [ ppExp d e1, " && ", ppExp d e2 ]
    | ppExp d (Or (e1,e2,_))     = concat [ ppExp d e1, " || ", ppExp d e2 ]
    | ppExp d (Not (e,_))        = concat [ "not(", ppExp d e, ")" ]
    | ppExp d (Negate (e,_))     = concat [ "~(", ppExp d e, ")" ]
    | ppExp d (Equal (e1, e2, pos)) =
        concat [ "(", ppExp d e1, " == ", ppExp d e2, ")" ]
    | ppExp d (Less (e1, e2, pos))  =
        concat [ "(", ppExp d e1, " < ", ppExp d e2, ")" ]
    | ppExp d (If (e1, e2, e3, pos)) =
        String.concat
          [ "if (",  ppExp d e1,   ")\n"
          , indent (d+2), "then ", ppExp (d+2) e2, "\n"
          , indent (d+2), "else ", ppExp (d+2) e3, "\n"
          , indent d ]
    | ppExp d (Apply (id, args, pos)) =
        concat [ id, "(", String.concatWith ", " (map (ppExp d) args), ")" ]
    | ppExp d (Let (Dec(id, e1, pos1), e2, pos2)) =
      let val nl = case e2 of Let _ => ""
                            | _     => "\n"
      in concat [ "\n",
                  indent (d+1), "let ", id, " = ",
                  ppExp (d+2) e1, " in", nl,
                  indent (d+1), ppExp d e2 ]
      end
    | ppExp d (Index (id, e, t, pos)) = concat [ id, "[", ppExp d e, "]" ]
    | ppExp d (Iota (e, pos))         = concat [ "iota(", ppExp d e, ")" ]
    | ppExp d (Map(f,e,_,_,pos))  = concat ["map(",ppFunArg d f,", ",ppExp d e,")"]
    | ppExp d (Reduce(f, el, lst, t, pos)) =
        concat [ "reduce(", ppFunArg d f, ", ", ppExp d el, ", ", ppExp d lst, ")" ]
    | ppExp d (Read (t,p))    = concat [ "read(",  ppType t,  ")" ]
    | ppExp d (Write (e,t,p)) = concat [ "write(", ppExp d e, ")" ]

  and ppBindings bs = String.concatWith ", "
            (map (fn (n, e) => n ^ " <- " ^ ppExp 0 e) bs)

  and ppVal d (IntVal   n)  = Int.toString n
    | ppVal d (BoolVal   b) = Bool.toString b
    | ppVal d (CharVal c)   = "'" ^ Char.toCString c ^ "'"
    | ppVal d (ArrayVal (vals, t)) =
        "{ " ^ String.concatWith ", " (map (ppVal d) vals) ^ " }"

  and ppFunArg d (FunName s) = s
    | ppFunArg d (Lambda (ret_tp, args, body, pos)) =
      "fn " ^ ppType ret_tp ^ " (" ^ ppParams args ^ ") => " ^
      ppExp (d+2) body

  (* pretty printing a function declaration *)
  fun ppFun d (FunDec (id, ret_tp, args, body, pos)) =
    concat [ "fun ", ppType ret_tp, " ", id,
             "(", ppParams args, ") =",
             indent (d+1), ppExp (d+1) body ]

  (* pretty printing a PROGRAM *)
  fun ppProg (p : Prog) = String.concatWith "\n\n" (map (ppFun 0) p) ^ "\n"

  fun expPos e = case e of
                     Constant (_, p) => p
                   | StringLit (_, p) => p
                   | ArrayLit (_, _, p) => p
                   | Var (_, p) => p
                   | Plus (_, _, p) => p
                   | Minus (_, _, p) => p
                   | Equal (_, _, p) => p
                   | Less (_, _, p) => p
                   | If (_, _, _, p) => p
                   | Apply (_, _, p) => p
                   | Let (_, _, p) => p
                   | Index (_, _, _, p) => p
                   | Iota (_, p) => p
                   | Map (_, _, _, _, p) => p
                   | Reduce (_, _, _, _, p) => p
                   | Read (_, p) => p
                   | Write (_, _, p) => p
                   | Times (_, _, p) => p
                   | Divide (_, _, p) => p
                   | And (_, _, p) => p
                   | Or (_, _, p) => p
                   | Not (_, p) => p
                   | Negate (_, p) => p

end

structure UnknownTypes = FastoFn (struct type TypeAnnot = unit end)
structure KnownTypes = FastoFn (struct type TypeAnnot = Type end)

end
