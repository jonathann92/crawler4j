#!/bin/bash

mvn install
rm nohup.out
rm -rf CrawlerData frontier
nice -10 nohup java -classpath target/test-classes:dep.jar:target/classes/:slf4j.jar edu.uci.ics.crawler4j.hw.TheController . 12 1 &
sleep 2
tail -f nohup.out
