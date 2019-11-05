/// @file
/// @author De Marchi Mirco

#include <signal.h>
#include <sys/wait.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <sys/msg.h>
#include <sys/sem.h>
#include <unistd.h>
#include <sys/types.h>
#include <stdlib.h>
#include <time.h>
#include <pthread.h>
#include "../include/nipote.h"
#include "../include/types.h"
#include "../include/mylib.h"
#include "../include/figlio_thread.h"
#define SHMKEY1 11
#define SHMKEY2 12
#define MSGKEY 21
#define SEMKEY1 31
#define STDOUT 1
#define NUMTHREADS 4

char *g_addr_shm1, *g_addr_shm2;
int g_semid1;
int g_row_file;

void figlio(int nrow){
    g_row_file = nrow;
    pthread_t thread[NUMTHREADS];
    thread_args args[NUMTHREADS];

    // Registro status_updated come signal handler del segnale SIGUSR1
    signal(SIGUSR1, status_updated);

    // Creo e ottengo l'id del semaforo SEM1 e faccio l'attach della SHM1 e SHM2
    g_semid1 = set_sem1();
    g_addr_shm1 = get_shm(SHMKEY1);
    g_addr_shm2 = get_shm(SHMKEY2);

    // Creo le Thread e aspetto la loro terminazione
    create_thread(thread, args);
    wait_thread(thread);
    
    // Invio messaggio di terminazione ricerca chiavi ed elimino il semaforo SEM1
    send_terminate("ricerca conclusa\n");
    check_error(semctl(g_semid1, 0, IPC_RMID,  (semun_t) 0), "system error: semctl() failed\n");
    exit(0);
}

void *nipote_thread(void *thread_arg){
    thread_args *args = (thread_args *) thread_arg;
    int my_string;
    int err;
    Status *s;
    char *addr_on_string;

    int length_plain_encoded_text;
    char *plain_text;
    char *encoded_text;
    unsigned int *array_plain;
    unsigned int *array_encoded;

    unsigned int key;

    time_t start;
    time_t end;

    s = (Status *) g_addr_shm1;

    while(1){
        // Mi blocco nel caso in cui la zona critica sia già occupata altrimenti accedo
        lock(g_semid1, 0);

        // Zona critica
	/*
	 * Leggo il valore di id_string, se il valore è maggiore del numero di righe
	 * della zona di memoria contenenti le stringhe allora esco, altrimenti incremento
	 * id_string, scrivo su grandson all'interno della struct Status l'id del processo
	 * nipote e invio al processo figlio il segnale SIGUSR1
	 */
        my_string = s->id_string;
        if(my_string >= g_row_file)
            break;

        s->grandson = args->id_thread;
        (s->id_string)++;
        err = kill(getpid(), SIGUSR1);
        check_error(err, "system error: kill() failed\n");
        
        // Trovo l'indirizzo di memoria relativo alla stringa che devo leggere
        addr_on_string = load_string(g_addr_shm1 + sizeof(Status), my_string);
        
        // Trovo gli indirizzi di plain_text e encoded_text e faccio la conversione a unsigned int
        plain_text = addr_on_string + 1;
        length_plain_encoded_text = length_text(plain_text);
        encoded_text = plain_text + length_plain_encoded_text + 3;

        array_plain = (unsigned int *) plain_text;
        array_encoded = (unsigned int *) encoded_text;
        
        // Cerco la chiave, calcolando il tempo che ci impiego
        start = time(NULL);
        key = find_key(array_plain, array_encoded, length_plain_encoded_text / 4);
        end = time(NULL);
        
        // Salvo il valore della chiave nello spazio relativo a id_string nella SHM2
        save_key(g_addr_shm2, my_string, key);
        
        // Invio un messaggio nella coda MSG1 dicendo il tempo impegato a trovare la chiave
        send_timeelapsed("chiave trovata in ", start, end);
    }
    
    // Sblocco il semaforo ed esco
    unlock(g_semid1, 0);
    pthread_exit((void *) 0);
}

int set_sem1(){
    int semid;
    semun_t args;

    semid = semget(SEMKEY1, 1, IPC_CREAT|0666);
    check_error(semid, "system error: semget() failed\n");

    args.val = 1;
    check_error(semctl(semid, 0, SETVAL, args), "system error: semctl() failed\n");

    return semid;
}

