/// @file
/// @author De Marchi Mirco

#include <fcntl.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <sys/ipc.h>
#include <sys/wait.h>
#include "../include/figlio.h"
#include "../include/nipote.h"
#include "../include/mylib.h"
#include "../include/types.h"
#include "../include/logger.h"
#include "../include/padre.h"
#define SHMKEY1 11
#define SHMKEY2 12
#define SEMKEY2 32
#define BUFLEN 1024
#define STDOUT 1

int g_shmid1, g_shmid2;
char *g_addr_shm1;
char *g_addr_shm2;
int g_row;

void padre(char *file_input, char *file_output){
    int semid2;
    
    // Carico il file_input sulla SHM1 
    load_file(file_input);
    
    // Creo la SHM2 e ricavo il suo indirizzo
    g_addr_shm2 = attach_segments(SHMKEY2, g_row * 8 * sizeof(char), &g_shmid2);

    // Semaforo supplemetare per sincronizzare logger() con figlio()
    semid2 = set_sem2();

    // CREO SOTTOPROCESSI
    create_process_figlio_logger(semid2);

    // Aspetto la terminazione di figlio() e logger()
    wait(NULL);
    wait(NULL);

    // Verifico che le chiavi trovate siano corrette e elimino la SHM1
    check_keys();
    detach_segments(g_addr_shm1, g_shmid1);

    // Salvo le chiavi in file_output e elimino la SHM2
    save_keys(file_output);
    detach_segments(g_addr_shm2, g_shmid2);
    
    // Elimino il semaforo SEM2
    check_error(semctl(semid2, 0, IPC_RMID, (semun_t) 0), "system error: semctl() failed\n");

}

void load_file(char *file){
    int fd;
    Status *s;
    char *addr_from_file;
    off_t size_input_file;
    ssize_t read_byte;
    
    fd = open(file, O_RDONLY, 0);
    check_error(fd, "system error: open() failed\n");

    // Calcolo lunghezza del file
    size_input_file = lseek(fd, (off_t) 0, SEEK_END);
    check_error((int) size_input_file, "system error: lseek() failed\n");

    g_addr_shm1 = attach_segments(SHMKEY1, sizeof(Status) + (size_input_file * sizeof(char)), &g_shmid1);

    // Copio file_input in shm1
    addr_from_file = g_addr_shm1 + sizeof(Status);
    check_error((int) lseek(fd, (off_t) 0, SEEK_SET), "system error: lseek() failed\n");
    while( (read_byte = read(fd, addr_from_file, BUFLEN)) > 0){
        addr_from_file += read_byte;
    }
    check_error(read_byte, "system error: read() failed\n");

    // Calcolo numero righe del file dalla SHM1
    g_row = count_file_row(g_addr_shm1 + sizeof(Status), size_input_file);

    // Inizializzo a 0 id_string
    s = (Status *) g_addr_shm1;
    s->id_string = 0;

    check_error(close(fd), "system error: close() failed\n");

}

int set_sem2(){
    int semid;
    semun_t args;

    semid = semget(SEMKEY2, 1, IPC_CREAT|0666);
    check_error(semid, "system error: semget() failed\n");

    // Inizializzo a 0 il semaforo SEM2
    args.val = 0;
    check_error(semctl(semid, 0, SETVAL, args), "system error: semctl() failed\n");

    return semid;
}

void create_process_figlio_logger(int semid){
    pid_t pid;
    
    // Creo logger
    pid = fork();
    check_error(pid, "system error: fork() failed\n");

    if(pid == 0)
        logger();

    // Aspetto che logger abbia creato la coda di messaggi MSG1
    lock(semid, 0);
    
    // Creo figlio
    pid = fork();
    check_error(pid, "system error: fork() failed\n");

    if(pid == 0)
        figlio(g_row);
}

void check_keys(){
    char * addr_from_keys = g_addr_shm2;
    char * addr_from_file = g_addr_shm1 + sizeof(Status);
    int length;
    int i, j;
    unsigned int *array_plain;
    unsigned int *array_encoded;
    unsigned int key;
    char *addr_plain;
    char *addr_encoded;

    // Scorro ogni riga
    for(i = 0; i < g_row; i++){

        // Calcolo la lunghezza del plain_text/encoded_text corrente
        length = length_text(addr_from_file + 1);

        /*
         * Conversione della chiave da stringa esadecimale a unsigned 
         * e conversione delle stringhe plain_text e encoded_text in
         * array di unsigned int
         */
        key = 0;
        from_hex_to_uint(addr_from_keys, &key);

        addr_plain = addr_from_file + 1;
        addr_encoded = addr_plain + length + 3;

        array_plain = (unsigned int *) addr_plain;
        array_encoded = (unsigned int *) addr_encoded;

        /*
         *   Verifica chiave
         * La lunghezza dell'array di unsigned int è pari alla lunghezza della
         * stringa diviso 4, perchè 4 caratteri della stringa corrispondono
         * a 1 unsigned int.
         *
         *  char = 1 byte
         *  unsigned int = 4 byte
         */
        for(j = 0; j < (length / 4); j++){
            if((array_plain[j] ^ key) != array_encoded[j]){
                write(1, "system error: verification failed\n", 35);
                exit(-1);
            }
        }

        addr_from_file += (2 * length) + 6;
        addr_from_keys += 8;
    }
}

void save_keys(char *file){
    char *addr_from_keys = g_addr_shm2;
    int fd;
    int i;
    ssize_t write_byte;

    fd = open(file, O_RDWR|O_CREAT|O_EXCL, 0666);
    check_error(fd, "system error: open() failed\n");
    
    /*
     * Scrivo sul file la stringa 0xFFFFFFFF\n, dove FFFFFFFF è 
     * la chiave in formato stringa esadecimale.
     */
    for(i = 0; i < g_row; i++){
        write_byte = write(fd, "0x", 2);
        check_error(write_byte, "system error: write() failed\n");
        write_byte = write(fd, addr_from_keys + (i * 8), 8);
        check_error(write_byte, "system error: write() failed\n");
        write_byte = write(fd, "\n", 1);
        check_error(write_byte, "system error: write() failed\n");
    }

    check_error(close(fd), "system error: close() failed\n");
}

void *attach_segments(key_t key, size_t size, int *shmid){
    void *addr_shm;

    *shmid = shmget(key, size, IPC_CREAT|0666);
    check_error(*shmid, "system error: shmget() failed\n");

    if( (addr_shm = shmat(*shmid, (void *) NULL, 0)) == (void *) -1){
        write(STDOUT, "system error: shmat() failed\n", 30);
        exit(-1);
    }

    return addr_shm;
}

void detach_segments(void *addr_shm, int shmid){
    check_error(shmdt(addr_shm), "system error: shmdt() failed\n");
    check_error(shmctl(shmid, IPC_RMID, (struct shmid_ds *) NULL), "system error: shmctl() failed \n");
}

int count_file_row(char *addr_from_file, int size){
    int row = 0;
    int i;

    for(i = 0; i < size; i++){
        if(*(addr_from_file + i) == '\n' )
            row++;
    }

    return row;
}

void from_hex_to_uint(char *hex, unsigned int *uint){
    int i;
    unsigned int val, base = 1;

    for(i = 7; i >= 0; i--){
        switch(*(hex + i)){
            case 'A': val = 10; break;
            case 'B': val = 11; break;
            case 'C': val = 12; break;
            case 'D': val = 13; break;
            case 'E': val = 14; break;
            case 'F': val = 15; break;
            default: val = hex[i] - '0'; break;
        }

        *uint += val * base;

        base *= 16;
    }
}
