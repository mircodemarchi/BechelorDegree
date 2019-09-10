# FUNZIONE duecifre2str_asm
# filename: duecifre2str_asm.s
#
# Converte il clock, conenuto in EBX in una stringa da 2 caratteri.
#
# Questa funzione è stata fatta con la limitazione a 2 cifre e non con una qualsiasi
# cifra in input per ottimizzare la funzione "controllore" per la conversione del 
# clock (contenuto in EBX) in una stringa.
# Infatti la parte di output NCK può contenere al massimo 2 cifre, di conseguenza 
# è inutile tradurre in stringa numeri a 3 cifre o più. 
#

.section .text
	.global duecifre2str_asm
	
	.type duecifre2str_asm, @function

duecifre2str_asm:
	pushl %ebx 		# Salvo EBX perchè poi lo devo modificare.
	
	cmpl $99, %ebx		# Se il numero è a 3 cifre scrivo su
	jg numero_3_cifre	# NCK questa stringa: "??"
	# altrimenti continuo...
	
	cmpl $10, %ebx		# Divido il programma in due parti:
	jl minore_di_10		#    - se la cifra è >= 10
				#    - se la cifra è < 10
	
maggiore_o_uguale_a_10:
	
	# Voglio fare AX/BL  
	# poi il quoziente lo troverò in AL
	# il resto in AH
	#
	# Es: 34 / 10
	# AL -> 3; AH -> 4;
	
	movl %ebx, %eax		# Sposto il clock al dividendo.
	movl $10, %ebx		# Metto la costante 10 su EBX (divisore).
	
	divb %bl		# AX/BL - AL è il quoziente, AH è il resto
	 
	# '0' = 48 (ASCII)
	addb $48, %al		# Porto il valore del quoziente in carattere.
	addb $48, %ah		# Porto il valore del resto in carattere.
	
	movb %al, 2(%edi)	# Sposto il carattere del quoziente in NCK1.
	movb %ah, 3(%edi)	# Sposto il carattere del resto in NCK0.
	
	jmp fine

minore_di_10:
	
	# Se la cifra è minore di 10 allora il 
	# numero è già pronto senza fare divisioni
	
	movb $48, 2(%edi)	# Sposto il carattere 0 in NCK1
	
	addb $48, %bl		# Porto il valore del clock in carattere
	movb %bl, 3(%edi)	# Sposto il carattere del clock in NCK0.
	
	jmp fine

numero_3_cifre:
	# '?' = 63 (ASCII)
	movb $63, 2(%edi)	# Carattere '?' su NCK1. 
	movb $63, 3(%edi)	# Carattere '?' su NCK0.

fine:
	popl %ebx		# Ripristino EBX.
	
ret

