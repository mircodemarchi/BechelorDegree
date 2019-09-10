#include <fcntl.h>
#include <unistd.h>
#include "../include/padre.h"
#include "../include/mylib.h"
#define STDOUT 1

int main(int argc, char *argv[]){
    int fd;
    ssize_t write_byte;

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

    fd = open(argv[2], O_RDWR|O_EXCL, 0666);
    if(fd != -1)
        check_error(-1, "system error: Inserire path ad un file_output non esistente\n");

    padre(argv[1], argv[2]);
}
