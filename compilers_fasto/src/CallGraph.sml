structure CallGraph :> CallGraph = struct

type CallGraph = (string * string list) list

fun callsOf caller graph =
    case List.find (fn (x,_) => x = caller) graph of
        NONE           => []
      | SOME (_,calls) => calls

fun calls caller callee graph =
    List.exists (fn x => x = callee) (callsOf caller graph)

open Fasto.KnownTypes

(* Remove duplicate elements in a list.  Quite slow - O(n^2) - but our
lists here will be small. *)
fun nub [] = []
  | nub (x::xs) =
    if List.exists (fn y => x = y) xs then
        nub xs
    else x :: nub xs

(* Get direct function calls of a single expression *)
fun expCalls e =
    case e of
        Constant _ => []
      | StringLit _ => []
      | ArrayLit (es, _, _) => List.concat (map expCalls es)
      | Var _ => []
      | Plus (e1, e2, _) => expCalls e1 @ expCalls e2
      | Minus (e1, e2, _) => expCalls e1 @ expCalls e2
      | Equal (e1, e2, _) => expCalls e1 @ expCalls e2
      | Less (e1, e2, _) => expCalls e1 @ expCalls e2
      | If (e1, e2, e3, _) => expCalls e1 @ expCalls e2 @ expCalls e3
      | Apply (fname, es, _) => fname :: List.concat (map expCalls es)
      | Let (Dec (_, e, _), body, _) => expCalls e @ expCalls body
      | Index (_, e, _, _) => expCalls e
      | Iota (e, _) => expCalls e
      | Map (farg, e, _, _, _) => fargCalls farg @ expCalls e
      | Reduce (farg, e1, e2, _, _) => fargCalls farg @ expCalls e1 @ expCalls e2
      | Times (e1, e2, _) => expCalls e1 @ expCalls e2
      | Divide (e1, e2, _) => expCalls e1 @ expCalls e2
      | And (e1, e2, _) => expCalls e1 @ expCalls e2
      | Or (e1, e2, _) => expCalls e1 @ expCalls e2
      | Not (e, _) => expCalls e
      | Negate (e, _) => expCalls e
      | Read _ => []
      | Write (e, _, _) => expCalls e
and fargCalls (Lambda (_, _, body, _)) = expCalls body
  | fargCalls (FunName s)              = [s]

(* Get the direct function calls of a single function. *)
fun functionCalls (FunDec (fname, _, _, body, _)) =
    (fname, nub (expCalls body))

(* Expand the direct function call graph to its transitive closure. *)
fun transitiveClosure (graph : CallGraph) =
    (* We do this by fix-point iteration. *)
    let fun grow (caller : string,
                  callees : string list) =
            let val calleecalls =
                    List.concat (map (fn callee => callsOf callee graph) callees)
                (* Find those calls we are not already aware of. *)
                val newcalls =
                    List.filter (fn call =>
                                    not (List.exists
                                             (fn x => x = call)
                                             callees))
                                calleecalls
            in if null newcalls
               then ((caller, callees),
                     false)
               else ((caller, callees @ nub newcalls),
                     true)
            end
        val (graph', changes) = ListPair.unzip (map grow graph)
        val changed = List.exists (fn x => x) changes
    in if changed
       then transitiveClosure graph'
       else graph'
    end

fun callGraph prog =
    let val directCalls = map functionCalls prog
    in transitiveClosure directCalls
    end
end
