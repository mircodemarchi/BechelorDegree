/**
 * @file
 * @author De Marchi Mirco
 *
 * Il processo figlio esegue in sequenza le seguenti operazioni:
 *
 * 1) Registra la funzione status_updated come signal handler del segnale SIGUSR1
 *
 * 2) Crea un semaforo SEM1, identificato dalla chiave 31, per poter sincronizzare 
 * i processi nipoti 
 * per la scrittura sulla memoria condivisa SHM1 della struttura Status
 *
 * 3) Fa la fork() dei due processi nipote: nipote(1) e nipote(2)
 *
 * 4) Aspetta che i processi nipote concludano la loro esecuzione
 *
 * 5) Invia un messaggio nella MSG1 dicendo che la ricerca delle chiavi è stata conclusa
 *
 * 6) Chiude il semaforo SEM1
 *
 * 7) Termina
 */
 

/// @defgroup processo_figlio Funzioni del processo figlio
/// @{
#ifndef FIGLIO
#define FIGLIO

/**
 * @brief Wrapper del processo figlio
 * @param nrow Numero di righe del file di input salvato sulla memoria condivisa SHM1
 */
void figlio(int nrow);

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
 * @brief Esegue la fork() per i processi nipote
 * @brief Crea due processi nipote, al primo gli assegna il valore 1, al secondo gli assegna il valore 2.
 * Inoltre al processo nipote serve come parametro di incresso il numero di righe del file di input,
 * che è il parametro passato a questa funzione.
 * @param row_file Numero di righe del file di input salvato sulla memoria condivisa SHM1
 */
void create_process_nipoti(int row_file);

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
