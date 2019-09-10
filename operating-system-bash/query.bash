#!/bin/bash
# student: De Marchi Mirco VR408481
# group: Filippo Guadagnini - Andrea Veneri - Mirco De Marchi
# file-name: query.bash

##############################################################################
# Questo script deve essere eseguito in foreground perchè ha bisogno dello   #
# standard input per poter leggere da riga di comando il nome di un utente.  #   
##############################################################################

# $directoryCSV: contiene la directory in cui sono presenti i file csv.
directoryCSV=.

# Controllo se la directory $directoryCSV esiste
# altrimenti stampo un messaggio di errore e termino il processo
if [ -d $directoryCSV ]; then
	cd $directoryCSV
else
	echo "Nessuna directory $directoryCSV."
	exit
fi

# Verifico che esista almeno un file .csv
# Il pattern riportato sul comando egrep si riferisce a una stringa
# del tipo "report_nnnnnnnn_nnnnnn.csv", dove 'n' è un qualsiasi
# numero tra 0 e 9.
if [ -z "$(ls -l | egrep report_[0-9]{8}_[0-9]{6}.csv)" ]; then
	echo "Nessun report.csv presente nella directory corrente."
	exit
fi

# 1
# Per ogni utente X, stampare il numero dei processi in esecuzione
# appartenenti a X riportati nel csv file generato più recentemente.

# Per trovare l'ultimo file csv generato uso il comando 'ls -l' che
# di default ordina i file della directory corrente in ordine alfabetico.
# Visto che i file csv vengono generati con una data tale da preservare 
# un ordine cronologico di generazione dei file csv, per prendere il più 
# recente, basterà prendere l'ultimo della lista con il comando 'tail -n -1'.
# Il comando 'tr -s' serve per ridurre a una sola ripetizione ogni occorrenza 
# della stringa indicata, 'cut' invece serve per selezionare solo il nome
# del file.

lastCSV=$(ls -l | tail -n -1 | tr -s " " | cut -d" " -f9)
echo "Last report: $lastCSV"

# Il comando di base di questa parte di codice è:
# 	cut -d';' -f1 $lastCSV | sort | uniq
# il comando 'cut' prende la prima colonna dell'ultimo file csv generato,
# ovvero la colonna con tutti i nomi utente, poi il comando 'sort' ordina
# questa lista di nomi e in fine 'uniq' sopprire le duplicazioni contigue
# di righe.
#  Variabili:
# $LIMIT: contiene il numero di utenti, contando le righe dall'output del 
#         comando di base, usando 'wc -l'.
# $userPerLine: contiene mediante un ciclo for l'utente della riga i-esima;
# 	            la selezione di ogni utente viene fatta con i comandi 
#               'head -n $i', che seleziona tutti gli utenti fino alla riga
#               i-esima e in pipe 'tail -n -1' che seleziona solo l'ultima
# 	            riga.
# 'grep -c $userPerLine' ritorna il numero di occorrenze del nome contenuto
# nella variabile $userPerLine.

LIMIT=$(cut -d';' -f1 $lastCSV | sort | uniq | wc -l)
for (( i=1; i<=LIMIT; ++i )); do
	userPerLine=$(cut -d';' -f1 $lastCSV | sort | uniq | head -n $i | tail -n -1)
	echo "$userPerLine: $(cut -d';' -f1 $lastCSV | grep -c $userPerLine)"
done

# 2
# Per un utente user fornito da riga di comando, stampare il numero di processi
# in esecuzione appartenenti a user riportati in ogni file csv generato.

# $user viene letto tramite una 'read' da linea di comando, nel caso in cui
# non venga inserito nulla allora la read viene rifatta.
# I file csv vengono selezionati tramite un ciclo for che stampa per ognuno
# di questi il nome del file csv e a seguito il numero delle occorrenze 
# dell'user inserito precedentemente. 
# Il numero delle occorrenze viene calcolato tramite il comando 'grep -c', che
# con l'opzione -w permette di prendere solo le occorrenze della stringa inserita 
# senza caratteri alfabetici vicini, quindi che non sia una sottostringa.
while [ -z "$user" ]; do
	echo -ne "\nInserire utente: "
	read user
done

for file in *.csv; do
	numberOfUser=$(cut $file -d";" -f1 | grep -cw $user)
	echo "report: $file processi $numberOfUser"
done
