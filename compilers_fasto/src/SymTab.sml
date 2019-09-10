(* A polymorphic symbol table. *)

(* See SymTab.sig for an overview. *)

structure SymTab :> SymTab = struct

(*

A symbol table is just a list of tuples identifiers and values. This allows for
easy shadowing, as a shadowing binding can be quickly placed at the head of the
list.

*)

type 'a SymTab = (string * 'a) list

fun empty () = []

fun lookup n [] = NONE
  | lookup n ((n1,i1)::tab) =
      if n = n1 then SOME i1 else lookup n tab

fun bind n i stab = (n,i)::stab

fun remove n =
    List.filter (fn (x, _) => x <> n)

fun removeMany ns =
    List.filter (fn (x, _) =>
                    not (List.exists (fn y => y = x) ns))

fun combine t1 t2 = t1 @ t2

fun fromList l = l

end
