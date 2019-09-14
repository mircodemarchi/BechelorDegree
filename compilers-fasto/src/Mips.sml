(* Types and utilities for the abstract syntax of MIPS. *)

structure Mips = struct

type reg = string
type imm = string
type addr = string

datatype Instruction =
    LABEL   of addr     (* Angiver en label, man fx kan hoppe til *)
  | COMMENT of string   (* Placerer en kommentar i assemblerkoden *)

  | LA   of reg*addr             (* LA($rd,addr):    $rd = addr (label) *)
  | LUI  of reg*imm              (* LUI($rd,imm):    $rd = (imm << 16) *)
  | LW   of reg*reg*imm          (* LW($rd,$rs,imm): $rd = Mem[$rs + imm] *)
  | LB   of string*string*string (* LB($rd,$rs,imm): $rd = Mem[$rs + imm] *)
  | SW   of string*string*string (* SW($rw,$rm,imm): Mem[$rm + imm] = $rw *)
  | SB   of string*string*string (* SB($rb,$rm,imm): Mem[$rm + imm] = $rb *)

  (* Aritmetiske instruktioner *)
  | ADD  of reg*reg*reg (* ADD($rd,$rs,$rt): $rd = $rs + $rt. *)
  | ADDI of reg*reg*imm (* ADDI($rd,$rs,imm): $rd = $rs + imm *)
  | SUB  of reg*reg*reg (* SUB($rd,$rs,$rt): $rd = $rs - $rt. *)
  | SUBI of reg*reg*imm (* SUBI($rd,$rs,imm): $rd = $rs - imm *)
  | MUL  of reg*reg*reg (* MUL($rd,$rs,$rt): $rd = $rs * $rt, no overflow. *)
  | DIV  of reg*reg*reg (* DIV($rd,$rs,$rt): $rd = quotient($rd / $rs), no overflow. *)

  (* Bitvise operatorer *)
  | AND  of reg*reg*reg (* AND($rd,$rs,$rt):  $rd = $rs & $rt *)
  | ANDI of reg*reg*imm (* ANDI($rd,$rs,imm): $rd = $rs & imm *)
  | OR   of reg*reg*reg (* OR($rd,$rs,$rt):   $rd = $rs | $rt *)
  | ORI  of reg*reg*imm (* ORI($rd,$rs,imm):  $rd = $rs | imm *)
  | XOR  of reg*reg*reg (* XOR($rd,$rs,$rt):  $rd = $rs ^ $rt *)
  | XORI of reg*reg*imm (* XORI($rd,$rs,imm): $rd = $rs ^ imm *)

  (* Bit-shifting *)
  | SLL  of reg*reg*imm (* SLL($rd,$rs,imm):  $rd = $rs << imm *)
  | SRA  of reg*reg*imm (* SRA($rd,$rs,imm):  $rd = $rs >> imm *)

  (* Instruktioner til sammenligning *)
  | SLT  of reg*reg*reg    (* SLT($rd,$rs,$rt):  $rd = $rs < $rt *)
  | SLTI of reg*reg*imm    (* SLTI($rd,$rs,imm): $rd = $rs < imm *)
  | BEQ  of reg*reg*addr   (* BEQ($rs,$rt,addr): if ($rs == $rd) goto(addr) *)
  | BNE  of reg*reg*addr   (* BNE($rs,$rt,addr): if ($rs != $rd) goto(addr) *)
  | BGEZ of reg*addr       (* BGEZ($rs,addr): if ($rs == $0) goto(addr) *)
  | J    of addr           (* J(addr):        goto(addr)            *)
  | JR   of reg * reg list (* JR($rd,regs):   goto($rd)             *)
  | JAL  of addr* reg list (* JAL(addr,regs): $RA = $PC; goto(addr) *)
  | NOP
  | SYSCALL                (* Udfører det systemkald som er nævnt i $2 *)

  (* Angiver direktiverne .globl, .text, .data, .space, ascii, .asciiz, .align *)
  | GLOBL   of string
  | TEXT    of string
  | DATA    of string
  | SPACE   of string
  | ASCII   of string
  | ASCIIZ  of string
  | ALIGN   of string

(* Diverse pseudo-instruktioner *)
fun MOVE (rd,rs) = ORI (rd,rs,"0") (* MOVE($rd,$rs): $rd = $rs *)
fun LI (rd,imm) = ORI (rd,"0",imm) (* LI($rd,imm):   $rd = imm *)

type Prog = Instruction list

(* Pretty-print a list of MIPS instructions in the
   format accepted by the MARS MIPS simulator. *)
fun ppMipsProg instructions =
    String.concatWith "\n" (map ppMips instructions)

