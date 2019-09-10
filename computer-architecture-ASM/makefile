EXE= controller
GCC= gcc
FLAG_GCC_compiler= -c -m32
FLAG_GCC_linker= -m32
OBJ= controller.o ciclocontrollore_asm.o controllore_asm.o ph2num_asm.o duecifre2str_asm.o

$(EXE): $(OBJ)
	$(GCC) $(FLAG_GCC_linker) -o $(EXE) $(OBJ)
controller.o: controller.c
	$(GCC) $(FLAG_GCC_compiler) -o controller.o controller.c
ciclocontrollore_asm.o: ciclocontrollore_asm.s
	$(GCC) $(FLAG_GCC_compiler) -o ciclocontrollore_asm.o ciclocontrollore_asm.s
controllore_asm.o: controllore_asm.s
	$(GCC) $(FLAG_GCC_compiler) -o controllore_asm.o controllore_asm.s
ph2num_asm.o: ph2num_asm.s
	$(GCC) $(FLAG_GCC_compiler) -o ph2num_asm.o ph2num_asm.s
duecifre2str_asm.o: duecifre2str_asm.s
	$(GCC) $(FLAG_GCC_compiler) -o duecifre2str_asm.o duecifre2str_asm.s
clean:
	rm -f *.o $(EXE) core
