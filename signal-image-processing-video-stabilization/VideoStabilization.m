% Progetto di Elaborazione dei segnali e immagini
% VIDEO STABILIZZAZIONE
% Author: Mirco De Marchi
%
% CdS = Caso di Studio
%
% Algoritmi di Stabilizzazione usati:
%  - STABILIZZAZIONE AL CENTRO              : CdS 1-10
%  - STABILIZZAZIONE RISPETTO AL CROP       : CdS 11-21
%  - STABILIZZAZIONE CON SELEZIONE DI FRAME : CdS 22-48
%
% STABILIZZAZIONE AL CENTRO: showStabilization()
%  Rispetto all'algoritmo di stabilizzazione visto a lezione, questo
%  algoritmo allinea l'immagine che ha identificato con la Cross
%  Correlazione 2D al centro del frame risultante. 
%
% STABILIZZAZIONE RISPETTO AL CROP: showStabilization2()
%  Rispetto all'algoritmo di stabilizzazione visto a lezione, questo
%  algoritmo allinea l'immagine che ha identificato con la Cross
%  Correlazione 2D rispetto alla posizione del template preso con il crop 
%  dal video.
%
% STABILIZZAZIONE CON SELEZIONE DI FRAME: showOptimizedStabilization()
%  Funziona esattamente come la stabilizzazione rispetto al crop, solo che
%  scarta alcuni frame in base a un valore di tolleranza valutato in
%  funzione della correlazione con il template scelto. Se il valore massimo 
%  di correlazione dei frame del video rispetto al template sforano la
%  tolleranza inserita, allora quel frame verrà scartato, altrimenti viene
%  mantenuto. 
%
% Legenda:
%  - frames:                Cds  1 e 11
%  - frames_beach01:        CdS 12
%  - frames_beach02:        CdS 34
%  - frames_beach03:        CdS 23
%  - frames_beach04:        CdS 13
%  - frames_beach05:        CdS  2
%  - frames_beach06:        CdS 14
%  - frames_beach07:        CdS 15
%  - frames_beach08:        CdS 24
%  - frames_beach09:        CdS 16
%  - frames_beach10:        CdS 17
%  - frames_beach11:        CdS 35
%  - frames_beach12:        CdS 36
%  - frames_beach13:        CdS 37      <- Educativo
%  - frames_beach14:        CdS 38      <- Problema della CC
%  - frames_beach15:        CdS 18
%  - frames_beach16:        CdS 39      <- Gabbiano riuscito bene
%  - frames_beach17:        CdS 40      <- Educativo
%  - frames_beach18:        CdS 41      <- Caso limite
%  - frames_beach19:        CdS 25
%  - frames_beach20:        CdS 42
%  - frames_beach21:        CdS 43
%  - frames_beach22:        CdS  3
%  - frames_beach23:        CdS 44      <- Educativo
%  - frames_beach24:        CdS 45      <- Educativo
%  - frames_hillclimb01:    CdS 19      <- Caso base
%  - frames_hillclimb02:    CdS  4
%  - frames_hillclimb03:    CdS 20
%  - frames_hillclimb04:    CdS 21
%  - frames_ola01:          CdS  5 
%  - frames_ola02:          CdS 26      <- Molto difficile
%  - frames_ola03:          CdS 46
%  - frames_ola04:          CdS  6
%  - frames_ola05:          CdS 47
%  - frames_ola06:          CdS  7
%  - frames_Phil:
%  - frames_Phil1-60:       CdS 22
%  - frames_Phil60-130:     CdS 33
%  - frames_tokyo01:        CdS 27
%  - frames_tokyo02:        CdS 28      <- Molto difficile
%  - frames_train01:        CdS  8
%  - frames_train02:        CdS 29      <- Educativo
%  - frames_train03:        CdS 30
%  - frames_wolverine01:    CdS  9
%  - frames_wolverine02:    CdS 31      <- Educativo
%  - frames_wolverine03:    CdS 32
%  - frames_wolverine04:    CdS 48      <- Educativo
%  - frames_wolverine05:    CdS 10      <- Caso base
%
% Belli da far vedere: 10 Capriolo
%                      (2) Scritte
%                      3 Oggetti simili che si alternano 
%                      (4) Oggetti che ruotano
%                      (5) Oggetti simili nella stessa immagine
%                      9 Riflesso che confonde l'immagine
%                      13 Scritta che si allarga
%                      (22) Chiesa che crolla
%                      37 Uccello su oggetto
%                      40 Bandiera
%                      (39) Gabbianoq
%                      (38) Problema con la CC
%                      41 Caso limite

clear all; 
close all; 
clc; 

