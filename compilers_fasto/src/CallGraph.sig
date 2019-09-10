signature CallGraph =
sig
    (* A call graph maps a function name A to those
    functions that are called by A.  This also includes recursive and
    indirect calls, such that if A calls B and B calls C, A is also
    considered to call C. *)
    type CallGraph

    (* Check whether the first function calls the second in the call graph. *)
    val calls : string -> string -> CallGraph -> bool

    (* Compute the call graph for a program. *)
    val callGraph : Fasto.KnownTypes.Prog -> CallGraph
end
