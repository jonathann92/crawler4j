#!/bin/bash

nice -19 nohup java -classpath target/test-classes:dep.jar:target/classes/:slf4j.jar edu.uci.ics.crawler4j.hw.TheController . 12 1 &
tail -f nohup.out