%% WATCH FRAME
% Funzione di supporto per visualizzare un 'frames.mat'.

FILENAME = 'frames_wolverine05';

watchFrame(FILENAME);

%% CROP FRAME
% Funzione di supporto per ritagliare un 'frames.mat' in un intervallo
% di numero di frame.

FRAMENAME_IN = 'frames_Phil';
FRAMENAME_OUT = 'frames_Phil60-130';
FROM = 60;
TO = 130;

cropFrame(FRAMENAME_IN, FRAMENAME_OUT, FROM, TO);

%% WATCH VIDEO
% Funzione di supporto per visualizzare un video.

FILENAME = 'output/stabilizedBeach04.mp4';

watchVideo(FILENAME);

%% CROP VIDEO
% Funzione di supporto per ritagliare un video in un intervallo 
% di minuti e secondi.

FILENAME = 'wolverine.mp4';
FRAMENAME = 'frames_wolverine05.mat';
FROMMIN = 1;
FROMSEC =48;
TOMIN = 1;
TOSEC = 50;

cropVideo(FILENAME, FRAMENAME, FROMMIN, FROMSEC, TOMIN, TOSEC);

%% Caso di Studio 1: Stabilizzazione al centro 
% Prendo come esempio il video di stabilizzazione della scimmia su un
% albero che abbiamo visto a lezione. Mi è servito per verificare
% l'efficacia della funzione showStabilization(), che a differenza della
% stabilizzazione vista a lezione allinea l'immagine da stabilizzare al
% centro del frame.
clear all
close all

load frames; % Scimmie su albero

% Input value
RESIZE_FACTOR = 0.3;
TEMPLATE_NUMBER = 10;
FILENAME_OUTPUT = 'output/stabilizedMonkey1';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~] = showStabilization(frames, TEMPLATE_NUMBER);
write2Video(frames, new, FILENAME_OUTPUT);

%% Caso di Studio 2: Stabilizzazione al centro 
% Questo esempio mi è servito per osservare come si comporta l'algoritmo di
% stabilizzazione sulle scritte.
clear all
close all

load frames_beach05; % Cartello 'Danger' rosso

% Input value
RESIZE_FACTOR = 0.6;
TEMPLATE_NUMBER = 10;
FILENAME_OUTPUT = 'output/stabilizedBeach05';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~] = showStabilization(frames, TEMPLATE_NUMBER);
write2Video(frames, new, FILENAME_OUTPUT);

%% Caso di Studio 3: Stabilizzazione al centro 
% Questo esempio è interessante perchè ci sono 2 foche che entrano e escono
% dall'acqua e volevo vedere se selezionandone una nel momento in cui
% scompariva, la stabilizzazione si focalizzava sull'altra.
clear all
close all

load frames_beach22; % Due foche

% Input value
RESIZE_FACTOR = 0.6;
TEMPLATE_NUMBER = 10;
FILENAME_OUTPUT = 'output/stabilizedBeach22';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~] = showStabilization(frames, TEMPLATE_NUMBER);
write2Video(frames, new, FILENAME_OUTPUT);
   
%% Caso di Studio 4: Stabilizzazione al centro 
% In questo esempio si può vedere che mentre l'auto fa le curve, lo
% specchietto, che è l'oggetto che ho scelto come riferimento da
% stabilizzare, ruota. Stabilizzando sullo spacchietto verifico se la
% stabilizzazione funziona, entro certi limiti, anche su oggetti non solo
% traslati ma anche un po' ruotati.
clear all
close all

load frames_hillclimb02; % Sterzate

% Input value
RESIZE_FACTOR = 0.3;
TEMPLATE_NUMBER = 10;
FILENAME_OUTPUT = 'output/stabilizedHillclimb02';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~] = showStabilization(frames, TEMPLATE_NUMBER);
write2Video(frames, new, FILENAME_OUTPUT);

%% Caso di Studio 5: Stabilizzazione al centro 
% Questo esempio, se valutato correttamente, può risultare simile al caso
% semplice tipo quello delle scimmie. Tuttavia questo video contiene tanti
% oggetti simili tra loro, ovvero persone in piedi e in movimento. Si vuole
% valutare se prendendone solo una si ottiene la stabilizzazione voluta.
clear all
close all

load frames_ola01; % Giochi in acqua

% Input value
RESIZE_FACTOR = 0.6;
TEMPLATE_NUMBER = 10;
FILENAME_OUTPUT = 'output/stabilizedOla01';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~] = showStabilization(frames, TEMPLATE_NUMBER);
write2Video(frames, new, FILENAME_OUTPUT);

