#MakeFile for key_finder
#MakeFile's name: makefile
NAME:= key_finder
PROGRAM:= $(NAME)
BINDIR:= ./bin
BUILDDIR:= ./build
SRCDIR:= ./src

LINKFLAGS:=
CFLAGS:= -c

#object files
SRC:= $(SRCDIR)/main.c $(SRCDIR)/padre.c $(SRCDIR)/logger.c $(SRCDIR)/figlio.c $(SRCDIR)/nipote.c $(SRCDIR)/mylib.c
OBJS:= $(SRC:$(SRCDIR)%.c=$(BUILDDIR)%.o)
SRCTHREAD:= $(SRCDIR)/main.c $(SRCDIR)/padre.c $(SRCDIR)/logger.c $(SRCDIR)/figlio_thread.c $(SRCDIR)/nipote.c $(SRCDIR)/mylib.c
OBJSTHREAD:= $(SRCTHREAD:$(SRCDIR)%.c=$(BUILDDIR)%.o)


all: $(BINDIR)/$(PROGRAM)

$(BINDIR)/$(PROGRAM): $(OBJS)
	@echo Linking $^
	$(CC) $(LINKFLAGS) -o $@ $^

threads: $(BINDIR)/$(PROGRAM)_thread

$(BINDIR)/$(PROGRAM)_thread: $(OBJSTHREAD)
	@echo Linking $^
	$(CC) $(LINKFLAGS) -o $@ $^

$(BUILDDIR)/%.o: $(SRCDIR)/%.c
	@echo Compiling $^
	$(CC) $(CFLAGS) -o $@ $^

clean:
	@echo Removing $(BUILDDIR)/*.o and $(BINDIR)/*
	@rm $(BUILDDIR)/*.o
	@rm $(BINDIR)/*

install: $(BINDIR)/$(PROGRAM)
	@echo Installing $(PROGRAM)
	@cp $(BINDIR)/$(PROGRAM) /bin

help:
	@echo Target:
	@echo all
	@echo clean
	@echo install
	@echo help
	@echo doc
	@echo thread
	@echo threadclean

doc:
	@echo Generating documentation:


.PHONY: all clean install help doc threads