(* Pretty-print a single MIPS instruction for .asm output *)
and ppMips inst =
  case inst of
      LABEL l         => l ^ ":"
    | COMMENT s       => "# " ^ s

    | LA   (rt,v)  => "\tla\t"  ^ ppReg rt ^ ", " ^ v
    | LUI  (rt,v)  => "\tlui\t" ^ ppReg rt ^ ", " ^ v
    | LW (rd,rs,v) => "\tlw\t"  ^ ppReg rd ^ ", " ^ v ^ "(" ^ ppReg rs ^ ")"
    | LB (rd,rs,v) => "\tlb\t"  ^ ppReg rd ^ ", " ^ v ^ "(" ^ ppReg rs ^ ")"
    | SW (rd,rs,v) => "\tsw\t"  ^ ppReg rd ^ ", " ^ v ^ "(" ^ ppReg rs ^ ")"
    | SB (rd,rs,v) => "\tsb\t"  ^ ppReg rd ^ ", " ^ v ^ "(" ^ ppReg rs ^ ")"

    | ADD  (rd,rs,rt) => "\tadd\t"  ^ ppReg rd ^ ", " ^ ppReg rs ^ ", " ^ ppReg rt
    | ADDI (rd,rs,v)  => "\taddi\t" ^ ppReg rd ^ ", " ^ ppReg rs ^ ", " ^ v
    | SUB  (rd,rs,rt) => "\tsub\t"  ^ ppReg rd ^ ", " ^ ppReg rs ^ ", " ^ ppReg rt
    | SUBI (rd,rs,rt) => "\tsubi\t" ^ ppReg rd ^ ", " ^ ppReg rs ^ ", " ^ ppReg rt
    | MUL  (rd,rs,rt) => "\tmul\t"  ^ ppReg rd ^ ", " ^ ppReg rs ^ ", " ^ ppReg rt
    | DIV  (rd,rs,rt) => "\tdiv\t"  ^ ppReg rd ^ ", " ^ ppReg rs ^ ", " ^ ppReg rt

    | AND  (rd,rs,rt) => "\tand\t"  ^ ppReg rd ^ ", " ^ ppReg rs ^ ", " ^ ppReg rt
    | ANDI (rd,rs,v)  => "\tandi\t" ^ ppReg rd ^ ", " ^ ppReg rs ^ ", " ^ v
    | OR   (rd,rs,rt) => "\tor\t"   ^ ppReg rd ^ ", " ^ ppReg rs ^ ", " ^ ppReg rt
    | ORI  (rd,rs,v)  => "\tori\t"  ^ ppReg rd ^ ", " ^ ppReg rs ^ ", " ^ v
    | XOR  (rd,rs,rt) => "\txor\t"  ^ ppReg rd ^ ", " ^ ppReg rs ^ ", " ^ ppReg rt
    | XORI (rd,rs,v)  => "\txori\t" ^ ppReg rd ^ ", " ^ ppReg rs ^ ", " ^ v

    | SLL  (rd,rt,v)  => "\tsll\t"  ^ ppReg rd ^ ", " ^ ppReg rt ^ ", " ^ v
    | SRA  (rd,rt,v)  => "\tsra\t"  ^ ppReg rd ^ ", " ^ ppReg rt ^ ", " ^ v

    | SLT  (rd,rs,rt) => "\tslt\t"  ^ ppReg rd ^ ", " ^ ppReg rs ^ ", " ^ ppReg rt
    | SLTI (rd,rs,v)  => "\tslti\t" ^ ppReg rd ^ ", " ^ ppReg rs ^ ", " ^ v
    | BEQ  (rs,rt,v)  => "\tbeq\t"  ^ ppReg rs ^ ", " ^ ppReg rt ^ ", " ^ v
    | BNE  (rs,rt,v)  => "\tbne\t"  ^ ppReg rs ^ ", " ^ ppReg rt ^ ", " ^ v
    | BGEZ (rs,rt)    => "\tbgez\t" ^ ppReg rs ^ ", " ^ ppReg rt
    | J l             => "\tj\t" ^ l
    | JAL (l,argRegs) => "\tjal\t" ^ l
    | JR (r,resRegs)  => "\tjr\t" ^ ppReg r
    | NOP => "\tnop"
    | SYSCALL => "\tsyscall"

    | GLOBL s  => "\t.globl\t" ^ s
    | TEXT s   => "\t.text\t" ^ s
    | DATA s   => "\t.data\t" ^ s
    | SPACE s  => "\t.space\t" ^ s
    | ASCII s  => "\t.ascii\t\"" ^ String.toCString s ^"\""
    | ASCIIZ s => "\t.asciiz\t\"" ^ String.toCString s ^"\""
    | ALIGN s  => "\t.align\t" ^ s

and ppReg r =
  if numerical r then "$" ^ r else r

and numerical s =
  foldl (fn (c,i) => i andalso ord c >= ord #"0" andalso ord c <= ord #"9")
        true (explode s)

and intOfString s =
  foldl ( fn (c,i) => 10*i + ord c- ord #"0" ) 0 (explode s)

end