%% Caso di Studio 6: Stabilizzazione al centro 
% Esempio semplice, analogo a quello delle scimmie, in cui un ragazzo 
% corre nell'acqua. Semplice da stabilizzare perchè sempre presente
% nell'immagine.
clear all
close all

load frames_ola04; % Corsa nell'acqua

% Input value
RESIZE_FACTOR = 0.6;
TEMPLATE_NUMBER = 10;
FILENAME_OUTPUT = 'output/stabilizedOla04';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~] = showStabilization(frames, TEMPLATE_NUMBER);
write2Video(frames, new, FILENAME_OUTPUT);

%% Caso di Studio 7: Stabilizzazione al centro 
% In questo caso è interessnate valutare di stabilizzare degli oggetti 
% piccoli, lontani e che cambiano forma. Questi oggetti sono persone 
% trascinate da una corrente d'acqua. Si vuole valutare fino a che punto la
% stabilizzazione riesce a resistere su questi oggetti.
clear all
close all

load frames_ola06; % Onda

% Input value
RESIZE_FACTOR = 0.6;
TEMPLATE_NUMBER = 10;
FILENAME_OUTPUT = 'output/stabilizedOla06';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~] = showStabilization(frames, TEMPLATE_NUMBER);
write2Video(frames, new, FILENAME_OUTPUT);

%% Caso di Studio 8: Stabilizzazione al centro 
% Esempio analogo a quello delle scimmie. Treno in arrivo. Semplice da
% stabilizzare perchè sempre presente nell'immagine.
clear all
close all

load frames_train01; % Treno 

% Input value
RESIZE_FACTOR = 0.3;
TEMPLATE_NUMBER = 10;
FILENAME_OUTPUT = 'output/stabilizedTrain01';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~] = showStabilization(frames, TEMPLATE_NUMBER);
write2Video(frames, new, FILENAME_OUTPUT);

%% Caso di Studio 9: Stabilizzazione al centro 
% In questo caso troviamo un ghiottone che sta nuotando in un fiume. Questo
% esempio sembra molto difficile da stabilizzare perchè il riflesso
% dell'acqua confonde parecchio la locazione del ghiottone. In questo
% esempio si può notare come questo algoritmo di stabilizzazione sia molto
% potente.
clear all
close all

load frames_wolverine01; % Ghiottone in acqua 

% Input value
RESIZE_FACTOR = 0.4;
TEMPLATE_NUMBER = 40;
FILENAME_OUTPUT = 'output/stabilizedWolverine01';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~] = showStabilization(frames, TEMPLATE_NUMBER);
write2Video(frames, new, FILENAME_OUTPUT);

%% Caso di Studio 10: Stabilizzazione al centro 
% Caso analogo a quelle delle scimmie in cui però c'è un capriolo. Semplice
% da stabilizzare perchè sempre presente nel'immagine.
clear all
close all

load frames_wolverine05; % Capriolo

% Input value
RESIZE_FACTOR = 0.3;
TEMPLATE_NUMBER = 10;
FILENAME_OUTPUT = 'output/stabilizedWolverine05';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~] = showStabilization(frames, TEMPLATE_NUMBER);
write2Video(frames, new, FILENAME_OUTPUT);

%% Caso di Studio11: Stabilizzazione rispetto al crop
% Prendo come esempio il video di stabilizzazione della scimmia su un
% albero che abbiamo visto a lezione. Mi è servito per verificare
% l'efficacia della funzione showStabilization2(), che a differenza della
% funzione showStabilization(), stabilizza l'immagine selezionata, 
% allineandola rispetto a dove questa è stata presa dal frame originale.
clear all
close all

load frames; % Scimmie 2

% Input value
RESIZE_FACTOR = 0.3;
TEMPLATE_NUMBER = 10;
FILENAME_OUTPUT = 'output/stabilizedMonkey2';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~] = showStabilization2(frames, TEMPLATE_NUMBER);
write2Video(frames, new, FILENAME_OUTPUT);

%% Caso di Studio 12: Stabilizzazione rispetto al crop
% Esempio semplice. In questo video ci sono molti oggetti che si possono
% selezionare per essere stabilizzati, di cui anche la scritta 'Resturant'.
clear all
close all

load frames_beach01; % Resturant

% Input value
RESIZE_FACTOR = 0.8;
TEMPLATE_NUMBER = 10;
FILENAME_OUTPUT = 'output/stabilizedBeach01';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~] = showStabilization2(frames, TEMPLATE_NUMBER);
write2Video(frames, new, FILENAME_OUTPUT);

%% Caso di Studio 13: Stabilizzazione rispetto al crop
% In questo caso si analizza una scritta che inizialemente è parzialmente 
% coperta e poi mano a mano viene scoperta. Si vuol capire come si 
% comporta la stabilizzazione su una scritta che varia.
clear all
close all

