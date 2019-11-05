/// @file
/// @author De Marchi Mirco

#include <stdlib.h>
#include <unistd.h>
#include "../include/mylib.h"
#define STDOUT 1

void check_error(int err, char *text){
    int i = 0;

    if(err == -1){

        while(*(text + i) != '\0'){
            write(STDOUT, text + i, 1);
            i++;
        }

        exit(-1);
    }
}

void check_thread_error(int err, char *text){
    int i = 0;
    char tmp_str_err[] = "000\n";
    char *str_err;

    if(err){
        int_to_str(err, tmp_str_err, sizeof(tmp_str_err) - 1);

        while(*(tmp_str_err + i) == '0')
            i++;

        str_err = tmp_str_err + i;

        i = 0;
        while(*(text + i) != '\0'){
            write(STDOUT, text + i, 1);
            i++;
        }

        i = 0;
        while(*(str_err + i) != '\0'){
            write(STDOUT, str_err + i, 1);
            i++;
        }

        exit(-1);
    }
}

void int_to_str(int integer, char *str, int length ){
    int resto;
    int i = length - 1;

    while(integer != 0){
        resto = integer % 10;
        str[i] = '0' + resto;
        i--;
        integer /= 10;
    }
}
