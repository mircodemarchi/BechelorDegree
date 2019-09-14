structure DeadFunctionRemoval =
struct
open Fasto
open Fasto.KnownTypes

fun removeDeadFunctions prog =
    let val graph = CallGraph.callGraph prog
        fun alive (FunDec (fname, _, _, _, _)) =
            fname = "main" orelse
            CallGraph.calls "main" fname graph
    in List.filter alive prog
    end
end
