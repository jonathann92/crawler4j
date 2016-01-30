#!/bin/bash

mvn install
nice -19 nohup java -classpath target/test-classes:dep.jar:target/classes/:slf4j.jar edu.uci.ics.crawler4j.hw.TheController . 6 1 &
tail -f nohup.out
