structure CopyConstPropFold = struct

(*
    (* An optimisation takes a program and returns a new program. *)
    val optimiseProgram : Fasto.KnownTypes.Prog -> Fasto.KnownTypes.Prog
*)

open Fasto
open Fasto.KnownTypes

(* A propagatee is something that we can propagate - either a variable
   name or a constant value. *)
datatype Propagatee = ConstProp of Value
                    | VarProp of string

fun copyConstPropFoldExp vtable e =
    case e of
      (* Copy propagation is handled entirely in the following three
      cases for variables, array indexing, and let-bindings. *)
        Var (name, pos) =>
        (case SymTab.lookup name vtable of
             SOME (VarProp newname) => Var (newname, pos)
           | SOME (ConstProp value) => Constant (value, pos)
           | _                      => Var (name, pos))
      | Index (name, e, t, pos) =>
        (case SymTab.lookup name vtable of
             SOME (VarProp newname) =>
               Index (newname, copyConstPropFoldExp vtable e, t, pos)
          |  _ =>
               Index (name, copyConstPropFoldExp vtable e, t, pos))
      | Let (Dec (name, e, decpos), body, pos) =>
        let val e' = copyConstPropFoldExp vtable e
        in case e' of
               Var (varname, _) =>
               raise Fail "Cannot copy-propagate Var yet"
             | Constant (value, _) =>
               raise Fail "Cannot copy-propagate Constant yet"
             | Let (Dec bindee, inner_body, inner_pos) =>
               raise Fail "Cannot copy-propagate Let yet"
             | _ => (* Fallthrough - for everything else, do nothing *)
               let val body' = copyConstPropFoldExp vtable body
               in Let (Dec (name, e', decpos), body', pos)
               end
        end
      | Times (e1, e2, pos) =>
        let val e1' = copyConstPropFoldExp vtable e1
            val e2' = copyConstPropFoldExp vtable e2
        in Times (e1', e2', pos) (* Do something here. *)
        end
      | And (e1, e2, pos) =>
        let val e1' = copyConstPropFoldExp vtable e1
            val e2' = copyConstPropFoldExp vtable e2
        in And (e1', e2', pos) (* Do something here. *)
        end
      | Constant x => Constant x
      | StringLit x => StringLit x
      | ArrayLit (es, t, pos) =>
        ArrayLit (map (copyConstPropFoldExp vtable) es, t, pos)
      | Plus (e1, e2, pos) =>
        let val e1' = copyConstPropFoldExp vtable e1
            val e2' = copyConstPropFoldExp vtable e2
        in case (e1', e2') of
               (Constant (IntVal x, _), Constant (IntVal y, _)) =>
               Constant (IntVal (x+y), pos)
             | (Constant (IntVal 0, _), _) =>
               e2'
             | (_, Constant (IntVal 0, _)) =>
               e1'
             | _ =>
               Plus (e1', e2', pos)
        end
      | Minus (e1, e2, pos) =>
        let val e1' = copyConstPropFoldExp vtable e1
            val e2' = copyConstPropFoldExp vtable e2
        in case (e1', e2') of
               (Constant (IntVal x, _), Constant (IntVal y, _)) =>
               Constant (IntVal (x-y), pos)
             | (_, Constant (IntVal 0, _)) =>
               e1'
             | _ =>
               Minus (e1', e2', pos)
        end
      | Equal (e1, e2, pos) =>
        let val e1' = copyConstPropFoldExp vtable e1
            val e2' = copyConstPropFoldExp vtable e2
        in case (e1', e2') of
               (Constant (v1, _), Constant (v2, _)) =>
               Constant (BoolVal (v1 = v2), pos)
             | _ => if e1' = e2'
                    then Constant (BoolVal true, pos)
                    else Equal (e1', e2', pos)
        end
      | Less (e1, e2, pos) =>
        let val e1' = copyConstPropFoldExp vtable e1
            val e2' = copyConstPropFoldExp vtable e2
        in case (e1', e2') of
               (Constant (IntVal v1, _), Constant (IntVal v2, _)) =>
               Constant (BoolVal (v1 < v2), pos)
             | _ => if e1' = e2'
                    then Constant (BoolVal false, pos)
                    else Less (e1', e2', pos)
        end
      | If (e1, e2, e3, pos) =>
        let val e1' = copyConstPropFoldExp vtable e1
        in case e1' of
               Constant (BoolVal b, _) => if b
                                          then copyConstPropFoldExp vtable e2
                                          else copyConstPropFoldExp vtable e2
             | _ => If (e1',
                        copyConstPropFoldExp vtable e2,
                        copyConstPropFoldExp vtable e3,
                        pos)
        end
      | Apply (fname, es, pos) =>
        Apply (fname, map (copyConstPropFoldExp vtable) es, pos)
      | Iota (e, pos) =>
        Iota (copyConstPropFoldExp vtable e, pos)
      | Map (farg, e, t1, t2, pos) =>
        Map (copyConstPropFoldFunArg vtable farg,
             copyConstPropFoldExp vtable e,
             t1, t2, pos)
      | Reduce (farg, e1, e2, t, pos) =>
        Reduce (copyConstPropFoldFunArg vtable farg,
                copyConstPropFoldExp vtable e1,
                copyConstPropFoldExp vtable e2,
                t, pos)
      | Divide (e1, e2, pos) =>
        let val e1' = copyConstPropFoldExp vtable e1
            val e2' = copyConstPropFoldExp vtable e2
        in case (e1', e2') of
               (Constant (IntVal x, _), Constant (IntVal y, _)) =>
                 (Constant (IntVal (Int.quot (x,y)), pos)
                   handle Div => Divide (e1', e2', pos))
             | _ => Divide (e1', e2', pos)
        end
      | Or (e1, e2, pos) =>
        let val e1' = copyConstPropFoldExp vtable e1
            val e2' = copyConstPropFoldExp vtable e2
        in case (e1', e2') of
               (Constant (BoolVal a, _), Constant (BoolVal b, _)) =>
               Constant (BoolVal (a orelse b), pos)
             | _ => Or (e1', e2', pos)
        end
      | Not (e, pos) =>
        let val e' = copyConstPropFoldExp vtable e
        in case e' of
               Constant (BoolVal a, _) => Constant (BoolVal (not a), pos)
             | _ => Not (e', pos)
        end
      | Negate (e, pos) =>
        let val e' = copyConstPropFoldExp vtable e
        in case e' of
               Constant (IntVal x, _) => Constant (IntVal (~x), pos)
             | _ => Negate (e', pos)
        end
      | Read (t, pos) =>
        Read (t, pos)
      | Write (e, t, pos) =>
        Write (copyConstPropFoldExp vtable e, t, pos)

and copyConstPropFoldFunArg vtable (FunName fname) =
    FunName fname
  | copyConstPropFoldFunArg vtable (Lambda (rettype, params, body, pos)) =
    (* Remove any bindings with the same names as the parameters. *)
    let val paramNames = (map (fn (Param (name, _)) => name) params)
        val vtable' = SymTab.removeMany paramNames vtable
    in Lambda (rettype, params, copyConstPropFoldExp vtable' body, pos)
    end

fun copyConstPropFoldFunDec (FunDec (fname, rettype, params, body, loc)) =
    let val body' = copyConstPropFoldExp (SymTab.empty ()) body
    in FunDec (fname, rettype, params, body', loc)
    end

fun optimiseProgram prog =
    map copyConstPropFoldFunDec prog
end