load frames_beach04; % Scritta che si allarga

% Input value
RESIZE_FACTOR = 0.8;
TEMPLATE_NUMBER = 10;
FILENAME_OUTPUT = 'output/stabilizedBeach04';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~] = showStabilization2(frames, TEMPLATE_NUMBER);
write2Video(frames, new, FILENAME_OUTPUT);

%% Caso di Studio 14: Stabilizzazione rispetto al crop
% In questo esempio si vuole mantenere l'orizzonte sempre allineato, usando
% la stabilizzazione. Si vuole analizzare quanto è efficace la
% stabilizzazione su un video che sull'asse delle x ha valori più o meno
% costanti.
clear all
close all

load frames_beach06; % Spiaggia cielo

% Input value
RESIZE_FACTOR = 0.6;
TEMPLATE_NUMBER = 10;
FILENAME_OUTPUT = 'output/stabilizedBeach06';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~] = showStabilization2(frames, TEMPLATE_NUMBER);
write2Video(frames, new, FILENAME_OUTPUT);

%% Caso di Studio 15: Stabilizzazione rispetto al crop
% In questo esempio si vuole selezionare degli oggetti in uno spazio
% uniforme. Lo spazio preso in considerazione è una spiaggia. Si vuole
% analizzare la stabilizzazione su degli oggetti sui quali l'algoritmo di
% stabilizzazione potrebbe fare confuzione, poichè inseriti in uno
% spazio di tonalità di colori molto simili.
clear all
close all

load frames_beach07; % Oggetti strani in spiaggia

% Input value
RESIZE_FACTOR = 0.6;
TEMPLATE_NUMBER = 10;
FILENAME_OUTPUT = 'output/stabilizedBeach07';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~] = showStabilization2(frames, TEMPLATE_NUMBER);
write2Video(frames, new, FILENAME_OUTPUT);

%% Caso di Studio 16: Stabilizzazione rispetto al crop
% Caso semplice in cui si vuole stabilizzare del personale al lavoro su una
% scala. Questo esempio risulta semplice perchè i lavoratori sono sempre
% presenti nel video e indossanno una giacchetta fosforescente arancione.
clear all
close all

load frames_beach09; % Lavoratori su scala

% Input value
RESIZE_FACTOR = 0.6;
TEMPLATE_NUMBER = 10;
FILENAME_OUTPUT = 'output/stabilizedBeach09';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~] = showStabilization2(frames, TEMPLATE_NUMBER);
write2Video(frames, new, FILENAME_OUTPUT);

%% Caso di Studio 17: Stabilizzazione rispetto al crop
% Caso semplice in cui si vuole stabilizzare una barca in mezzo alla
% nebbia. La barca è sempre presente nel video, dunque non è difficile
% eseguire la stabilizzazione.
clear all
close all

load frames_beach10; % Barchetta

% Input value
RESIZE_FACTOR = 0.6;
TEMPLATE_NUMBER = 10;
FILENAME_OUTPUT = 'output/stabilizedBeach10';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~] = showStabilization2(frames, TEMPLATE_NUMBER);
write2Video(frames, new, FILENAME_OUTPUT);

%% Caso di Studio 18: Stabilizzazione rispetto al crop
% Caso semplice in cui si vuole stabilizzare un gabbiano sulla
% spiaggia. Il gabbiano è sempre presente nel video, dunque non è difficile
% eseguire la stabilizzazione.
clear all
close all

load frames_beach15; % Gabbiani sulla spiaggia

% Input value
RESIZE_FACTOR = 0.6;
TEMPLATE_NUMBER = 10;
FILENAME_OUTPUT = 'output/stabilizedBeach15';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~] = showStabilization2(frames, TEMPLATE_NUMBER);
write2Video(frames, new, FILENAME_OUTPUT);

%% Caso di Studio 19: Stabilizzazione rispetto al crop
% In questo caso si vuole stabilizzare le riprese all'interno di un'auto di
% una corsa di hillclimb. Si vuole ricercare quale sia l'oggetto migliore
% da prendere in considerazione per ottimizzare la stabilizzazione: lo
% specchietto o altro?
% Interessante anche stabilizzare rispetto l'esterno dell'auto.
clear all
close all

load frames_hillclimb01; % Doppia sterzata

% Input value
RESIZE_FACTOR = 0.3;
TEMPLATE_NUMBER = 60;
FILENAME_OUTPUT = 'output/stabilizedHillclimb01';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~] = showStabilization2(frames, TEMPLATE_NUMBER);
write2Video(frames, new, FILENAME_OUTPUT);