void create_thread(pthread_t *thread_list, thread_args *args){
    pthread_attr_t attr;
    int t;
    int err;
    
    /*
     * Setto gli attributi delle thread ai valori di default e setto lo
     * stato di detach delle thread a JOINABLE
     */
    err = pthread_attr_init(&attr);
    check_thread_error(err, "thread error: pthread_attr_init() failed with err code: ");
    err = pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_JOINABLE);
    check_thread_error(err, "thread error: pthread_attr_setdetachstate() failed with err code: ");

    /*
     * Per ogni thread assegno alla struttura da passargli
     * come argomento l'id delle thread, poi creo le thread sulla 
     * funzione nipote_thread()
     */
    for(t = 0; t < NUMTHREADS; t++){
        args[t].id_thread = t + 1;
        err = pthread_create(&thread_list[t], &attr, nipote_thread, (void *) &args[t]);
        check_thread_error(err, "thread error: pthread_create() failed with err code: ");
    }

    pthread_attr_destroy(&attr);
}

void wait_thread(pthread_t *thread_list){
    int t;
    int err;
    
    // Per ogni thread, faccio una join ovvero attendo che la thread finisca la sua esecuzione
    for(t = 0; t < NUMTHREADS; t++) {
        err = pthread_join(thread_list[t], (void **) 0);
        check_thread_error(err, "thread error: pthread_join() failed with err code: ");
    }

}

void status_updated(int signal){
    int i;
    Status * s;
    ssize_t write_byte;
    char str_grandson[] = "0000000";
    char str_id_string[] = "0000000";
    char *str_x, *str_y;
    int length_x, length_y;

    s = (Status *) g_addr_shm1;
    
    // Converto grandson e id_string in stringhe
    int_to_str(s->grandson, str_grandson, sizeof(str_grandson) - 1);
    int_to_str(s->id_string, str_id_string, sizeof(str_id_string) - 1);
    
    /*
     * Sposto il puntatore str_x per escludere gli '0' di str_grandson e 
     * trovo la lunghezza di grandson in stringa.
     */
    i = 0;
    while(*(str_grandson + i) == '0')
        i++;

    str_x = str_grandson + i;
    length_x = sizeof(str_grandson) - 1 - i;
    
    /*
     * Sposto il puntatore str_y per escludere gli '0' di str_id_string e 
     * trovo la lunghezza di id_string in stringa.
     */
    i = 0;
    while(*(str_id_string + i) == '0')
        i++;

    str_y = str_id_string + i;
    length_y = sizeof(str_id_string) - 1 - i;

    /*
     * Scrivo sullo STDOUT:
     *    "Il nipote X sta analizzando la Y-esima stringa."
     */
    write_byte = write(STDOUT, "Il nipote ", 11);
    check_error(write_byte, "system error: write() failed\n");
    write_byte = write(STDOUT, str_x, length_x);
    check_error(write_byte, "system error: write() failed\n");
    write_byte = write(STDOUT, " sta analizzando la ", 21);
    check_error(write_byte, "system error: write() failed\n");
    write_byte = write(STDOUT, str_y, length_y);
    check_error(write_byte, "system error: write() failed\n");
    write_byte = write(STDOUT, "-esima stringa.\n", 17);
    check_error(write_byte, "system error: write() failed\n");
    
    /*
     * Sblocco il semaforo SEM1 per permettere al processo nipote
     * di accedere alla struttura Status nella zona critica.
     */
    unlock(g_semid1, 0);
}

void send_terminate(const char *text){
    int msgid;
    Message msg;
    int i;

    msgid = msgget(MSGKEY, 0666);
    check_error(msgid, "system error: msgget() failed\n");
    
    // Copio la stringa text su msg.text
    for(i = 0; i < 128 && *(text + i) != '\0'; i++)
        *(msg.text + i) = *(text + i);
    *(msg.text + i) = '\0';
    
    // Setto il tipo di messaggio a 1 e la sua lunghezza, poi faccio la send del messaggio
    msg.mtype = 1;
    msg.length = i;
    check_error(msgsnd(msgid, &msg, sizeof(Message) - sizeof(long), 0), "system error: msgrcv() failed\n");
}
