#!/bin/bash

rm -rf target
mkdir target
cd src
javac -Xlint -cp .:../dep.jar edu/uci/ics/crawler4j/hw/*.java -d ../target
cd ..