%% Caso di Studio 20: Stabilizzazione rispetto al crop
% In questo caso, come nel caso precedente si vuole stabilizzare le 
% riprese all'interno di un'auto di una corsa di hillclimb. 
% Si vuole ricercare quale sia l'oggetto migliore da prendere in 
% considerazione per ottimizzare la stabilizzazione: lo specchietto o 
% altro?
clear all
close all

load frames_hillclimb03; % Doppia sterzata 2

% Input value
RESIZE_FACTOR = 0.3;
TEMPLATE_NUMBER = 30;
FILENAME_OUTPUT = 'output/stabilizedHillclimb03';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~] = showStabilization2(frames, TEMPLATE_NUMBER);
write2Video(frames, new, FILENAME_OUTPUT);

%% Caso di Studio 21: Stabilizzazione rispetto al crop
% In questo caso l'auto di hillclimb è ferma e si cerca di stabilizzare i
% movimenti del casco. Anche qui si analizza quale oggetto selezionare per
% stabilizzare al meglio il video. Lo specchietto allora va bene?
clear all
close all

load frames_hillclimb04; % Partenza

% Input value
RESIZE_FACTOR = 0.3;
TEMPLATE_NUMBER = 10;
FILENAME_OUTPUT = 'output/stabilizedHillclimb04';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~] = showStabilization2(frames, TEMPLATE_NUMBER);
write2Video(frames, new, FILENAME_OUTPUT);

%% Caso di Studio 22: Stabilizzazione con selezione di frame
% Prendo come esempio il video di stabilizzazione del crollo della chiesa 
% alle Filippine che abbiamo visto a lezione. Mi è servito per verificare
% l'efficacia della funzione showOptimizedStabilization(), che funziona
% esattamente come showStabilization2(), che però esclude tutti i frame che
% non soddisfano la condizione di tolleranza data.
clear all
close all

load frames_Phil1-60; % Crollo chiesa

% Input value
TOLERANCE = 0.1;
RESIZE_FACTOR = 0.3;
TEMPLATE_NUMBER = 10;
FILENAME_OUTPUT = 'output/stabilizedPhil1';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);

%% Caso di Studio 23: Stabilizzazione con selezione di frame
% In questo caso si analizza una macchina rossa che entra ed esce
% dall'inquadratura. Si vuole verificare la potenzialità dell'algoritmo di
% stabilizzazione e di selezione di frame su un oggetto abbastanza
% appariscente.
clear all
close all

load frames_beach03; % Macchina rossa

% Input value
TOLERANCE = 0.20;
RESIZE_FACTOR = 0.6;
TEMPLATE_NUMBER = 10;
FILENAME_OUTPUT = 'output/stabilizedBeach03';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);

%% Caso di Studio 24: Stabilizzazione con selezione di frame
% Caso in cui si osservano delle persone offuscate dalla nebbia con
% inquadratura che entra ed esce. Si analizzano le potenzialità di
% stabilizzazione e selezione dei frame di questo algoritmo. 
clear all
close all

load frames_beach08; % Persone all'orizzonte

% Input value
TOLERANCE = 0.01;
RESIZE_FACTOR = 0.6;
TEMPLATE_NUMBER = 30;
FILENAME_OUTPUT = 'output/stabilizedBeach08';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);

%% Caso di Studio 25: Stabilizzazione con selezione di frame
% In questo caso voglio capire se l'algoritmo di stabilizzazione e
% selezione riesce a capire quando la foca è fuori dall'acqua e quando
% invece ha eseguito l'immersione.
clear all
close all

load frames_beach19; % Immersione foca

% Input value
TOLERANCE = 0.30;
RESIZE_FACTOR = 0.6;
TEMPLATE_NUMBER = 10;
FILENAME_OUTPUT = 'output/stabilizedBeach19';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);

%% Caso di Studio 26: Stabilizzazione con selezione di frame
% Anche in questo caso, si vuole testare l'efficacia dell'algoritmo di
% stabilizzazione e di selezione dei frame in un caso in cui l'oggetto in
% questione entra ed esce dall'inquadratura. In questo esempio si tratta di
% un bambino che corre.
clear all
close all

load frames_ola02; % Bambino che entra e esce dall'inquadratura 

% Input value
TOLERANCE = 0.4;
RESIZE_FACTOR = 0.6;
TEMPLATE_NUMBER = 40;
FILENAME_OUTPUT = 'output/stabilizedOla02';

% Execution
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);

