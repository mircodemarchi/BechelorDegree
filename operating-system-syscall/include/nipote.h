#ifndef NIPOTE
#define NIPOTE

#include <sys/types.h>
#include <time.h>

/**
 * @brief Wrapper del processo nipote
 * @brief Il processo nipote esegue in sequenza le seguenti operazioni:
 * 1) Ricava tramite una get e una attach l'indirizzo della SHM1 e SHM2, identificate rispettivamente dalle chiavi 11 e 12
 * 2) Ricava tramite una get l'id del semaforo SEM1, identificato dalla chiave 31
 * 3) Entra in un ciclo che continua finchè ci sono stringhe da analizzare
 * 4) Fa la lock() sul semaforo SEM1 e accede alla sezione critica
 * 5) Legge l'indice della stringa che deve analizzare dalla struttura Status
 * 6) Scrive sulla struttura Status il proprio id nipote e incrementa l'indice alla stringa da analizzare
 * 7) Invia il segnale SIGUSR1 al processo figlio
 * 8) Tramite l'indice della stringa ricava l'indirizzo della SHM1 da cui parte la stringa che deve analizzare
 * 9) Trova gli indirizzi delle stringhe plaintext ed encodedtext e li traduce in unsigned int
 * 10) Trova la chiave memorizzando quanto tempo ci impiega
 * 11) Salva la chiave nella SHM2
 * 12) Accede alla coda di messaggi MSG1, identificata dalla chiave 21, per inviare un messaggio con i tempi di ritrovamento della chiave
 * 13) Se le stringhe da analizzare sono finite termina, altrimenti analizza la prossima stringa
 * @param x ID del processo nipote
 * @param nrow Numero di righe del file di input salvato sulla memoria condivisa SHM1
 */
void nipote(int x, int nrow);

/**
 * @brief Richiama un segmento di memoria condivisa SHM e fa l'attach
 * @brief Usa la chiave key per fare una get su un segmento di memoria già esistente. Poi fa l'attach e ricava
 * l'indirizzo della memoria condivisa ritornato dalla shmat().
 * @param key Chiave che identifica la Share Memory
 * @return Indirizzo della memoria condivisa ritornato dalla shmat()
 */
void *get_shm(key_t key);

/**
 * @brief Trova l'indirizzo esatto da cui partire per analizzare la stringa rispetto all'indice
 * @brief A partire dall'indirizzo addr_from_file vengono fatti scorrere i caratteri uno alla volta,
 * fino a quando si passa alla riga successiva.
 * La funzione passa alla riga successiva un numero di volte pari al valore di index_string.
 * Il carattere per identificare la terminazione della riga è '\n'.
 * @param addr_from_file Indirizzo da cui partire per trovare la stringa da analizzare
 * @param index_string Indice della stringa che deve essere analizzata
 * @return Indirizzo a memoria della stringa da analizzare
 */
char *load_string(char *addr_from_file, int index_string);

/**
 * @brief Trova la lunghezza della stringa rispetto il carattere '>'
 * @brief Viene fatta scorrere la stringa text carattere per carattere fino a quando non viene trovato il
 * carattere '>' così da trovarne la lunghezza.
 * Questa funzione è stata creata appositamente per trovare la lunghezza delle stringhe plaintext ed encodedtext,
 * che infatti terminano con il carattere '>'.
 * @param text Indirizzo alla stringa da cacolare la lunghezza
 * @return Lunghezza della stringa rispetto il carattere '>'
 */
int length_text(char *text);

/**
 * @brief Richiama la lock() del semaforo identificato da semid e da semnum
 * @brief semid è l'id identifidativo del semaforo ritornato dalla get, semnum invece è l'indice del semaforo nel
 * caso in cui si trattasse di un array di semafori.
 * @param semid ID del semaforo
 * @param semnum Indice dell'array di semafori
 */
void lock(int semid, int semnum);

/**
 * @brief Richiama la unlock() del semaforo identificato da semid e da semnum
 * @brief semid è l'id identifidativo del semaforo ritornato dalla get, semnum invece è l'indice del semaforo nel
 * caso in cui si trattasse di un array di semafori.
 * @param semid ID del semaforo
 * @param semnum Indice dell'array di semafori
 */
