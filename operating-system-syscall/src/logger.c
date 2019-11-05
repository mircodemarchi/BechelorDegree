/// @file
/// @author De Marchi Mirco

#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/sem.h>
#include <sys/msg.h>
#include <unistd.h>
#include <stdlib.h>
#include "../include/mylib.h"
#include "../include/nipote.h"
#include "../include/types.h"
#include "../include/logger.h"
#define MSGKEY 21
#define STDOUT 1
#define SEMKEY2 32

int g_msgid;
Message g_msg;

void logger(){
    int semid2;
    
    // Ricavo l'id del semaforo SEM2 già creato
    semid2 = semget(SEMKEY2, 0, 0666);
    check_error(semid2, "system error: semget() failed\n");
    
    // Creo una nuova coda di messaggi MSG1
    g_msgid = msgget(MSGKEY, 0666|IPC_CREAT );
    check_error(g_msgid, "system error: msgget() failed\n");
    
    /*
     * Ora che ho creato la coda di messaggi posso sbloccare il processo 
     * padre e iniziare il polling per la ricezione dei messaggi dalla coda
     */
    unlock(semid2, 0);
    polling_receive();

    check_error(msgctl(g_msgid, IPC_RMID, (struct msqid_ds *) NULL), "system error: msgctl() failed\n");
    exit(0);
}

void polling_receive(){
    ssize_t write_byte;
    struct msqid_ds buf;
    int i;
    semun_t args;
    
    // Fintantochè i messaggi ricevuto non sono di tipo 1 il polling continua
    do{
    	/*
    	 * Il controllo sulla coda viene fatto ogni secondo.
    	 * Se dentro la cosa trovo un messaggio allora lo stampo sullo STDOUT
    	 */
        sleep(1);
        if( msgrcv(g_msgid, &g_msg, sizeof(Message) - sizeof(long), 0, IPC_NOWAIT) != -1){
            write_byte = write(STDOUT, g_msg.text, g_msg.length);
            check_error(write_byte, "system error: write1() failed\n");
        }
    }while( g_msg.mtype != 1);
    
    // Ricavo quanti messaggi sono rimastri nella coda
    check_error(msgctl(g_msgid, IPC_STAT, &buf), "system error: msgctl() failed\n");
    
    // Stampo sullo STDOUT i rimanenti messaggi nella cosa
    for( i = 0; i < buf.msg_qnum; i++){
        check_error(msgrcv(g_msgid, &g_msg, sizeof(Message) - sizeof(long), 0, 0), "system error: msgrcv() failed\n");
        write_byte = write(STDOUT, g_msg.text, g_msg.length);
        check_error(write_byte, "system error: write2() failed\n");
    }
}