%% Caso di Studio 27: Stabilizzazione con selezione di frame
% In questo caso si vuole analizzare se l'algoritmo di stabilizzazione e di
% selezione dei frame riesce a capire quando l'oggetto selezionato è
% coperto da un altro oggetto ed escludere quei frames. In questo caso
% abbiamo due persone che corrono e si incrociano.
clear all
close all

load frames_tokyo01; % Persone che si incrociano

% Input value
TOLERANCE = 0.20;
RESIZE_FACTOR = 0.5;
TEMPLATE_NUMBER = 30;
FILENAME_OUTPUT = 'output/stabilizedTokyo01';

% Execution 
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);

%% Caso di Studio 28: Stabilizzazione con selezione di frame
% In questo caso si vuole analizzare se l'algoritmo di stabilizzazione e di
% selezione dei frame riesce a capire quando l'oggetto selezionato è
% coperto da un altro oggetto ed escludere quei frames. In questo caso
% c'è un uomo che corre e passa dietro ad un'altra persona.
clear all
close all

load frames_tokyo02; % Uomo che corre 

% Input value
TOLERANCE = 0.16;
RESIZE_FACTOR = 0.5;
TEMPLATE_NUMBER = 10;
FILENAME_OUTPUT = 'output/stabilizedTokyo02';

% Execution 
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);

%% Caso di Studio 29: Stabilizzazione con selezione di frame
% Questo caso è utile da analizzare perchè permette di capire come si
% comporta l'algoritmo di stabilizzazione con selezione di frame quando un
% oggetto piccolo si avvicina e diventa man mano grande. In questo esempio
% c'è un treno che arriva.
clear all
close all

load frames_train02; % Treno in arrivo

% Input value
TOLERANCE = 0.25;
RESIZE_FACTOR = 0.5;
TEMPLATE_NUMBER = 15;
FILENAME_OUTPUT = 'output/stabilizedTrain02';

% Execution 
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);

%% Caso di Studio 30: Stabilizzazione con selezione di frame
% Questo caso è utile da analizzare perchè permette di capire come si
% comporta l'algoritmo di stabilizzazione con selezione di frame quando un
% oggetto vicino, molto grande che man mano si allontana. In questo esempio
% c'è un treno che sta andando via.
clear all
close all

load frames_train03; % Partenza treno

% Input value
TOLERANCE = 0.10;
RESIZE_FACTOR = 0.5;
TEMPLATE_NUMBER = 30;
FILENAME_OUTPUT = 'output/stabilizedTrain03';

% Execution 
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);

%% Caso di Studio 31: Stabilizzazione con selezione di frame
% In questo caso si vuole osservare come si comporta l'algoritmo di
% stabilizzazione e di selezione di frame quando ci sono dei movimenti
% molto bruschi della telecamera. In questo esempio si osserva un ghiottone
% che corre.
clear all
close all

load frames_wolverine02; % Ghiottone corre

% Input value
TOLERANCE = 0.2;
RESIZE_FACTOR = 0.5;
TEMPLATE_NUMBER = 10;
FILENAME_OUTPUT = 'output/stabilizedWolverine02';

% Execution 
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);


%% Caso di Studio 32: Stabilizzazione con selezione di frame
% In questo caso si vuole osservare come si comporta l'algoritmo di
% stabilizzazione e selezione dei frame su un oggetto che si muove e che si
% nasconde dietro agli oggetti. In questo caso l'oggetto preso in analisi è
% un ghiottone che corre.
clear all
close all

load frames_wolverine03; % Ghiottone corre

% Input value
TOLERANCE = 0.2;
RESIZE_FACTOR = 0.5;
TEMPLATE_NUMBER = 35;
FILENAME_OUTPUT = 'output/stabilizedWolverine03';

% Execution 
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);

%% Caso di Studio 33: Stabilizzazione con selezione di frame
% Caso più difficile!
% In questo caso il video è molto mosso, di conseguenza la stabilizzazione
% dei frame diventa più difficile e necessita sicuramente di selezione dei
% frame. In questo esempio è in corso un terremoto con il crollo di una
% parte di chiesa. Si vuole stabilizzare sulla croce.
clear all
close all

load frames_Phil60-130; % Crollo chiesa

% Input value
TOLERANCE = 0.12; %0.15
RESIZE_FACTOR = 0.3;
TEMPLATE_NUMBER = 25;
FILENAME_OUTPUT = 'output/stabilizedPhil2';

% Execution 
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);

%% Caso di Studio 34: Stabilizzazione con selezione di frame
% Caso più difficile!
% In questo caso il video non è molto mosso, tuttavia c'è una macchina che
% si muove, anche lentamente, nascosta in parte da molti oggetti. Questo la
% fa cambiare di forma ed è molto difficile identificarla. Si analizza fino
% a che punto l'algoritmo di stabilizzazione riesce a vedere la macchina.
clear all
close all

