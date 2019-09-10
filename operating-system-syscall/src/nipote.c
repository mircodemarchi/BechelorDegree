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

    addr_shm1 = get_shm(SHMKEY1);
    s = (Status *) addr_shm1;

    addr_shm2 = get_shm(SHMKEY2);

    semid1 = semget(SEMKEY1, 0, 0666);
    check_error(semid1, "system error: semget() failed\n");

    while(1){
        lock(semid1, 0);

        //ZONA CRITICA
        my_string = s->id_string;
        if(my_string >= nrow)
            break;

        s->grandson = x;
        (s->id_string)++;
        err = kill(getppid(), SIGUSR1);
        check_error(err, "system error: kill() failed\n");

        addr_on_string = load_string(addr_shm1 + sizeof(Status), my_string);

        plain_text = addr_on_string + 1;
        length_plain_encoded_text = length_text(plain_text);
        encoded_text = plain_text + length_plain_encoded_text + 3;

        array_plain = (unsigned int *) plain_text;
        array_encoded = (unsigned int *) encoded_text;

        start = time(NULL);
        key = find_key(array_plain, array_encoded, length_plain_encoded_text / 4);
        end = time(NULL);

        save_key(addr_shm2, my_string, key);

        send_timeelapsed("chiave trovata in ", start, end);
    }

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

    for( i = 0; i < index_string; i++){
        while(*addr_from_file != '\n')
            addr_from_file++;
        addr_from_file++;
    }

    return addr_from_file;
}

int length_text(char *text){
    int length = 0;

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

    while(1){
        key_find = 1;
        for(j = 0; j < length; j++){
            if( (array_plain[j] ^ key) != array_encoded[j]){
                key_find = 0;
                break;
            }
        }

        if(key_find == 1)
            break;

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

    char str_time[] = "00000000000000000000000000000000000";
    time_to_string(time_elapsed, str_time, sizeof(str_time) - 1);

    msgid = msgget(MSGKEY, 0666);
    check_error(msgid, "system error: msgget() failed\n");

    for(i = 0; i < 128 && *(text + i) != '\0'; i++)
        *(msg.text + i) = *(text + i);

    j = 0;
    while(str_time[j] != '\0'){
        if(str_time[j] != '0'){
            *(msg.text + i) = str_time[j];
            i++;
        }
        j++;
    }

    j = 0;
    while(str_seconds[j] != '\0'){
        *(msg.text + i) = str_seconds[j++];
        i++;
    }

    *(msg.text + i) = '\0';

    msg.length = i;
    msg.mtype = 2;
    check_error(msgsnd(msgid, &msg, sizeof(Message) - sizeof(long), 0), "system error: msgsnd() failed\n");
}

void save_key(char *addr_from_keys, int my_string, unsigned int key){
    int i;
    char key_str[] = "00000000";

    for( i = 0; i < my_string; i++)
        addr_from_keys += 8;

    toHexadecimal(key, key_str, sizeof(key_str) - 1);

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
