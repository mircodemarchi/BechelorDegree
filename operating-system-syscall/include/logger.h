/// @file
/// @author De Marchi Mirco

/// @defgroup Funzioni del processo logger
/// @{
#ifndef LOGGER
#define LOGGER

/**
 * @brief Wrapper del processo logger
 * @brief Il processo logger esegue in sequenza le seguenti operazioni:
 * 1) Richiama la get() per il semaforo SEM2 per poter bloccare il processo padre e creare il processo figlio
 * 2) Crea la coda di messaggi MSG1, identificata dalla chiave 21
 * 3) Fa l'unlock() del semaforo SEM2
 * 4) Inizia l'analisi della coda di messaggi (polling receive) fino a quando riceverà un messaggio di tipo 1
 * 5) Chiude la coda di messaggi MSG1
 * 6) Termina
 */
void logger();

/**
 * @brief Analizza la MSG1 con polling di un secondo
 * @brief Ogni 1 secondo controlla se c'è un messaggio all'interno della coda MSG1 e in caso affermativo
 * lo stampa sullo STDOUT.
 * Quando riceve un messaggio di tipo 1, ferma l'analisi della coda di messaggi, controlla se ci sono messaggi
 * rimanenti all'interno della coda e in caso ce ne siano li stampa sulla STDOUT.
 */
void polling_receive();

#endif

/// @}
