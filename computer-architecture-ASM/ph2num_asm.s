# FUNZIONE ph2num_asm
# filename: ph2num_asm.s
#
# Converte 3 caratteri numerici in numero
#
# Questa funzione serve per ottimizzare la funzione controllore,
# per cui prevede in ingresso 3 caratteri di PH e che poi devono
# essere interpretati come un numero.
# Per questa operazione non serve realizzare una funzione che
# opera anche con stringhe formate da più di 3 caratteri, perchè
# il PH in input è dato esattamente da 3 caratteri.
#

.section .text
  	.global ph2num_asm
  
  	.type ph2num_asm, @function
  
ph2num_asm:

	# Pongo a 0 EAX, ECX e EDX
	xorl %eax, %eax	
	xorl %ecx, %ecx
	xorl %edx, %edx
	
	# 1° carattere PH2
	movb $10, %dl		# Carico 10 su EDX ovvero il secondo fattore
				# della successiva moltiplicazione.
	movb 4(%esi), %al	# Carico il carattere corrispondente a PH2 su AL.
	subb $48, %al		# Porto a numero PH2
				# '0' = 48 (ASCII)
	mull %edx		# Faccio EAX * EDX ( ovvero PH2*10 )
	
	# 2° carattere PH1
	movb $10, %dl		# Con la moltiplicazione di prima ho perso la
				# costante 10 salvata su EDX: la ricarico.
	movb 5(%esi), %cl	# Carico il carattere corrispondente a PH1 su CL.
				# Non posso caricarlo direttamente su AL come prima,
				# altrimenti perderei il valore di prima.
	subb $48, %cl		# Porto a numero PH1.
	addl %ecx, %eax		# (PH2 * 10) + PH1 !
	mull %edx		# Faccio EAX * EDX (ovvero ((PH2 * 10)+PH1)*10 )
	
	# 3° carattere PH0
	movb 6(%esi), %cl	# Carico su CL l'ultimo carattere (PH0)
	subb $48, %cl		# Porto PH0 a numero
	addl %ecx, %eax		# Faccio (((PH2 * 10)+PH1)*10) + PH0 !
	
	

fine:
  	# il risultato si trova in EAX


ret
