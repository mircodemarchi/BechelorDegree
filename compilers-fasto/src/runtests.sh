#!/bin/sh
#
# Compile the compiler and run the test suite.
#
# Use -o to optimise the test programs before compiling them.
# Use -i to interpret the test programs instead of compiling them.
#
# Test programs with no corresponding .in files are expected to fail
# at compile-time.  The error message is not controlled.  This script
# should be run from the directory containing the compiler sources,
# but can be run from anywhere, as long as you change RESDIR,
# COMPILER and MARS below.
#
# Written by Troels Henriksen  <athas@sigkill.dk>.
# Modified by Rasmus Wriedt Larsen to work under Cywin

set -e # Die on first error.

IS_CYGWIN=""
if [ "$(uname -o 2> /dev/null)" = "Cygwin" ]; then
    IS_CYGWIN="true"
fi

# RESDIR is the path at which test programs can be found.
RESDIR=../tests

# COMPILER is the command to run the compiler
COMPILER=../bin/fasto

# MARS is the command to run Mars.
# can be set e.g. $>MARS=/path/to/my/Mars4_5.jar make test
if [ -z "$MARS" ]; then
    MARS="$HOME/Mars*.jar"

    # In cygwin we need to use a "real" windows path for the jar file
    if [ $IS_CYGWIN ]; then
        MARS=$(cygpath -w $MARS)
    fi
fi

if [ ! -r $MARS ]; then
    echo "Could not find Mars, either put it in your home folder or set it manually"
    exit 1
fi

# Verify java is installed
java -version &> /dev/null || (echo "Could not find java" && exit 1)

RUNMARS="java -jar $MARS nc"

if [ "$1" = -o ]; then
    flags=-o
    shift
elif [ "$1" = -i ]; then
    flags=''
    shift
else
    flags=-c
fi

if [ "$flags" ]; then
    compile="$COMPILER $flags"
else
    compile=''
fi

# Remove all whitespace when comparing results, because Mars
# and the interpreter puts different amounts -- and to handle
# Windows/OSX/Unix line ending differences.
fix_whitespace() {
    tr -d ' \t\n\r\f' < $1 1>&1
}

check_equal() {
    if [ -f $RESDIR/$OUTPUT ]; then

        EXCEPTED=$(fix_whitespace "$RESDIR/$OUTPUT")
        ACTUAL=$(fix_whitespace "$TESTOUT")
        if [ "$EXCEPTED" = "$ACTUAL" ]; then
            rm -f $TESTOUT
        else
            echo "Output for $PROG does not match expected output."
            echo "Compare $TESTOUT and $RESDIR/$OUTPUT."
        fi
    fi
}

if [ $IS_CYGWIN ]; then
    ./make.bat
elif [ ! "$FROMMAKE" ]; then
    make
fi

for FO in $RESDIR/*fo; do
    FO=$(basename "$FO")
    PROG=$(echo $FO|sed 's/.fo$//')
    INPUT=$(echo $FO|sed 's/fo$/in/')
    OUTPUT=$(echo $FO|sed 's/fo$/out/')
    ERROUT=$(echo $FO|sed 's/fo$/err/')
    ASM=$(echo $FO|sed 's/fo$/asm/')
    TESTOUT=$RESDIR/$OUTPUT-testresult

    if [ -f $RESDIR/$INPUT ]; then
        # Is positive test
        echo "Testing $FO:"
        if [ "$compile" ]; then
            # Compile
            $compile $RESDIR/$PROG || true
            if [ -f $RESDIR/$ASM ]; then
                touch $RESDIR/$INPUT
                $RUNMARS $RESDIR/$ASM < $RESDIR/$INPUT > $TESTOUT 2>/dev/null
                check_equal
            fi
        else
            # Interpret
            touch $RESDIR/$INPUT
            ($COMPILER -r $RESDIR/$PROG || true) < $RESDIR/$INPUT > $TESTOUT 2>&1
            check_equal
        fi
    else
        # Is negative test
        echo "Testing $FO (expecting compile failure):"
        if ! [ "$compile" ]; then
            compile="$COMPILER -c"
        fi
        if $compile $RESDIR/$PROG > $TESTOUT 2>&1; then
            echo "$PROG compiled, but should result in compile error."
        fi
        if [ -f $RESDIR/$ERROUT ]; then

            EXCEPTED=$(fix_whitespace $RESDIR/$ERROUT)
            ACTUAL=$(fix_whitespace $TESTOUT)
            if [ "$EXCEPTED" = "$ACTUAL" ]; then
                rm -f $TESTOUT
            else
                echo "Error message for $PROG does not match expected."
                echo "Compare $TESTOUT and $RESDIR/$ERROUT."
            fi
        fi
    fi
done
