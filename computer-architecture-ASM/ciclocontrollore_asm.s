# FUNZIONE ciclocontrollore_asm
# filename: ciclocontrollore_asm.s
#
# Questa funzione si occupa di ciclare la funzione "controllore" fino a quando finisce
# la stringa bufferin. 
# Aggiorna di volta in volta i registri ESI e EDI che contengono 
# l'indirizzo della stringa rispettivamente da leggere e da modificare, in modo tale che
# la funzione "controllore" possa agire trovando sempre su ESI una stringa del tipo 
# INIT,RESET,PH e su EDI una stringa del tipo ST,NCK,VLV.
#

.section .text
	.global ciclocontrollore_asm
	
	.type ciclocontrollore_asm, @function

ciclocontrollore_asm:

	pushl %ebp		# Salvo sullo stack EBP così da 
				# poter essere modificato.
	movl %esp, %ebp	  	# Aggiorno EBP con il valore di ESP.
	
	# Ora lo stack si presenta così:
	#
	#        EBP ->	|          |
	#		------------
	#	4+EBP	|    EBP   |
	#               ------------
	#	8+EBP	| bufferin |
	#		------------
	#	12+EBP	| bufferout|
	#		------------
	#		|    ...   |
	#
	
	movl 8(%ebp), %esi	# Metto l'indirizzo di bufferin su ESI
    	movl 12(%ebp), %edi	# Metto l'indirizzo di bufferout su EDI
    	
    	xorl %ebx, %ebx		# EBX sarà il clock e parte da 0.
    	
ripeti: 
	# '\0' = 0 (ASCII)
	cmpb $0, (%esi)		# Verifico se sono arrivato a fine stringa.
    	je fine			# Se trovo '\0' salto su fine,
    				# altrimenti continuo...
    	
    	call controllore_asm
    	
    	addl $8, %esi		# Incremento ESI in modo da accedere alla stringa successiva
    	addl $8, %edi		# Incremento EDI in modo da accedere alla stringa successiva
    				# Infatti le stringhe INIT,RESET,PH e ST,NCK,VLV sono formate
    				# entrambe da 8 caratteri.
    	
    	jmp ripeti		# Ripeto il controllo fino a fine strinfa
    	
    	
fine:
	movl $0, (%edi)		# Inserisco il carattere nullo alla fine della stringa di output.
    	popl %ebp		# Ricarico EBP.
    	
ret
