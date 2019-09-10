#!/bin/bash
# student: De Marchi Mirco VR408481
# group: Filippo Guadagnini - Andrea Veneri - Mirco De Marchi
# file-name: recorder.bash

###############################################################################
# Ciclo infinito a condizione sempre true:                                    #
# bisogna terminare con il comando                                            #
# 	kill -9 <pid>                                                             #
# Per trovare il <pid> del processo recorder.bash                             #
# utilizzare il seguente comando:                                             #
#	ps ax -o "%p;%a" | grep recorder.bash | head -n -1 | cut -f1 -d";"        #
#                                                                             #
# È consigliato eseguire questo script in background, altrimenti              #
# l'esecuzione del processo recorder.bash in foreground, bloccherà            #
# la shell e potrà essere sbloccata solo con ^Z (per stopparlo),              #
# oppure con ^C (per terminarlo).                                             #   
###############################################################################

# $directoryCSV: contiene la directory in cui sono presenti i file csv.
directoryCSV=.

# Verifico che la directory inserita nella variabile $directoryCSV 
# sia esistente e mi sposto dentro tale directory, altrimenti prima
# creo la directory.
if [ -d $directoryCSV ]; then
	cd $directoryCSV
else
	mkdir $directoryCSV
	cd $directoryCSV
fi

while true; do

	# Formato data (yyyymmdd) e ora (hhmmss)
	date=$(date +%Y%m%d)
	hour=$(date +%H%M%S)
	
	# Il comando 'ps' mostra i processi attualmente in esecuzione.
	# Con l'opzione 'ax' lista tutti i processi, l'opzione 'h' toglie 
	# l'header e '-o' serve per specificare un formato.
	# Creo e scrivo sul file report_yyyymmdd_hhmmss.csv i processi
	# attivi nell'istante corrente, nel seguente formato:
	# User ; pid ; command ; start time ; cpu time \n
	# Il comando sed toglie ogni spazio, ovvero sostituisce 
	# ogni stringa " " con la stringa "". Il flag 'g' sta per global 
	# e serve per fare la sostituzione per ogni occorrenza su tutta la riga, 
	# altrimenti si fermerebbe solo alla prima occorrenza.
	# Il comando head serve per togliere dalla lista dei processi in esecuzione
	# i comandi richiamati dalla pipeline, ovvero ps, sad e head. 
	ps ahx -o "%U;%p;%c;" -o start -o ";%x" | sed 's/ //g' | head -n -3 > report_"$date"_"$hour".csv

	sleep 60
done
