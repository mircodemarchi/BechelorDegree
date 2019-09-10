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

    load_file(file_input);

    g_addr_shm2 = attach_segments(SHMKEY2, g_row * 8 * sizeof(char), &g_shmid2);

    // SEMAFORI SUPPLEMENTARI
    semid2 = set_sem2();

    // CREO SOTTOPROCESSI
    create_process_figlio_logger(semid2);

    wait(NULL);
    wait(NULL);

    //VERIFICA
    check_keys();
    detach_segments(g_addr_shm1, g_shmid1);

    // SALVO CHIAVI
    save_keys(file_output);
    detach_segments(g_addr_shm2, g_shmid2);

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

    g_row = count_file_row(g_addr_shm1 + sizeof(Status), size_input_file);

    s = (Status *) g_addr_shm1;
    s->id_string = 0;

    check_error(close(fd), "system error: close() failed\n");

}

int set_sem2(){
    int semid;
    semun_t args;

    semid = semget(SEMKEY2, 1, IPC_CREAT|0666);
    check_error(semid, "system error: semget() failed\n");

    args.val = 0;
    check_error(semctl(semid, 0, SETVAL, args), "system error: semctl() failed\n");

    return semid;
}

void create_process_figlio_logger(int semid){
    pid_t pid;

    pid = fork();
    check_error(pid, "system error: fork() failed\n");

    if(pid == 0)
        logger();

    lock(semid, 0);
    pid = fork();
    check_error(pid, "system error: fork() failed\n");

    if(pid == 0)
        figlio(g_row);
}

void check_keys(){
    char * addr_from_keys = g_addr_shm2;
    char * addr_from_file = g_addr_shm1 + sizeof(Status);
    int length_text;
    int i, j;
    unsigned int *array_plain;
    unsigned int *array_encoded;
    unsigned int key;
    char *addr_plain;
    char *addr_encoded;

    // SCORRO OGNI RIGA
    for(i = 0; i < g_row; i++){

        // CALCOLO LUNGHEZZA PLAIN E ENCODED TEXT
        length_text = 0;
        for(j = 0; *(addr_from_file + j) != ';'; j++)
            if( *(addr_from_file + j) != '<' && *(addr_from_file + j) != '>')
                length_text++;

        // CONVERSIONE
        //addr_from_keys += 2;
        key = 0;
        from_hex_to_uint(addr_from_keys, &key);

        addr_plain = addr_from_file + 1;
        addr_encoded = addr_plain + length_text + 3;

        array_plain = (unsigned int *) addr_plain;
        array_encoded = (unsigned int *) addr_encoded;

        // VERIFICA CHIAVE
        for(j = 0; j < (length_text / 4); j++){
            if((array_plain[j] ^ key) != array_encoded[j]){
                write(1, "system error: verification failed\n", 35);
                exit(1);
            }
        }

        addr_from_file += (2 * length_text) + 6;
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
