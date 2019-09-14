(* A polymorphic symbol table. *)

(*

A symbol table is a data structure associating names (strings) with values. It
is useful for keeping track of bindings. Bindings can be shadowed --- the
active binding is the one made most recently.

*)

signature SymTab = sig

    (* A symbol table with values of type 'a. *)
    type 'a SymTab

    (* Create an empty symbol table. *)
    val empty : unit -> 'a SymTab

    (* Look up the active binding for the name. *)
    val lookup : string -> 'a SymTab -> 'a option

    (* Bind the name to a value, shadowing any existing
       binidngs with the same name. *)
    val bind : string -> 'a -> 'a SymTab -> 'a SymTab

    (* Remove all existing bindings of the given name. *)
    val remove : string -> 'a SymTab -> 'a SymTab

    (* Remove all existing bindings of all the given names. *)
    val removeMany : string list -> 'a SymTab -> 'a SymTab

    (* Combine two symbol tables. The first table shadows the second. *)
    val combine : 'a SymTab -> 'a SymTab -> 'a SymTab

    (* Create a symbol table from a list of name-value pairs.
       
       In case of duplicates, the bindings are shadowed in reverse order from
       the head of the list. That is, the active binding will be the one
       closest to the head of the list. *)
    val fromList : (string * 'a) list -> 'a SymTab

end
