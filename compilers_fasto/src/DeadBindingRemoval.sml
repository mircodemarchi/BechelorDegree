structure DeadBindingRemoval = struct

(*
    val removeDeadBindings : Fasto.KnownTypes.Prog -> Fasto.KnownTypes.Prog
*)

open Fasto
open Fasto.KnownTypes

type Usages = string list

fun isUsed name usages = List.exists (fn x => x = name) usages
fun used name usages   = name :: usages

fun unzip3 [] = ([], [], [])
  | unzip3 ((x,y,z)::l) =
    let val (xs, ys, zs) = unzip3 l
    in (x::xs, y::ys, z::zs) end

val anytrue = List.exists (fn x => x)

fun removeDeadBindingsInExp e =
    case e of
        Constant x =>
        (false, [], Constant x)
      | StringLit x =>
        (false, [], StringLit x)
      | ArrayLit (es, t, pos) =>
        let val (ios, uses, es') = unzip3 (map removeDeadBindingsInExp es)
        in (anytrue ios,
            List.concat uses,
            ArrayLit (es', t, pos)) end
      | Var (name, pos) =>
        (false, [name], Var (name, pos))
      | Plus (x, y, pos) =>
        let val (xios, xuses, x') = removeDeadBindingsInExp x
            val (yios, yuses, y') = removeDeadBindingsInExp y
        in (xios orelse yios,
            xuses @ yuses,
            Plus (x', y', pos)) end
      | Minus (x, y, pos) =>
        let val (xios, xuses, x') = removeDeadBindingsInExp x
            val (yios, yuses, y') = removeDeadBindingsInExp y
        in (xios orelse yios,
            xuses @ yuses,
            Minus (x', y', pos)) end
      | Equal (x, y, pos) =>
        let val (xios, xuses, x') = removeDeadBindingsInExp x
            val (yios, yuses, y') = removeDeadBindingsInExp y
        in (xios orelse yios,
            xuses @ yuses,
            Equal (x', y', pos)) end
      | Less (x, y, pos) =>
        let val (xios, xuses, x') = removeDeadBindingsInExp x
            val (yios, yuses, y') = removeDeadBindingsInExp y
        in (xios orelse yios,
            xuses @ yuses,
            Less (x', y', pos)) end
      |  If (e1, e2, e3, pos) =>
         let val (ios1, uses1, e1') = removeDeadBindingsInExp e1
             val (ios2, uses2, e2') = removeDeadBindingsInExp e2
             val (ios3, uses3, e3') = removeDeadBindingsInExp e3
         in (ios1 orelse ios2 orelse ios3,
             uses1 @ uses2 @ uses3,
             If (e1', e2', e3', pos)) end
      | Apply (fname, args, pos) =>
        let val (ios, uses, args') = unzip3 (map removeDeadBindingsInExp args)
        in (anytrue ios,
            List.concat uses,
            Apply (fname, args', pos)) end
      | Let (Dec (name, e, decpos), body, pos) =>
        let val (eio, euses, e') = removeDeadBindingsInExp e
            val (bodyio, bodyuses, body') = removeDeadBindingsInExp body
        in (* We cannot remove bindings that perform impure I/O, even
              if the result is not used. *)
           if isUsed name bodyuses orelse eio
           then (eio orelse bodyio,
                 euses @ bodyuses,
                 Let (Dec (name, e', decpos), body', pos))
           else (bodyio,
                 bodyuses,
                 body')
        end
      | Index (name, e, t, pos) =>
        let val (io, uses, e') = removeDeadBindingsInExp e
        in (io,
            name :: uses,
            Index (name, e', t, pos))
        end
      | Iota (e, pos) =>
        let val (io, uses, e') = removeDeadBindingsInExp e
        in (io,
            uses,
            Iota (e', pos))
        end
      | Map (farg, e, t1, t2, pos) =>
        let val (eio, euses, e') = removeDeadBindingsInExp e
            val (fio, fuses, farg') = removeDeadBindingsInFunArg farg
        in (eio orelse fio,
            euses @ fuses,
            Map (farg', e', t1, t2, pos))
        end
      | Reduce (farg, e1, e2, t, pos) =>
        let val (io1, uses1, e1') = removeDeadBindingsInExp e1
            val (io2, uses2, e2') = removeDeadBindingsInExp e2
            val (fio, fuses, farg') = removeDeadBindingsInFunArg farg
        in (io1 orelse io2 orelse fio,
            uses1 @ uses2 @ fuses,
            Reduce (farg', e1', e2', t, pos))
        end
      | Times (x, y, pos) =>
        let val (xios, xuses, x') = removeDeadBindingsInExp x
            val (yios, yuses, y') = removeDeadBindingsInExp y
        in (xios orelse yios,
            xuses @ yuses,
            Times (x', y', pos))
        end
      | Divide (x, y, pos) =>
        let val (xios, xuses, x') = removeDeadBindingsInExp x
            val (yios, yuses, y') = removeDeadBindingsInExp y
        in (xios orelse yios,
            xuses @ yuses,
            Divide (x', y', pos))
        end
      | And (x, y, pos) =>
        let val (xios, xuses, x') = removeDeadBindingsInExp x
            val (yios, yuses, y') = removeDeadBindingsInExp y
        in (xios orelse yios,
            xuses @ yuses,
            And (x', y', pos))
        end
      | Or (x, y, pos) =>
        let val (xios, xuses, x') = removeDeadBindingsInExp x
            val (yios, yuses, y') = removeDeadBindingsInExp y
        in (xios orelse yios,
            xuses @ yuses,
            Or (x', y', pos))
        end
      | Not (e, pos) =>
        let val (ios, uses, e') = removeDeadBindingsInExp e
        in (ios, uses, Not (e', pos)) end
      | Negate (e, pos) =>
        let val (ios, uses, e') = removeDeadBindingsInExp e
        in (ios, uses, Negate (e', pos)) end
      | Read x =>
        (true, [], Read x)
      | Write (e, t, pos) =>
        let val (_, uses, e') = removeDeadBindingsInExp e
        in (true, uses, Write (e', t, pos))
        end
and removeDeadBindingsInFunArg (FunName fname) =
    (false, [], FunName fname)
  | removeDeadBindingsInFunArg (Lambda (rettype, params, body, pos)) =
    let val (io, uses, body') = removeDeadBindingsInExp body
        (* Remove those uses that are really the parameter names. *)
        fun isParam x =
            List.exists (fn (Param (pname, _)) => pname = x) params
        val uses' = List.filter (not o isParam) uses
    in (io,
        uses',
        Lambda (rettype, params, body', pos))
    end

fun removeDeadBindingsInFunDec (FunDec (fname, rettype, params, body, pos)) =
    let val (_, _, body') = removeDeadBindingsInExp body
    in FunDec (fname, rettype, params, body', pos)
    end

fun removeDeadBindings prog =
    map removeDeadBindingsInFunDec prog

end