load frames_beach02; % Auto che passa

% Input value
TOLERANCE = 0.20;
RESIZE_FACTOR = 0.8;
TEMPLATE_NUMBER = 25;
FILENAME_OUTPUT = 'output/stabilizedBeach02';

% Execution 
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);

%% Caso di Studio 35: Stabilizzazione con selezione di frame
% Caso più difficile!
% Anche in questo caso il video non è mosso, ma vengono ripresi degli
% uccelli in volo, il che rende molto difficile la stabilizzazione dato il
% fatto che volando gli uccelli cambiano di forma.
clear all
close all

load frames_beach11; % Uccelli che volano

% Input value
TOLERANCE = 0.3; 
RESIZE_FACTOR = 0.8;
TEMPLATE_NUMBER = 46;
FILENAME_OUTPUT = 'output/stabilizedBeach11';

% Execution 
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);


%% Caso di Studio 36: Stabilizzazione con selezione di frame
% Caso più difficile!
% Anche in questo caso il video non è mosso, ma vengono ripresi degli
% uccelli in volo, il che rende molto difficile la stabilizzazione dato il
% fatto che volando gli uccelli cambiano di forma.
clear all
close all

load frames_beach12; % Altri uccelli che volano

% Input value
TOLERANCE = 0.30;
RESIZE_FACTOR = 0.8;
TEMPLATE_NUMBER = 30;
FILENAME_OUTPUT = 'output/stabilizedBeach12';

% Execution 
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);


%% Caso di Studio 37: Stabilizzazione con selezione di frame
% Caso più difficile!
% Questo è un caso di studio in cui la stabilizzazione con selezione di
% frame risulta molto efficace, poichè viene preso in esame un oggetto che
% rimane più o meno sempre lo stesso e risulta molto visibile, ma che poi
% ci passano sopra degli uccelli che volano.
clear all
close all

load frames_beach13; % Uccello su oggetto

% Input value
TOLERANCE = 0.20; 
RESIZE_FACTOR = 0.8;
TEMPLATE_NUMBER = 15;
FILENAME_OUTPUT = 'output/stabilizedBeach13';

% Execution 
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);


%% Caso di Studio 38: Stabilizzazione con selezione di frame
% Caso più difficile!
% In questo caso viene testato l'algoritmo di stabilizzazione e di selezione
% di frame su una spiaggia che però viene ripresa ruotano un po' anche la
% telecamera. Si vuole capire fino a che punto la stabilizzazione riesce a
% tenere questa rotazione.
clear all
close all

load frames_beach14; % Orizzonte obliquo

% Input value
TOLERANCE = 0.05; 
RESIZE_FACTOR = 0.5;
TEMPLATE_NUMBER = 25;
FILENAME_OUTPUT = 'output/stabilizedBeach14';

% Execution 
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);

%% Caso di Studio 39: Stabilizzazione con selezione di frame
% Caso più difficile!
% In questo caso il video non è mosso, ma vengono ripresi degli
% uccelli in volo, il che rende molto difficile la stabilizzazione dato il
% fatto che volando gli uccelli cambiano di forma e anche lo sfondo dietro 
% è mutevole.
clear all
close all

load frames_beach16; % Gabbiano che vola su mare

% Input value
TOLERANCE = 0.40; 
RESIZE_FACTOR = 1;
TEMPLATE_NUMBER = 43;
FILENAME_OUTPUT = 'output/stabilizedBeach16';

% Execution 
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);

%% Caso di Studio 40: Stabilizzazione con selezione di frame
% Caso più difficile!
% Anche in questo caso il video non è mosso, ma viene riperesa una bandiera
% scossa da un forte vento, il che rende molto difficile la stabilizzazione 
% dato che la bandiera cambia di forma.
clear all
close all

load frames_beach17; % Bandiera

% Input value
TOLERANCE = 0.60; 
RESIZE_FACTOR = 0.8;
TEMPLATE_NUMBER = 30;
FILENAME_OUTPUT = 'output/stabilizedBeach17';

% Execution 
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);


%% Caso di Studio 41: Stabilizzazione con selezione di frame
% Caso più difficile!
% Anche in questo caso il video non è mosso, ma viene ripreso un
% uccello in volo, il che rende molto difficile la stabilizzazione dato il
% fatto che volando gli uccelli cambiano di forma.
clear all
close all

load frames_beach18; % Gabbiano in volo 2

% Input value
TOLERANCE = 0.45;
RESIZE_FACTOR = 0.8;
TEMPLATE_NUMBER = 32;
FILENAME_OUTPUT = 'output/stabilizedBeach18';

