#!/bin/bash
make compile
for i in {1..100}:
do
    sed '/^prefix/ d' test.properties > temp.properties
    rm -f test.properties
    mv temp.properties test.properties
    echo "prefix=load$i-"  >> test.properties
    echo "Running load with prefix load$i"
    make run
done
