(* The Fasto compiler main *)

structure FastoC = struct

(*

This is the main program for Fasto that will be turned into an executable.  It
ties together all the compiler modules.

You probably want to build the entire compiler by typing 'make' on Linux or
Mac, or by running 'make.bat' on Windows. Once the Fasto compiler has been
built, it can be run from the command-line.

*)

val usage =
    [ "   fasto -i tests/fib.fo"
    , "     Run 'fib.fo' in the 'tests' directory in interpreted mode."
    , "     and print the result."
    , ""
    , "   fasto -r tests/fib.fo"
    , "     Run 'fib.fo' in interpreted mode, but do not print the result."
    , ""
    , "   fasto -c tests/fib.fo"
    , "     Compile 'tests/fib.fo' into the MIPS program 'tests/fib.asm'."
    , ""
    , "   fasto -o [opts] tests/fib.fo"
    , "     Compile the optimised 'tests/fib.fo' into 'tests/fib.asm'."
    , ""
    , "   fasto -p [opts] tests/fib.fo"
    , "     Optimise 'tests/fib.fo' and print the result on standard output."
    , "     <opts> is a sequence of characters corresponding to optimisation"
    , "     passes, where: "
    , "       i                     - Inline functions."
    , "       c                     - Copy propagation and constant folding."
    , "       d                     - Remove dead bindings."
    , "       D                     - Remove dead functions."
    ]

exception FileProblem of string

exception SyntaxError of int * int

(* Save the content of a string to file *)
fun saveFile filename content =
    let val outfile = TextIO.openOut filename
    in TextIO.output (outfile, content);
       TextIO.closeOut outfile
    end handle Io _ => raise FileProblem filename

(* Convert a file stream into a lexer stream *)
fun createLexerStream (is : BasicIO.instream) =
    Lexing.createLexer ( fn buff => fn n => Nonstdio.buff_input is buff 0 n)

(* Print error message to the standard error channel *)
fun errorMessage message = TextIO.output (TextIO.stdErr, message ^ "\n");

fun errorMessage' (errorType, message, line, col) =
    errorMessage (errorType ^ ": " ^ message ^ " at line " ^
      makestring line ^ ", column " ^ makestring col)

(* Remove trailing .fo from filename. *)
fun sanitiseFilename argFilename =
    if String.isSuffix ".fo" argFilename
    then substring(argFilename, 0, size(argFilename)-3)
    else argFilename

(* only for debug functionality *)
fun viewLexedFile filename =
    let val lexbuf = createLexerStream (BasicIO.open_in (filename ^ ".fo"))
        fun loop() = case Lexer.Token lexbuf of
                       (Parser.EOF p) => (Parser.EOF p) :: []
                     | t          => t :: loop()
    in loop()
    end

fun parseFastoFile filename =
    let (* Create internal lexer state handed to parser *)
        val lexbuf = createLexerStream (BasicIO.open_in (filename ^ ".fo"))
          handle SysErr _ => raise FileProblem (filename ^ ".fo")
    in
      (* Construct syntax tree using parser and internal lexer state *)
      Parser.Prog Lexer.Token lexbuf
        handle Parsing.ParseError ob =>
          let val (line, col) = Lexer.getPos lexbuf
          in raise SyntaxError (line, col)
          end
      end

fun interpret filename =
    let val pgm = parseFastoFile filename
        val _ = print "Program is:\n\n"
        val _ = print (Fasto.UnknownTypes.ppProg pgm);
        val _ = print "\n+----------------------------------------+"
        val _ = print "\n| You might need to enter some input now |";
        val _ = print "\n+----------------------------------------+"
        val _ = print "\n"
        val _ = TextIO.flushOut TextIO.stdOut;

        val res = (Interpreter.evalProg pgm);
    in
       print "\n\nResult of 'main': ";
       print (Fasto.UnknownTypes.ppVal 0 res);
       print "\n"
    end

fun interpretSimple filename =
    let val pgm = parseFastoFile filename
    in Interpreter.evalProg pgm
    end

fun compile filename optimiser =
    let val pgm = parseFastoFile filename
        val pgm_decorated  = TypeChecker.checkProg pgm
        val pgm_optimised  = optimiser pgm_decorated
        val mips_code      = CodeGen.compile pgm_optimised
        val mips_code_text = Mips.ppMipsProg mips_code
    in saveFile (filename ^ ".asm") mips_code_text end

fun printOptimised argFilename optimiser =
    let val pgm = parseFastoFile argFilename
        val pgm_decorated  = TypeChecker.checkProg pgm
        val pgm_optimised  = optimiser pgm_decorated
    in print (Fasto.KnownTypes.ppProg pgm_optimised) end

fun bad () =
    ( errorMessage "Unknown command-line arguments. Usage:\n"
    ; app errorMessage usage)

(* Note that these functions are listed in opposite order of application. *)
val defaultOptimisations = DeadFunctionRemoval.removeDeadFunctions o
                           DeadBindingRemoval.removeDeadBindings o
                           CopyConstPropFold.optimiseProgram o
                           Inlining.optimiseProgram

fun extractOpts [] = SOME (fn x => x)
  | extractOpts (opt::opts) =
    let fun extractOpt #"i" = SOME Inlining.optimiseProgram
          | extractOpt #"c" = SOME CopyConstPropFold.optimiseProgram
          | extractOpt #"d" = SOME DeadBindingRemoval.removeDeadBindings
          | extractOpt #"D" = SOME DeadFunctionRemoval.removeDeadFunctions
          | extractOpt _    = NONE
   in case (extractOpt opt, extractOpts opts) of
        (SOME opt', SOME opts') => SOME (fn x => opts' (opt' x))
      | _                       => NONE
   end

val _ = (
  case Mosml.argv () of
    [_, "-i", file] => interpret (sanitiseFilename file)
  | [_, "-r", file] => (interpretSimple (sanitiseFilename file); print "\n")
  | [_, "-c", file] => compile (sanitiseFilename file) (fn x => x)
  | [_, "-o", file] => compile (sanitiseFilename file) defaultOptimisations
  | [_, "-o", opts, file] =>
    (case extractOpts (explode opts) of
         SOME opts' => compile (sanitiseFilename file) opts'
       | NONE       => bad ())
  | [_, "-p", file] =>
    printOptimised (sanitiseFilename file) defaultOptimisations
  | [_, "-p", opts, file] =>
    (case extractOpts (explode opts) of
       SOME opts' => printOptimised (sanitiseFilename file) opts'
      | NONE       => bad ())
         | _ => bad ()
  )
    handle
      Parsing.yyexit ob =>
        ( errorMessage "Parser exited unexpectedly."
        ; Process.exit Process.failure )

      | SyntaxError (line, col) =>
        ( errorMessage' ("Parse error", "", line, col)
        ; Process.exit Process.failure )

      | Lexer.Error (message, (line, col)) =>
        ( errorMessage' ("Lexical error", message, line, col)
        ; Process.exit Process.failure )

      | Interpreter.Error (message, (line, col)) =>
        ( errorMessage' ("Interpreter error", message, line, col)
        ; Process.exit Process.failure )

      | CodeGen.Error (message, (line, col)) =>
        ( errorMessage' ("Code generator error", message, line, col)
        ; Process.exit Process.failure )

      | TypeChecker.Error (message, (line, col)) =>
        ( errorMessage' ("Type error", message, line, col)
        ; Process.exit Process.failure )

      | FileProblem filename =>
        ( errorMessage ("There was a problem with the file: "^filename)
        ; Process.exit Process.failure )
end
