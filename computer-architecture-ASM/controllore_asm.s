# FUNZIONE controllore_asm
# filename: controllore_asm.s
#
# INPUT:
# 1) ESI contenente l'indirizzo a una stringa costruita in questo modo:
#		INIT,RESET,PH
# OUTPUT:
# 1) EDI contenete l'indirizzo a una stringa costruita in questo modo:
# 		ST,NCK,VLV
#
#
#			INIT   ,   RESET   ,   PH2   PH1   PH0	 \n
#		ESI:      0    1     2     3    4     5     6     7 
#
#
#			  ST   ,   NCK1   NCK0   ,   VLV1   VLV0   \n
#		EDI:      0    1     2     3     4    5      6      7 
#

.section .text
	.global controllore_asm
	
	.type controllore_asm, @function

controllore_asm:
	
	
	# '-' = 45 (ASCII)
	# Di default pongo a "--" l'uscita VLV1 VLV0 
	movb $45, 5(%edi)
	movb $45, 6(%edi)
	
	# '0' = 48 (ASCII)
	cmpb $48, (%esi) 	# Confronto '0' con il primo valore della stringa, 
	je reset		# se il bit di INIT è 0 salto a reset_clock
				# altrimenti continuo...
	
	# '1' = 49 (ASCII)
	cmpb $49, 2(%esi) 	# Confronto '1' con il secondo valore della stringa
	je reset		# se il bit di RESET è 1 salto a reset_clock
				# altrimenti continuo...
	
	# Solo se INIT = 1 e RESET = 0 arrivo qui
	
	
	# Converto il PH da stringa a numero
	call ph2num_asm 		
	# Ora ho PH su EAX ! 
	
	
	
	cmpb $60, %al		# Se EAX contiene un valore minore di
	jl stampa_str_acido	# 60 allora il PH è acido, quindi salto a
				# stampa_str_acido.
	
	cmpb $80, %al		# Se EAX contiene un valore maggiore di
	jg stampa_str_basico	# 80 allora il PH è basico, quindi salto a 
				# stampa_str_basico.
	
	
	# Altrimenti vuol dire che 60 < EAX < 80
	# quindi il PH è neutro.
	
	
	
	#############
	# PH NEUTRO #
	#############
stampa_str_neutro:
	# 'N' = 78 (ASCII)
	movb $78, (%edi) 	# Carattere 'N' nel primo valore della stringaout.
	
	cmpb $78, -8(%edi)	# Se nel controllo della stringa precedente (ovvero in 
	jne reset_clock		# posizione -8(%EDI) ) NON trovo il carattere 'N'
				# vuol dire che ho fatto un cambio di stato, ciò
				# significa che devo resettare il clock e quindi salto 
				# a reset_clock_neutro.
	
	# altrimenti...
	incl %ebx		# Incremento EBX, perchè significa che mi sono 
				# mantenuto nello stesso stato.
	jmp scrivi_clock
	
	
	
	#############
	# PH BASICO #
	#############
stampa_str_basico:
	# 'B' = 66 (ASCII)
	movb $66, (%edi) 	# Carattere 'B' nel primo valore della stringaout.
	
	cmpb $66, -8(%edi)	# Se nel controllo della stringa precedente (ovvero in 
	jne reset_clock		# posizione -8(%EDI) ) NON trovo il carattere 'B'
				# vuol dire che ho fatto un cambio di stato, ciò
				# significa che devo resettare il clock e quindi salto 
				# a reset_clock_basico.
	
	# altrimenti...
	incl %ebx		# Incremento EBX, perchè significa che mi sono 
				# mantenuto nello stesso stato.
	
continua_basico:
	cmpl $5, %ebx		# Verifico lo stato del clock.
	jl scrivi_clock 	# Se il clock è minore di 5 salto sull'etichetta scrivi_clock,
				# altrimenti devo aprire la vavola AS
			
	# 'A' = 65; 'S' = 83; (ASCII)
	movb $65, 5(%edi)	# VLV1 = 'A'
	movb $83, 6(%edi)	# VLV0 = 'S'
	
	jmp scrivi_clock
	
	
	
	############
	# PH ACIDO #
	############	
stampa_str_acido:
	movb $65, (%edi) 	# Carattere 'A' nel primo valore della stringaout
	
	cmpb $65, -8(%edi)	# Se nel controllo della stringa precedente (ovvero in 
	jne reset_clock		# posizione -8(%EDI) ) NON trovo il carattere 'A'
				# vuol dire che ho fatto un cambio di stato, ciò
				# significa che devo resettare il clock e quindi salto 
				# a reset_clock_acido.

	# altrimenti...
	incl %ebx		# Incremento EBX, perchè significa che mi sono 
				# mantenuto nello stesso stato.
	
continua_acido:
	cmpl $5, %ebx		# Verifico lo stato del clock.
	jl scrivi_clock 	# Se il clock è minore di 5 salto sull'etichetta scrivi_clock
				# altrimenti devo aprire la valvola BS
	
	# 'B' = 66; 'S' = 83; (ASCII)		
	movb $66, 5(%edi)	# VLV1 = 'B'
	movb $83, 6(%edi)	# VLV0 = 'S'
	
	jmp scrivi_clock
	
	
reset_clock:
	xorl %ebx, %ebx		# Reset clock.	

scrivi_clock:
	# Scrivo in caratteri su NCK1 e NCK0 il valore del clock.
	call duecifre2str_asm
	
	# Salto alla fine
	jmp END


reset:
	xorl %ebx, %ebx		# Reset clock.
	
	# Se devo fare il reset devo impostare tutte le uscite a '-'
	# VLV1 e VLV0 le avevo già impostate!
	# '-' = 45 (ASCII)
	movb $45, 0(%edi)	# ST = '-' 
	movb $45, 2(%edi)	# NCK1 = '-'
	movb $45, 3(%edi)	# NCK0 = '-'
	

END:
	# Metto le ',' !!!
	# ',' = 44 (ASCII)
	movb $44, 1(%edi)
	movb $44, 4(%edi)
	
	# Alla fine della stringa metto \n
	movb $10, 7(%edi)

ret
