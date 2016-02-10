#!/bin/bash


nice -19 nohup java -Xmx16g -classpath target:dep.jar:slf4j.jar edu.uci.ics.crawler4j.hw.TheController . 9 1 &
tail -f nohup.out
