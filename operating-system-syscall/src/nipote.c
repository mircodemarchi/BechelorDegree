/// @file
/// @author De Marchi Mirco

#include <sys/ipc.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <sys/msg.h>
#include <stdlib.h>
#include <sys/types.h>
#include <signal.h>
#include <unistd.h>
#include <time.h>
#include "../include/types.h"
#include "../include/mylib.h"
#include "../include/nipote.h"
#define SHMKEY1 11
#define SHMKEY2 12
#define MSGKEY 21
#define SEMKEY1 31
#define STDOUT 1

void nipote(int x, int nrow){
    int my_string;
    char *addr_shm1, *addr_shm2;
    int semid1;
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
    
    /*
     * Faccio l'attach dei segmenti di memoria SHM1 e SHM2 e ricavo l'id del
     * semafoto SEM1 già creato
     */
    addr_shm1 = get_shm(SHMKEY1);
    s = (Status *) addr_shm1;

    addr_shm2 = get_shm(SHMKEY2);

    semid1 = semget(SEMKEY1, 0, 0666);
    check_error(semid1, "system error: semget() failed\n");

    while(1){
        // Mi blocco nel caso in cui la zona critica sia già occupata altrimenti accedo
        lock(semid1, 0);
	
	// Zona critica
	/*
	 * Leggo il valore di id_string, se il valore è maggiore del numero di righe
	 * della zona di memoria contenenti le stringhe allora esco, altrimenti incremento
	 * id_string, scrivo su grandson all'interno della struct Status l'id del processo
	 * nipote e invio al processo figlio il segnale SIGUSR1
	 */
        my_string = s->id_string;
        if(my_string >= nrow)
            break;

        s->grandson = x;
        (s->id_string)++;
        err = kill(getppid(), SIGUSR1);
        check_error(err, "system error: kill() failed\n");
	
	// Trovo l'indirizzo di memoria relativo alla stringa che devo leggere
        addr_on_string = load_string(addr_shm1 + sizeof(Status), my_string);
        
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
        save_key(addr_shm2, my_string, key);

	// Invio un messaggio nella coda MSG1 dicendo il tempo impegato a trovare la chiave
        send_timeelapsed("chiave trovata in ", start, end);
    }
	
    // Sblocco il semaforo ed esco
    unlock(semid1, 0);
    exit(0);
}

void *get_shm(key_t key){
    int shmid;
    void *addr_shm;

    shmid = shmget(key, (size_t) 0, 0666);
    check_error(shmid, "system error: shmget() failed\n");

    if( (addr_shm = shmat(shmid, (void *) NULL, 0)) == (void *) -1){
        write(STDOUT, "system error: shmat() failed\n", 30);
        exit(-1);
    }

    return addr_shm;
}

char *load_string(char *addr_from_file, int index_string){
    int i;
	
    // Scorro addr_from_file fino a quando trovo il carattere '\n' per index_string volte
    for( i = 0; i < index_string; i++){
        while(*addr_from_file != '\n')
            addr_from_file++;
        addr_from_file++;
    }

    return addr_from_file;
}

int length_text(char *text){
    int length = 0;

    // Trovo la lunghezza a partire dall'indirizzo puntato da text fino al carattere '>'
    while( *(text + length) != '>'){
        length++;
    }

    return length;
}

void lock(int semid, int semnum){
    struct sembuf sb;

    sb.sem_num = semnum;
    sb.sem_op = -1;
    sb.sem_flg = 0;
    check_error(semop(semid, &sb, 1), "system error: semop() failed\n");
}

void unlock(int semid, int semnum){
    struct sembuf sb;

    sb.sem_num = semnum;
    sb.sem_op = 1;
    sb.sem_flg = 0;
    check_error(semop(semid, &sb, 1), "system error: semop() failed\n");
}

unsigned int find_key(unsigned int *array_plain, unsigned int *array_encoded, int length){
    unsigned int key = 0;
    int key_find;
    int j;
    
    // Viene provato ogni valore della chiave key da 0 a 2^32 - 1
    while(1){
        key_find = 1;
        
        /*
         * Se per tutta la lunghezza degli array il risultato dello xor tra 
         * array_plain e la chiave risulta uguale a array_encoded, allora significa 
         * che la chiave è stata trovata e key_find rimane a 1
         */
        for(j = 0; j < length; j++){
            if( (array_plain[j] ^ key) != array_encoded[j]){
                key_find = 0;
                break;
            }
        }

        if(key_find == 1)
            break;
	
	// Se la chiave non è stata trovata, provo la prossima
        key++;
    }

    return key;
}

void send_timeelapsed(const char *text, time_t start, time_t end){
    int msgid;
    Message msg;
    int i, j;
    double time_elapsed = ((double) (end - start));
    char str_seconds[] = " seconds\n";
    
    /*
     * Alloco una stringa nella quale verrà messo il valore del tempo 
     * e richiamo la funzione time_to_string()
     */
    char str_time[] = "00000000000000000000000000000000000";
    time_to_string(time_elapsed, str_time, sizeof(str_time) - 1);
    
    // Ricavo la coda di messaggi MSG1
    msgid = msgget(MSGKEY, 0666);
    check_error(msgid, "system error: msgget() failed\n");
    
    // Copio su msg.text il messaggio text in input
    for(i = 0; i < 128 && *(text + i) != '\0'; i++)
        *(msg.text + i) = *(text + i);
    
    // Concatenato con il valore del time tradotto in stringa escludendo gli '0'
    j = 0;
    while(str_time[j] != '\0'){
        if(str_time[j] != '0'){
            *(msg.text + i) = str_time[j];
            i++;
        }
        j++;
    }
    
    // Concatenato con la stringa " seconds\n"
    j = 0;
    while(str_seconds[j] != '\0'){
        *(msg.text + i) = str_seconds[j++];
        i++;
    }

    *(msg.text + i) = '\0';
    
    /*
     * Il messaggio viene settato di tipo 2 quindi non sblocca il polling del 
     * processo logger e poi viene inviato
     */
    msg.length = i;
    msg.mtype = 2;
    check_error(msgsnd(msgid, &msg, sizeof(Message) - sizeof(long), 0), "system error: msgsnd() failed\n");
}

void save_key(char *addr_from_keys, int my_string, unsigned int key){
    int i;
    char key_str[] = "00000000";
    
    // Calcolo l'indirizzo in cui inserire la chiave in base a my_string 
    addr_from_keys += (8 * my_string);
    
    // Converto la chiave trovata (unsigned int) in stringa esadecimale
    toHexadecimal(key, key_str, sizeof(key_str) - 1);
    
    // Scrivo la stringa decimale ricavata nella zona di memoria dedicata
    for( i = 0; i < 8; i++)
        *(addr_from_keys + i) = *(key_str + i);
}

void time_to_string(long int time, char *str, int length){
    long int resto;
    int i = length - 1;

    while(time != 0){
        resto = time % 10;
        str[i] = '0' + resto;
        i--;
        time /= 10;
    }
}

void toHexadecimal(unsigned int uinteger, char *hex, int length){
    unsigned int resto;
    int pos = length - 1;

    while(uinteger != 0 && pos >= 0){
        resto = uinteger % 16;
        switch(resto){
            case 10: hex[pos] = 'A'; break;
            case 11: hex[pos] = 'B'; break;
            case 12: hex[pos] = 'C'; break;
            case 13: hex[pos] = 'D'; break;
            case 14: hex[pos] = 'E'; break;
            case 15: hex[pos] = 'F'; break;
            default: hex[pos] = '0' + resto; break;
        }

        uinteger = uinteger / 16;
        pos--;
    }

}
