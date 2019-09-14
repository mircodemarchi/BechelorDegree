(* We will inline any function that does not call itself. *)
structure Inlining = struct

(*
    (* An optimisation takes a program and returns a new program. *)
    val optimiseProgram : Fasto.KnownTypes.Prog -> Fasto.KnownTypes.Prog
*)

open Fasto
open Fasto.KnownTypes

fun inlineInExp graph prog e =
    case e of
        Constant _ => e
      | StringLit _ => e
      | ArrayLit (es, t, pos) =>
        ArrayLit (map (inlineInExp graph prog) es, t, pos)
      | Var _ => e
      | Plus (e1, e2, pos) =>
        Plus (inlineInExp graph prog e1,
              inlineInExp graph prog e2, pos)
      | Minus (e1, e2, pos) =>
        Minus (inlineInExp graph prog e1,
               inlineInExp graph prog e2, pos)
      | Equal (e1, e2, pos) =>
        Equal (inlineInExp graph prog e1,
               inlineInExp graph prog e2, pos)
      | Less (e1, e2, pos) =>
        Less (inlineInExp graph prog e1,
              inlineInExp graph prog e2, pos)
      | If (e1, e2, e3, pos) =>
        If (inlineInExp graph prog e1,
            inlineInExp graph prog e2,
            inlineInExp graph prog e3,
            pos)
      | Apply (fname, es, pos) =>
        if CallGraph.calls fname fname graph then
            (* Function is recursive - do not inline. *)
            Apply (fname, map (inlineInExp graph prog) es, pos)
        else (* OK - inline. *)
            inlineFuncall fname graph prog es pos
      | Let (Dec (name, e, decpos), body, pos) =>
        Let (Dec (name, inlineInExp graph prog e, decpos),
             inlineInExp graph prog body,
             pos)
      | Index (name, e, t, pos) =>
        Index (name, inlineInExp graph prog e, t, pos)
      | Iota (e, pos) =>
        Iota (e, pos)
      | Map (farg, e, t1, t2, pos) =>
        Map (inlineInFunArg graph prog farg,
             inlineInExp graph prog e,
             t1, t2, pos)
      | Reduce (farg, e1, e2, t, pos) =>
        Reduce (inlineInFunArg graph prog farg,
                inlineInExp graph prog e1,
                inlineInExp graph prog e2,
                t, pos)
      | Times (e1, e2, pos) =>
        Times (inlineInExp graph prog e1,
               inlineInExp graph prog e2,
               pos)
      | Divide (e1, e2, pos) =>
        Divide (inlineInExp graph prog e1,
                inlineInExp graph prog e2,
                pos)
      | And (e1, e2, pos) =>
        And (inlineInExp graph prog e1,
             inlineInExp graph prog e2,
             pos)
      | Or (e1, e2, pos) =>
        Or (inlineInExp graph prog e1,
            inlineInExp graph prog e2,
            pos)
      | Not (e, pos) =>
        Not (inlineInExp graph prog e, pos)
      | Negate (e, pos) =>
        Negate (inlineInExp graph prog e, pos)
      | Read (t, pos) =>
        Read (t, pos)
      | Write (e, t, pos) =>
        Write (inlineInExp graph prog e, t, pos)

and inlineInFunArg graph prog (Lambda (rettype, params, body, pos)) =
    Lambda (rettype, params, inlineInExp graph prog body, pos)
  | inlineInFunArg graph prog (FunName fname) =
    case List.find (fn (FunDec (x, _, _, _, _)) => x = fname) prog of
        NONE => (* Must be a built-in function that cannot be inlined. *)
        FunName fname
      | SOME (FunDec (_, rettype, params, body, pos)) =>
        inlineInFunArg graph prog (Lambda (rettype, params, body, pos))

and inlineFuncall fname graph prog args pos =
    case List.find (fn (FunDec (x, _, _, _, _)) => x = fname) prog of
        NONE => (* Must be a built-in function that cannot be inlined. *)
        Apply (fname, map (inlineInExp graph prog) args, pos)
      | SOME (FunDec (_, _, params, body, _)) =>
        let val paramBindings = ListPair.zip (params, args)
            fun mkLetsAroundBody [] = body
              | mkLetsAroundBody ((Param (paramname, paramtype), arg) :: rest) =
                Let (Dec (paramname, arg, pos),
                     mkLetsAroundBody rest,
                     pos)
        in inlineInExp graph prog (mkLetsAroundBody paramBindings)
        end

fun inlineInFunction graph prog (FunDec (fname, rettype, params, body, pos)) =
    FunDec (fname, rettype, params, inlineInExp graph prog body, pos)

fun optimiseProgram prog =
    let val graph = CallGraph.callGraph prog
    in map (inlineInFunction graph prog) prog
    end
end
