#!/bin/bash

mkdir target
cd src
javac -cp .:../dep.jar edu/uci/ics/crawler4j/hw/*.java -d ../target
cd ..

