{
  (* A lexer definition for Fasto, for use with mosmllex. *)

  (* boilerplate code for all lexer files... *)

 open Lexing;

 exception Error of string * (int * int) (* (message, (line, column)) *)

 val currentLine = ref 1
 val lineStartPos = ref [0]

 fun resetPos () = (currentLine :=1; lineStartPos := [0])

 fun getPos lexbuf = getLineCol (getLexemeStart lexbuf)
        (!currentLine)
        (!lineStartPos)

 and getLineCol pos line (p1::ps) =
       if pos>=p1 then (line, pos-p1)
       else getLineCol pos (line-1) ps
   | getLineCol pos line [] = raise Error ("",(0,0))

 fun lexerError lexbuf s =
     raise Error (s, getPos lexbuf)

(* This one is language specific, yet very common. Alternative would
   be to encode every keyword as a regexp. This one is much easier. *)
 fun keyword (s, pos) =
     case s of
         "if"           => Parser.IF pos
       | "then"         => Parser.THEN pos
       | "else"         => Parser.ELSE pos
       | "let"          => Parser.LET pos
       | "in"           => Parser.IN pos
       | "int"          => Parser.INT pos
       | "bool"         => Parser.BOOL pos
       | "char"         => Parser.CHAR pos
       | "fun"          => Parser.FUN pos

(*============================ Start of my code ==============================*)
(* @author De Marchi Mirco
   @date   25-05-2019
   @brief  Lexeme for not (boolean negation).                                 *)
       | "not"          => Parser.NOT pos

       | "iota"         => Parser.IOTA pos
       | "map"          => Parser.MAP pos
       | "reduce"       => Parser.REDUCE pos

       | "fn"       => Parser.FN pos
(*============================================================================*)

(* specials: *)
       | "read"         => Parser.READ pos
       | "write"        => Parser.WRITE pos
       | _              => Parser.ID (s, pos)

 }

rule Token = parse
    [` ` `\t` `\r`]+    { Token lexbuf } (* whitespace *)
  | "//" [^`\n`]*       { Token lexbuf } (* comment *)
  | [`\n` `\012`]       { currentLine := !currentLine+1;
                          lineStartPos :=  getLexemeStart lexbuf
                           :: !lineStartPos;
                          Token lexbuf } (* newlines *)
  | [`0`-`9`]+          { case Int.fromString (getLexeme lexbuf) of
                               NONE   => lexerError lexbuf "Bad integer"
                             | SOME i => Parser.NUM (i, getPos lexbuf) }
(*============================ Start of my code ==============================*)
(* @author De Marchi Mirco
   @date   25-05-2019
   @brief  Lexeme for true and false.                                         *)
  | "true" | "false"    { case Bool.fromString (getLexeme lexbuf) of
                                 NONE   => lexerError lexbuf "Bad boolean"
                               | SOME b => Parser.BOOLEAN (b, getPos lexbuf) }
(*============================================================================*)
  | [`a`-`z` `A`-`Z`] [`a`-`z` `A`-`Z` `0`-`9` `_`]*
                        { keyword (getLexeme lexbuf,getPos lexbuf) }
  | `'` ([` ` `!` `#`-`&` `(`-`[` `]`-`~`] | `\`[` `-`~`]) `'`

                        { Parser.CHARLIT
        ((case String.fromCString (getLexeme lexbuf) of
             NONE => lexerError lexbuf "Bad char constant"
           | SOME s => String.sub(s,1)),
           getPos lexbuf) }
  | `"` ([` ` `!` `#`-`&` `(`-`[` `]`-`~`] | `\`[` `-`~`])* `"`
                        { Parser.STRINGLIT
			    ((case String.fromCString (getLexeme lexbuf) of
			       NONE => lexerError lexbuf "Bad string constant"
			     | SOME s => String.substring(s,1,
							  String.size s - 2)),
			     getPos lexbuf) }
  | `+`                 { Parser.PLUS   (getPos lexbuf) }
  | `-`                 { Parser.MINUS  (getPos lexbuf) }

(*============================ Start of my code ==============================*)
(* @author De Marchi Mirco
   @date   25-05-2019
   @brief  Lexeme for multiplication, division, and, or
           and integer negation.                                              *)
  | `*`                 { Parser.TIMES  (getPos lexbuf) }
  | `/`                 { Parser.DIVIDE (getPos lexbuf) }
  | "&&"                { Parser.AND    (getPos lexbuf) }
  | "||"                { Parser.OR     (getPos lexbuf) }
  | `~`                 { Parser.NEGATE (getPos lexbuf) }

  | "=>"                { Parser.ARROW  (getPos lexbuf) }
(*============================================================================*)
  | "=="                { Parser.DEQ    (getPos lexbuf) }
  | `=`                 { Parser.EQ     (getPos lexbuf) }
  | `<`                 { Parser.LTH    (getPos lexbuf) }
  | `(`                 { Parser.LPAR   (getPos lexbuf) }
  | `)`                 { Parser.RPAR   (getPos lexbuf) }
  | `[`                 { Parser.LBRACKET (getPos lexbuf) }
  | `]`                 { Parser.RBRACKET (getPos lexbuf) }
  | `{`                 { Parser.LCURLY (getPos lexbuf) }
  | `}`                 { Parser.RCURLY (getPos lexbuf) }
  | `,`                 { Parser.COMMA (getPos lexbuf) }
  | eof                 { Parser.EOF (getPos lexbuf) }
  | _                   { lexerError lexbuf "Illegal symbol in input" };
