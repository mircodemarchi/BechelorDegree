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
#include <stdio.h>
#include "../include/nipote.h"
#include "../include/types.h"
#include "../include/mylib.h"
#include "../include/figlio.h"
#define SHMKEY1 11
#define MSGKEY 21
#define SEMKEY1 31
#define STDOUT 1

void figlio(int nrow){
    int semid1;

    signal(SIGUSR1, status_updated);

    semid1 = set_sem1();

    create_process_nipoti(nrow);

    wait(NULL);
    wait(NULL);

    send_terminate("ricerca conclusa\n");
    check_error(semctl(semid1, 0, IPC_RMID,  (semun_t) 0), "system error: semctl() failed\n");
    exit(0);
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

void create_process_nipoti(int row_file){
    pid_t pid;

    pid = fork();
    check_error(pid, "system error: fork() failed\n");

    if(pid == 0)
        nipote(1, row_file);

    pid = fork();
    check_error(pid, "system error: fork() failed\n");

    if(pid == 0)
        nipote(2, row_file);
}

void status_updated(int signal){
    int semid1;
    int shmid1;
    int i;
    char *addr_shm1;
    Status * s;
    ssize_t write_byte;
    char str_grandson[] = "0000000";
    char str_id_string[] = "0000000";
    char *str_x, *str_y;
    int length_x, length_y;

    semid1 = semget(SEMKEY1, 0, 0666);
    check_error(semid1, "system error: semget() failed\n");

    shmid1 = shmget(SHMKEY1, sizeof(Status), 0666);
    check_error(shmid1, "system error: shmget() failed\n");

    if( (addr_shm1 = shmat(shmid1, (void *) NULL, 0)) == (void *) -1){
        perror("system error: shmat() failed\n");
        exit(1);
    }
    s = (Status *) addr_shm1;

    int_to_str(s->grandson, str_grandson, sizeof(str_grandson) - 1);
    int_to_str(s->id_string, str_id_string, sizeof(str_id_string) - 1);

    i = 0;
    while(*(str_grandson + i) == '0')
        i++;

    str_x = str_grandson + i;
    length_x = sizeof(str_grandson) - 1 - i;

    i = 0;
    while(*(str_id_string + i) == '0')
        i++;

    str_y = str_id_string + i;
    length_y = sizeof(str_id_string) - 1 - i;

    //Il nipote X sta analizzando la Y-esima stringa.
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

    unlock(semid1, 0);
}

void send_terminate(const char *text){
    int msgid;
    Message msg;
    int i;

    msgid = msgget(MSGKEY, 0666);
    check_error(msgid, "system error: msgget() failed\n");

    for(i = 0; i < 128 && *(text + i) != '\0'; i++)
        *(msg.text + i) = *(text + i);
    *(msg.text + i) = '\0';

    msg.mtype = 1;
    msg.length = i;
    check_error(msgsnd(msgid, &msg, sizeof(Message) - sizeof(long), 0), "system error: msgrcv() failed\n");
}
