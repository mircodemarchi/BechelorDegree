#!/bin/bash
# student: De Marchi Mirco VR408481
# group: Filippo Guadagnini - Andrea Veneri - Mirco De Marchi
# file-name: checkSpace.bash

##############################################################################
# Questo script può essere eseguito sia in foreground che in background      #
##############################################################################

# $directoryCSV: contiene la directory in cui sono presenti i file csv.
# $sogliaKiloByte: contiene la soglia massima dello spazio utilizzato dai file csv.
directoryCSV=.
sogliaByte=8000

# Controllo se la directory $directoryCSV esiste
# altrimenti stampo un messaggio di errore e termino il processo
if [ -d $directoryCSV ]; then
	cd $directoryCSV
else
	echo "Nessuna directory $directoryCSV."
	exit
fi

# $LIMIT: contiene il numero di file csv contenuti nella directoryCSV.
LIMIT=$(ls -l | egrep report_[0-9]{8}_[0-9]{6}.csv | cut -f1 | wc -l)

# Controllo se viene contato almeno un file csv all'interno della directoryCSV
# altrimenti stampo un messaggio di errore e termino il processo
if [ $LIMIT -eq 0 ]; then
	echo "Nessun report.csv presente nella directory corrente."
	exit
fi

# Per ogni file csv viene usato il comando 'du -ak' per mostrare lo spazio occupato 
# da ogni singolo file, dove l'opzione -a indica tutti i file (non solo directory), 
# -b mostra lo spazio occupato in byte.
# Viene poi usata la variabile $sumOfSpace per essere incrementata degli spazi occupati
# da ogni singolo file csv.
# Per selezionare solo lo spazio occupato del file i-esimo viene usato il comando 'cut'
# sul primo campo e poi in pipe i comandi 'head -n $i | tail -n -1' per isolare il file
# i-esimo. 
for (( i=1; i<=LIMIT; ++i )); do
	sumOfSpace=$(( $sumOfSpace + $(du -ab | egrep report_[0-9]{8}_[0-9]{6}.csv | cut -f1 | head -n $i | tail -n -1) ))
done

# Il ciclo non termina fintantochè lo spazio occupato dei file csv è maggiore della soglia
# impostata.
# Utilizzando il comando 'ls -l' è possibile ottenere in ordine alfabetico la lista dei file
# contenuti nella directoryCSV.
# Visto che i file csv vengono generati con una data tale da preservare 
# un ordine cronologico di generazione dei file csv, per prendere il meno 
# recente, basterà prendere il primo della lista con il comando 'head -n 1'.
# Il comando 'tr -s' serve per ridurre a una sola ripetizione ogni occorrenza 
# della stringa indicata.
# In fine viene aggiornata la variabile $sumOfSpace, togliendo lo spazio occupato
# dal file che verrà poi subito dopo rimosso.
while [ $sumOfSpace -gt $sogliaByte ]; do
	fileToRemove=$(ls -l | egrep report_[0-9]{8}_[0-9]{6}.csv | head -n 1 | tr -s " " | cut -f9 -d" ")
	sumOfSpace=$(( $sumOfSpace - $(du -ab $fileToRemove | cut -f1) ))
	rm $fileToRemove
done
