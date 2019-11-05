/// @file
/// @author De Marchi Mirco

#include <fcntl.h>
#include <unistd.h>
#include "../include/padre.h"
#include "../include/mylib.h"
#define STDOUT 1

int main(int argc, char *argv[]){
    int fd;
    ssize_t write_byte;
    
    /*
     * Se dopo la chaiamata al file eseguibile sono stati agiiunti meno di 2 argomenti
     * oppure più di 2 argomenti, stampo il rispettivo messaggio di errore sullo STDOUT
     */
    if( argc < 3){
        write_byte = write(STDOUT, "args 1 -> file_input\nargs 2 -> file_output\n", 44);
        check_error((int) write_byte, "system error: write() failed\n");
        check_error(-1, "system error: Inserire almeno 2 argomenti\n");
    }
    else if(argc > 3){
        write_byte = write(STDOUT, "args 1 -> file_input\nargs 2 -> file_output\n", 44);
        check_error((int) write_byte, "system error: write() failed\n");
        check_error(-1, "system error: Inserire solo 2 argomenti\n");
    }
    
    /*
     * Verifico se il file specificato dal secondo argomento inserito è esistente
     * e in tal caso stampo un messaggio di errore.
     */
    fd = open(argv[2], O_RDWR|O_EXCL, 0666);
    if(fd != -1)
        check_error(-1, "system error: Inserire path ad un file_output non esistente\n");
    
    // Richiamo la funzione padre con la stringa di file_input e file_output
    padre(argv[1], argv[2]);
}
