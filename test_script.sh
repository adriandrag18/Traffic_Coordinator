#!/bin/bash

FOLDERS=../tests/*
HW_PATH=com/apd/tema2/Main

#make && make run

mkdir out err
cd src
javac -g $HW_PATH.java -d bin

for d in $FOLDERS
do	
	for f in $d/*
	do
    		echo "Processing $f file..."
    		
		fullpath=`echo "${f%.*}"`
		filename="${fullpath##*/}"

		java -cp bin/ $HW_PATH $f > ../out/$filename.out

	done
done

cd ..
java -jar ./Tema2Checker.jar