void unlock(int semid, int semnum);

/**
 * @brief Trova la chiave dati arrayplain, arrayencoded e la loro lunghezza
 * @brief Per trovare la chiave viene provata uno a uno ogni possibile soluzione, da 0 a 2^32 - 1.
 * Dunque per ogni chiave da provare vengono fatti scorrere gli array array_plain e array_encoded, e
 * se per tutta la lunghezza degli array il risultato dello xor tra array_plain e la chiave risulta uguale a
 * array_encoded, allora significa che la chiave è stata trovata, altrimenti si incrementa il valore della
 * chiave e si prova la prossima soluzione.
 * @param array_plain Indirizzo al array unsigned int arrayplain
 * @param array_encoded Indirizzo al array unsigned int arrayencoded
 * @param length Lunghezza di array_plain e array_encoded
 * @return Chiave trovata
 */
unsigned int find_key(unsigned int *array_plain, unsigned int *array_encoded, int length);

/**
 * @brief Invia un messaggio sulla MSG1 con il tempo impiegato a trovare la chiave
 * @brief Per trovare il tempo impiegato a trovare la chiave viene fatta la differenza tra il tempo end
 * e il tempo start, e poi viene tradotto questo valore (float) in stringa tramite la funzione time_to_string(),
 * per poter essere inviata sulla coda di messaggi MSG1.
 * La funzione time_to_string() traduce un long in una stringa quindi la parte frazionaria del tempo impiegato
 * per trovare la chiave viene scartata.
 * Dopodichè vengono concatenate la stringa text con la stringa ottenuta dalla funzione time_to_string() con la
 * stringa " seconds.\n", sulla struct Message per poter in fine fare la send del messaggio sulla coda MSG1.
 * Questo messaggio viene settato di tipo 2 quindi non blocca il polling del processo logger.
 * @param text Indirizzo alla stringa da inviare insieme al tempo impiegato a trovare la chiave
 * @param start Tempo di inizio ricerca chiave
 * @param end Tempo di fine ricerca chiave
 */
void send_timeelapsed(const char *text, time_t start, time_t end);

/**
 * @brief Salva la chiave key sul segmento di memoria addr_from_keys rispetto l'indice my_string
 * @brief La chiave key tradotta in stringa esadecimale occupa 8 caratteri, quindi il segmento di memoria
 * viene fatto scorrere di 8 caratteri a partire da addr_from_keys per un numero di volte pari a my_string.
 * In questo modo arrivo a puntare alla giusta locazione di memoria in cui scrivere la chiave.
 * La chiave viene tradotta in stringa esadecimale tramite la funzione toHexadecimal() e poi viene scritta
 * la stringa risultante in memoria.
 * @param addr_from_keys Indirizzo al segmento di memoria in cui vengono salvate le chiavi
 * @param my_string Indice della chiave rispetto alla stringa che è stata analizzata
 * @param key Chiave da salvare su addr_from_keys rispetto l'indice my_string
 */
void save_key(char *addr_from_keys, int my_string, unsigned int key);

/**
 * @brief Traduce il tempo time in stringa
 * @brief La stringa viene sovrascritta da destra verso sinistra. Per ricavare ogni cifra di time viene usato
 * il metodo del resto della divisione con 10 in un ciclo fino a quando time diventa uguale a 0.
 * @param time Tempo (long int) che deve essere tradotto in stringa
 * @param str Indirizzo alla stringa su cui salvare il tempo time tradotto in stringa
 * @param length Lunghezza della stringa str
 */
void time_to_string(long int time, char *str, int length);

/**
 * @brief Traduce un unsigned int in stringa esadecimale
 * @brief La stringa viene sovrascritta da destra verso sinistra. Per ricavare ogni cifra di uinteger in esadecimale
 * viene usato il metodo del resto della divisione con 16 in un ciclo fino a quando uinteger diventa uguale a 0.
 * @param uinteger Valore unsigned int da tradurre in stringa esadecimale
 * @param hex Indirizzo alla stringa su cui salvare l'unsigned int tradotto in stringa esadecimale
 * @param length Lunghezza della stringa hex
 */
void toHexadecimal(unsigned int uinteger, char *hex, int length);

#endif
