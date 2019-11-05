#MakeFile for key_finder
#MakeFile's name: makefile
NAME:= key_finder
PROGRAM:= $(NAME)
BUILDDIR:= ./build
SRCDIR:= ./src

LINKFLAGS:= -pthread
CFLAGS:= -c

#object files
SRC:= $(SRCDIR)/main.c $(SRCDIR)/padre.c $(SRCDIR)/logger.c $(SRCDIR)/figlio.c $(SRCDIR)/nipote.c $(SRCDIR)/mylib.c
OBJS:= $(SRC:$(SRCDIR)%.c=$(BUILDDIR)%.o)
SRCTHREAD:= $(SRCDIR)/main.c $(SRCDIR)/padre.c $(SRCDIR)/logger.c $(SRCDIR)/figlio_thread.c $(SRCDIR)/nipote.c $(SRCDIR)/mylib.c
OBJSTHREAD:= $(SRCTHREAD:$(SRCDIR)%.c=$(BUILDDIR)%.o)


all: $(BUILDDIR)/$(PROGRAM)

$(BUILDDIR)/$(PROGRAM): $(OBJS)
	@echo Linking $^
	$(CC) $(LINKFLAGS) -o $@ $^

threads: $(BUILDDIR)/$(PROGRAM)_thread

$(BUILDDIR)/$(PROGRAM)_thread: $(OBJSTHREAD)
	@echo Linking $^
	$(CC) $(LINKFLAGS) -o $@ $^

$(BUILDDIR)/%.o: $(SRCDIR)/%.c
	@echo Compiling $^
	$(CC) $(CFLAGS) -o $@ $^

clean:
	@echo Removing $(BUILDDIR)/*.o and $(BUILDDIR)/$(PROGRAM)*
	@rm $(BUILDDIR)/*.o
	@rm $(BUILDDIR)/$(PROGRAM)*

install: $(BUILDDIR)/$(PROGRAM)
	@echo Installing $(PROGRAM)
	@cp $(BUILDDIR)/$(PROGRAM) /bin
	
installthreads: $(BUILDDIR)/$(PROGRAM)_thread
	@echo Installing $(PROGRAM)_thread
	@cp $(BUILDDIR)/$(PROGRAM)_thread /bin

help:
	@echo Target:
	@echo "all           - Compila tutti i target"
	@echo "clean         - Pulisce i file intermedi e l'eseguibile"
	@echo "install       - Copia l'eseguibile nella cartella bin"
	@echo "installthread - Copia l'eseguibile con thread nella cartella bin"
	@echo "help          - Mostra i comandi utilizzabili"
	@echo "doc           - Genera la documentazione"
	@echo "openhtml      - Apre HTML generato dalla documentazione"
	@echo "thread        - Compila tutti i target con thread"

doc:
	@echo Generating documentation:
	doxygen doxygen.cfg

openhtml:
	@echo Opening html:
	firefox ./doc/html/index.html &


.PHONY: all clean install help doc openhtml threads installthreads
