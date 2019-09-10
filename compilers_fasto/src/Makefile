all: make
	./make

clean: make
	./make clean
	rm make

help: make
	./make help

make: Make.sml
	mosmlc -o make Make.sml

.PHONY: test testoptimised testinterpreted
test: all
	FROMMAKE=1 ./runtests.sh
testoptimised: all
	FROMMAKE=1 ./runtests.sh -o
testinterpreted: all
	FROMMAKE=1 ./runtests.sh -i
