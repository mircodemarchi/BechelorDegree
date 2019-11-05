/// @file
/// @author De Marchi Mirco

/// @defgroup struct Definizione Strutture
/// @{
#ifndef TYPES
#define TYPES

/**
 * @brief Struttura Status contenuta nella Share Memory 1
 * @brief Contiene grandson (int), ovvero il codice del rispettivo nipote (1 o 2), oppure 
 *	  il codice della rispettiva thread, e id_string (int), ovvero la riga della 
 *        stringa analizzata dal nipote/thread dalla SHM1.
 */
typedef struct Status{
    int grandson;
    int id_string;
} Status;

/**
 * @brief Struttura Message usata dalla Coda di Messaggi gestita da logger.c
 * @brief Contiene mtype (long), ovvero il codice identificato del messaggio inviato o ricevuto,
 *        length (int), ovvero la lunghezza del messaggio inviato o ricevuto,
 *        e text (char *), ovvero il messaggio stesso, di lunghezza massima 128 char.
 */
typedef struct Message {
    long mtype;
    int length;
    char text[128];
} Message;

/**
 * @brief Struttura semun per la gestione dei semafori tramite semctl()
 * @brief Contiene val (int), usato per cambiare il valore del semaforo con il comando SETVAL, 
 *        buffer (struct semid_ds *), usato per ottenere o modificare informazioni sul semaforo con i 
 *        comandi IPC_STAT e IPC_SET, e array (unsigned short *), usato per ottenere o modificare il 
 *        valore dei semafori nel caso in cui siano più di 1 con i comandi GET_ALL e SET_ALL.
 */
typedef union semun{
    int val;
    struct semid_ds *buffer;
    unsigned short *array;
} semun_t;

/**
 * @brief Struttura thread_args per poter passare dei parametri in input alle threads
 * @brief Contiene id_threads, ovvero l'id della thread che è stata creata, usato per poter aggiornare il campo grandson della struttura Status così che il processo figlio può capire quale 
 * thread sta lavorando sulla stringa id_string
 */
typedef struct thread_args{
    int id_thread;
} thread_args;

#endif

/// @}