% Execution 
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);


%% Caso di Studio 42: Stabilizzazione con selezione di frame
% Caso più difficile!
% Anche in questo caso il video non è mosso, ma viene ripreso un
% uccello in volo, il che rende molto difficile la stabilizzazione dato il
% fatto che volando gli uccelli cambiano di forma.
clear all
close all

load frames_beach20; % Gabbiano in volo 3

% Input value
TOLERANCE = 0.5;
RESIZE_FACTOR = 0.8;
TEMPLATE_NUMBER = 25;
FILENAME_OUTPUT = 'output/stabilizedBeach20';

% Execution 
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);


%% Caso di Studio 43: Stabilizzazione con selezione di frame
% Caso più difficile!
% Anche in questo caso il video non è mosso, ma viene ripreso un
% uccello in volo, il che rende molto difficile la stabilizzazione dato il
% fatto che volando gli uccelli cambiano di forma.
clear all
close all

load frames_beach21; % Gabbiano in volo 4

% Input value
TOLERANCE = 0.35;
RESIZE_FACTOR = 1;
TEMPLATE_NUMBER = 35;
FILENAME_OUTPUT = 'output/stabilizedBeach21';

% Execution 
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);

%% Caso di Studio 44: Stabilizzazione con selezione di frame
% Caso più difficile!
% Anche in questo caso il video non è mosso, ma viene ripreso un
% uccello in volo, il che rende molto difficile la stabilizzazione dato il
% fatto che volando gli uccelli cambiano di forma.
clear all
close all

load frames_beach23; % Uccello strano in volo

% Input value
TOLERANCE = 0.25;
RESIZE_FACTOR = 0.8;
TEMPLATE_NUMBER = 21;
FILENAME_OUTPUT = 'output/stabilizedBeach23';

% Execution 
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);


%% Caso di Studio 45: Stabilizzazione con selezione di frame
% Caso più difficile!
% Anche in questo caso il video non è mosso, ma viene ripreso un
% uccello in volo, il che rende molto difficile la stabilizzazione dato il
% fatto che volando gli uccelli cambiano di forma.
clear all
close all

load frames_beach24; % Uccello in volo 5

% Input value
TOLERANCE = 0.3;
RESIZE_FACTOR = 0.8;
TEMPLATE_NUMBER = 25;
FILENAME_OUTPUT = 'output/stabilizedBeach24';

% Execution 
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);

%% Caso di Studio 46: Stabilizzazione con selezione di frame
% Caso più difficile!
% In questo caso si osserva una ragazza che cammina. Con il movimento delle
% braccia di una persona risulta molto difficile stabilizzarne il video. Si
% analizza per quali template l'algoritmo di stabilizzazione e selezione
% dei frame risulta efficace.
clear all
close all

load frames_ola03; % Ragazza che passa

% Input value
TOLERANCE = 0.2;
RESIZE_FACTOR = 0.5;
TEMPLATE_NUMBER = 40;
FILENAME_OUTPUT = 'output/stabilizedOla03';

% Execution 
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);

%% Caso di Studio 47: Stabilizzazione con selezione di frame
% Caso più difficile!
% In questo caso ci sono più ragazzi in lontanaza che giocano. Si vuole
% analizzare fino a che punto la stabilizzazione e selezione di frame
% riesce a riconoscere la singola persona in movimento e se la scambia con
% l'altra.
clear all
close all

load frames_ola05; % Bambini che corrono

% Input value
TOLERANCE = 0.40;
RESIZE_FACTOR = 0.8;
TEMPLATE_NUMBER = 30;
FILENAME_OUTPUT = 'output/stabilizedOla05';

% Execution 
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);

%% Caso di Studio 48: Stabilizzazione con selezione di frame
% Caso più difficile!
% Qui invece si vuole osservare un ghiottone che corre e che passa
% attraverso una spiepe. Si vuole analizzare se l'algoritmo di
% stabilizzazione e selezione di frame riesce a distinguere l'istante
% precedente da quello successivo l'attraversamento della siepe.
clear all
close all

load frames_wolverine04; % Ghiottone attraverso siepe

% Input value
TOLERANCE = 0.20;
RESIZE_FACTOR = 0.4;
TEMPLATE_NUMBER = 38;
FILENAME_OUTPUT = 'output/stabilizedWolverine04';

% Execution 
frames = resizeVideo(frames, RESIZE_FACTOR);
[new, ~, result] = showOptimizedStabilization(frames, TEMPLATE_NUMBER, TOLERANCE);
write3Video(frames, new, result, FILENAME_OUTPUT);

%%

