/**
 * @file
 * @author De Marchi Mirco
 *
 * Il processo figlio per le thread esegue in sequenza le seguenti operazioni:
 *
 * 1) Registra la funzione status_updated come signal handler del segnale SIGUSR1
 *
 * 2) Crea un semaforo SEM1, identificato dalla chiave 31, per poter sincronizzare i processi 
 * nipoti per la scrittura sulla memoria condivisa SHM1 della struttura Status
 *
 * 3) Esegue le thread
 *
 * 4) Aspetta che le thread concludano la loro esecuzione
 *
 * 5) Invia un messaggio nella MSG1 dicendo che la ricerca delle chiavi è stata conclusa
 *
 * 6) Chiude il semaforo SEM1
 *
 * 7) Termina 
 */
 
/// @defgroup procesaso_figlio_thread Funzioni del processo figlio_thread
/// @{
#ifndef FIGLIOTHREAD
#define FIGLIOTHREAD

/**
 * @brief Wrapper del processo figlio con implementazione delle thread
 * @param row Numero di righe del file di input salvato sulla memoria condivisa SHM1
 */
void figlio(int nrow);

/**
 * @brief Wrapper della funzione eseguita dalla thread
 * @brief La thread esegue in sequenza le seguenti operazioni:
 *
 * 1) Ricava tramite una get e una attach l'indirizzo della SHM1 e SHM2, identificate rispettivamente dalle chiavi 11 e 12
 *
 * 2) Ricava tramite una get l'id del semaforo SEM1, identificato dalla chiave 31
 *
 * 3) Entra in un ciclo che continua finchè ci sono stringhe da analizzare
 *
 * 4) Fa la lock() sul semaforo SEM1 e accede alla sezione critica
 *
 * 5) Legge l'indice della stringa che deve analizzare dalla struttura Status
 *
 * 6) Scrive sulla struttura Status il proprio id e incrementa l'indice alla stringa da analizzare
 *
 * 7) Invia il segnale SIGUSR1 al processo figlio
 *
 * 8) Tramite l'indice della stringa ricava l'indirizzo della SHM1 da cui parte la stringa che deve analizzare
 *
 * 9) Trova gli indirizzi delle stringhe plaintext ed encodedtext e li traduce in unsigned int
 *
 * 10) Trova la chiave memorizzando quanto tempo ci impiega
 *
 * 11) Salva la chiave nella SHM2
 *
 * 12) Accede alla coda di messaggi MSG1, identificata dalla chiave 21, per inviare un messaggio con i tempi di ritrovamento della chiave
 *
 * 13) Se le stringhe da analizzare sono finite termina, altrimenti analizza la prossima stringa
 *
 * Attenzione:
 * 
 * La lunghezza dell'array di unsigned int è pari alla lunghezza della
 * stringa diviso 4, perchè 4 caratteri della stringa corrispondono
 * a 1 unsigned int.
 *
 *  char = 1 byte
 *
 *  unsigned int = 4 byte
 * @param x ID del processo nipote
 * @param x ID del processo nipote
 * @param nrow Numero di righe del file di input salvato sulla memoria condivisa SHM1
 */
void *nipote_thread(void *thread_arg);

/**
 * @brief Crea il semaforo SEM1 e inizializza il suo valore a 1
 * @brief Il semaforo SEM1, identificato dalla chiave 31, serve ai processi nipote per potersi
 * sincronizzare per poter accedere alla zona critica contenuta all'interno del segmento di memoria condivisa
 * SHM1 sulla struttura Status.
 * Questo permetterà poi al processo figlio di poter accedere ai dati salvati sulla struttura Status dai processi
 * nipote in maniera consistente.
 * Per fare ciò l'unlock() del semaforo SEM1 dovrà essere fatta dal processo figlio dopo che il processo figlio
 * avrà letto i dati che gli interessano dalla sezione critica.
 * @return id del semaforo SEM1
 */
int set_sem1();

/**
 * @brief Setta gli attributi delle thread ai valori di default e crea le threads
 * @brief Richiama pthread_attr_init() per settare l'oggetto attributi ai valori di default e richiama
 * pthread_attr_setdetachstate() per settare le thread JOINABLE.
 * In fine per ogni valore di thread_list viene fatta la pthread_create().
 * @param thread_list Lista delle thread
 */
void create_thread(pthread_t *thread_list, thread_args *args);

/**
 * @brief Aspetta che le thread finiscano di essere eseguite
 * @brief Per ogni valore di thread_list viene fatta la pthread_join().
 * @param thread_list Lista delle thread
 */
void wait_thread(pthread_t *thread_list);

/**
 * @brief Scrive sullo STDOUT lo stato dei processi nipote
 * @brief Per poter fare una write() sullo STDOUT dello stato corrente del processo nipote che ha inviato la signal,
 * la funzione signal handler status_updated() deve richiamare la get e l'attach della memoria condivisa SHM1 per
 * poter accedere alla zona di memoria condivisa che contiene la struttura Status, inoltre deve anche richiamare la get
 * del semaforo SEM1 per poter sbloccare il nipote per permettergli la scrittura al segmento di memoria critica in
 * cui è presente la struct Status.
 * In questo modo la funzione ha accesso ai campi grandson e id_string della struttura Status, che corrispondono rispettivamente
 * all'id assegnato al processo nipote e all'indice della stringa che il processo nipote sta analizzando dalla SHM1.
 * La funzione potrà accedere ai campi grandson e id_string senza problemi di inconsistenza dei dati poichè i processi nipoti
 * saranno sbloccati per accedere alla zona critica solo dopo la lettura di questi e la scrittura sullo STDOUT.
 * I campi grandson e id_string vengono convertiti in stringhe tramite la funzione int_to_str(), per poter essere scritti sullo
 * STDOUT tramite la write():
 * Per fare la conversione da intero a stringa vengono usate delle stringhe di lunghezza preimpostata e settate con i
 * caratteri a '0', quindi dopo aver richiamato int_to_str() verranno eseguiti dei passaggi per poter spostare il puntatore
 * di questa stringa sul pezzo di stringa corrispondente all'intero e per trovare la lunghezza dell'intero tradotto in stringa.
 * Dopo tutti questi passaggi si potranno fare tutte le write() necessarie per scrivere la stringa
 * "Il nipote X sta analizzando la Y-esima stringa.\n", dove X = grandoson e Y = id_string, e in fine fare l'unlock()
 * sul semaforo che blocca i processi nipote prima della sezione critica.
 * @param signal Segnale ricevuto che ha attivato la signal handler
 */
void status_updated(int signal);

/**
 * @brief Invia il messaggio di terminazione di analisi delle stringhe
 * @brief Il messaggio text dovrà essere inviato tramite la coda di messaggi MSG1, quindi per prima cosa la funzione richiama
 * con la get la MSG1, dopodichè riscrive la stringa text sulla apposita struttura Message per la coda di messaggi e
 * in fine verrà fatta la send del messaggio.
 * Questo messaggio verrà settato di tipo 1, quindi sarà il messaggio che potrà sbloccare il processo logger dal meccanismo
 * di polling.
 * @param text Stringa del messaggio da inviare
 */
void send_terminate(const char *text);

#endif

/// @}
