/**
 * @file
 * @author De Marchi Mirco
 *
 * Il processo Padre esegue in sequenza le seguenti operazioni:
 *
 * 1) Viene creato un segmento di memoria condivisa (SHM1), identificato dalla chiave 11, dentro il quale viene 
 * inserito il contenuto del file di input
 *
 * 2) Crea e fa l'attach di un ulteriore segmento di memoria condivisa (SHM2), identificato dalla chiave 12, che poi conterrà le chiavi
 * di decriptazione trovate dai sottoprocessi
 *
 * 3) Genera i sottoprocessi logger e figlio
 *
 * 4) Aspetta che i due sottoprocessi finiscano la loro esecuzione
 *
 * 5) Controlla se le chiavi trovate sono corrette
 *
 * 6) Copia le chiavi dal segmento di memoria condivisa SHM2 sul file di file di output
 *
 * 7) Rimuove SHM1 e SHM2
 *
 * 8) Termina
 */

/// @defgroup processo_padre Funzioni del processo padre
/// @{
#ifndef PADRE
#define PADRE

#include <sys/types.h>
#include <unistd.h>

/**
 * @brief Wrapper del processo Padre
 * @param file_input Stringa del path del file da leggere in input
 * @param file_output Stringa del path del file da scrivere in output
 */
void padre(char *file_input, char *file_output);

/**
 * @brief Carica i dati contenuti nel file di input sulla Share Memory 1
 * @brief Apre il file, calcola la grandezza totale del file in byte e crea il segmento di memoria condivisa SHM1,
 * identificata dalla chiave 11, grande quanto la struttura Status e la grandezza del file.
 * La struttura Status servirà al sottoprocesso logger per leggere l'indice della stringa che sta per essere analizzata
 * per ricercare la chiave e l'id del processo/thread che sta analizzando la stringa.
 * Dopodichè viene copiato il contenuto del file nella zona di memoria condivisa dedicata ad esso.
 * In fine viene calcolato il numero di stringhe all'interno del file e viene chiuso il file.
 * @param file Stringa del path del file da leggere
 */
void load_file(char *file);

/**
 * @brief Crea il semaforo SEM2 e setta il suo valore a 0
 * @brief Il semaforo sono identificati dalla chiave 32.
 * Il semaforo SEM2 serve per sincronizzare il processo logger con il processo figlio, ovvero per eseguire
 * il processo figlo solo dopo che il processo logger ha creato la coda di messaggi.
 * @return id del semaforo creato
 */
int set_sem2();

/**
 * @brief Esegue le fork() per i processi logger e figlio
 * @brief Prima esegue la fork() per il processo logger, poi fa una lock() sul semaforo SEM2 per aspettare
 * che venga creata la coda di messaggi e poi si esegue la fork() per il processo figlio.
 * @param semid id del semaforo da usare per sincronizzare l'esecuzione del processo figlio e del processo logger
 */
void create_process_figlio_logger(int semid);

/**
 * @brief Controlla che le chiavi siano corrette per tutta la lunghezza delle stringhe
 * @brief Per controllare che le chiavi siano corrette bisogna calcolare per ogni riga di ogni stringa la lunghezza del plaintext
 * e del encodedtext, fare la conversione della chiave da stringa esadecimale a intero unsigned, fare la conversione del plaintext
 * e encodedtext da stringa a array di unsigned int e poi per tutta la lunghezza degli array unsigned int encodedtext e plaintext,
 * fare l'or esclusivo tra la chiave e il plaintext e verificare che sia uguale ad encodedtext.
 * Bisogna fare la conversione della chiave da stringa esadecimale a intero unsigned, perchè la chiave viene salvata nella
 * SHM2 dal processo nipote come una stringa di caratteri in esadecimale.
 */
void check_keys();

/**
 * @brief Salva le chiavi sul file di output
 * @brief Viene aperto il file per poi fare la write() di ogni chaive salvata nella SHM2
 * ne seguente formato: "0xFFFFFFFF\n", dove al posto di FFFFFFFF c'è il codice della chiave
 * in esaecimale.
 * In fine viene chiuso il file.
 * @param file_output Stringa del file da usare in output per la scrittura delle chiavi
 */
void save_keys(char *file);

/**
 * @brief Crea un segmento di memoria condivisa e fa l'attach sul processo corrente
 * @param key Chiave della Share Mamory
 * @param size Grandezza della memoria condivisa da creare
 * @param shmid Indirizzo all'identificatore della memoria condivisa
 * @return Indirizzo alla memoria condivisa
 */
void *attach_segments(key_t key, size_t size, int *shmid);

/**
 * @brief Fa il detach del segmento di memoria condivisa dal processo corrente e elimina la Share Memory
 * @param addr_shm Indirizzo al segmento di memoria per il detach
 * @param shmid Id della memoria condivisa
 */
void detach_segments(void *addr_shm, int shmid);

/**
 * @brief Conta le righe di una stringa in memoria
 * @brief Per contare le righe di una locazione di memoria viene preso come carattere di riferimento '\n'.
 * Per cui viene contato il numero di ricorrenze del carattere '\n' all'interno di un segmento di memoria,
 * puntato da un indirizzo di memoria e di una grandezza definita.
 * @param addr_from_file Indirizzo alla stringa da leggere
 * @param size Lunghezza della stringa
 * @return Numero di righe
 */
int count_file_row(char *addr_from_file, int size);

/**
 * @brief Traduce una stringa di caratteri esadecimali in un intero unsigned
 * @brief Questa funzione è stata implementata per tradurre la key presa dalla SHM2 in un unsigned int,
 * dunque la funzione considera che la stringa hex sia lunga esattamente 8 caratteri, così come la chiave
 * in formato esadecimale.
 * Per fare la traduzione la stringa viene analizzata da destra a sinistra. Ogni simbolo esadecimale,
 * viene direttamente tradotto nel numero in base 10, moltiplicato per 16^i dove i è l'indice
 * del simbolo e poi sommato con tutti gli altri.
 * @param hex Indirizzo alla stringa di caratteri esadecimali
 * @param uint Indirizzo al unsigned int in cui salvare il risultato
 */
void from_hex_to_uint(char *hex, unsigned int *uint);

#endif

/// @}
