#!/bin/bash
# student: De Marchi Mirco VR408481
# group: Filippo Guadagnini - Andrea Veneri - Mirco De Marchi
# file-name: menu.bash

##############################################################################
# Questo script deve essere eseguito in foreground perchè ha bisogno dello   #
# standard input per poter leggere da riga di comando il nome di un utente.  #   
##############################################################################

RECORDER=./recorder.bash
QUERY=./query.bash
CHECKSPACE=./checkSpace.bash

# 	FUNZIONI
# Funzione per stampare il menu
stampa_menu(){
	echo "Monitoraggio server. Comandi disponibili:"
	echo "    1)  Inizia monitoraggio"
	echo "    2)  Ferma monitoraggio"
	echo "    3)  Stampa info utilizzo"
	echo "    4)  Chiudi"
	echo "    5)  Riporta spazio a soglia predefinita"
}

# Funzione per eseguire lo script recorder.bash
# Se esiste un processo recorder.bash già in esecuzione allora chiedo all'utente
# se vuole eseguirne un altro oppure no, rispondendo y(Y)/n(N).
# Altrimenti eseguo recorder.bash.
recorder(){
	if [ -n "$(ps ax | grep recorder.bash | head -n -1)" ]; then
		echo -n "recorder.bash già in esecuzione, vuoi eseguirne un altro? [Y/n] "
		read choice
	
		while [ "$choice" != "y" ] && [ "$choice" != "Y" ] && 
			[ "$choice" != "n" ] && [ "$choice" != "N" ]; do
			echo -n "Stringa non riconosciuta: [Y/n] "
			read choice
		done
			
		if [ "$choice" = "y" ] || [ "$choice" = "Y" ]; then
			bash $RECORDER &
		fi
	else
		bash $RECORDER &
	fi
}

# Funzione per eseguire lo script query.bash
query(){
	bash $QUERY
}

# Funzione per eseguire lo script chackSpace.bash
checkSpace(){
	bash $CHECKSPACE
}

# Funzione per killare il processo recorder.bash
# Se il processo recorder.bash non è attualmente in esecuzione, allora
# non eseguo il comando 'kill' e stampo un messaggio di errore.
# Nel caso in cui ci siano più processi recorder.bash in esecuzione,
# allora assumo di terminare solo il meno recente facendo 'head -n 1'.
# Il primo comando 'head' serve invece per togliere dalla lista dei processi 
# in esecuzione i comando 'grep' richiamato dalla pipeline. 
# 'cut' invece serve per selezionare solo la prima colonna, ovvero quella
# che riporta i pid.
killRecorder(){
	pidToKill=$(ps ax -o "%p;%a" | grep recorder.bash | head -n -1 | head -n 1 | cut -f1 -d";")
	if [ -n "$pidToKill" ]; then
		kill -9 $pidToKill
	else 
		echo "Nessun processo $RECORDER da killare."
	fi;
}

# Funzione per uscire dal menu
# Se esiste un processo recorder.bash già in esecuzione allora chiedo all'utente
# se vuole davvero uscire oppure no, rispondendo y(Y)/n(N).
# Altrimenti esco.
esc(){
	if [ -n "$(ps ax | grep recorder.bash | head -n -1)" ]; then
		echo -n "recorder.bash è ancora in esecuzione, sicuro di voler uscire? [Y/n] "
		read choice
	
		while [ "$choice" != "y" ] && [ "$choice" != "Y" ] && 
			[ "$choice" != "n" ] && [ "$choice" != "N" ]; do
			echo -n "Stringa non riconosciuta: [Y/n] "
			read choice
		done
			
		if [ "$choice" = "y" ] || [ "$choice" = "Y" ]; then
			exit
		fi
	else
		exit
	fi
}

#	CODICE
# Stampo il menu richiamando la relativa funzione e chiedo all'utente quale 
# comando vuole eseguire scegliendo un numero tra 1 e 5. 
# Questa operazione viene ripetuta anche all'interno del successivo ciclo infinito,
# in questo modo ogni volta che viene scelto un comando, quando questo viene eseguito,
# poi si ha subito la possibilità di sceglierne un altro, fino a quando si vuole
# terminare il monitoraggio.
stampa_menu
echo -n "Inserisci numero comandi [1-5]: "
read comando

# Le varie alternative vengono sviluppate tramite uno switch inserito
# in un ciclo infinito, per cui, per ogni numero tra 1 e 5, viene
# richiamata la funzione relativa al comando scelto, tranne per il
# numero 4 che, con una exit, fa terminare il processo corrente.
# Nel caso in cui si inserisca un qualsiasi altro numero, carattere
# o stringa viene stampato un messaggio di errore e viene ristampato
# il menu.
while true; do
	
	case $comando in 
		1) recorder;;
		2) killRecorder;;
		3) query;;
		4) esc;;
		5) checkSpace;;
		*) echo "Comando non riconosciuto."; stampa_menu;;
	esac
	
	echo -n "Inserisci numero comandi [1-5]: "
	read comando

done
