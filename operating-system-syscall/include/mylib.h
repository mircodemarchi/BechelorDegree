/// @file
/// @author De Marchi Mirco

/// @defgroup mylib Funzioni di supporto mylib
/// @{
#ifndef MYLIB
#define MYLIB

/**
 * @brief Termina il processo in caso di errore
 * @brief Se err = -1 viene stampato il messaggio di errore text e viene terminato il processo con
 * codice di uscita -1.
 * @param err Codice di errore
 * @param text Messaggio di errore da stampare in caso di errore
 */
void check_error(int err, char *text);

/**
 * @brief Termina il processo in caso di errore di thread
 * @brief Se err è un valore diverso da 0 viene stampato sullo STDOUT il messaggio di errore text, viene stampato
 * il codice err e viene terminato il processo con codice di uscita -1.
 * Per poter fare la write()del codice err, viene usata la funzione int_to_str() per poterlo tradurre in stringa.
 * @param err Codice di errore
 * @param text Messaggio di errore da stampare in caso di errore
 */
void check_thread_error(int err, char *text);


/**
 * @brief Traduce un intero in stringa
 * @brief La stringa viene sovrascritta da destra verso sinistra. Per ricavare ogni cifra di integer viene usato
 * il metodo del resto della divisione con 10 in un ciclo fino a quando integer diventa uguale a 0.
 * @param str Stringa in cui salvare integer tradotto
 * @param length Lunghezza della stringa str
 */
void int_to_str(int integer, char *str, int length );

#endif

/// @}
