#!/bin/bash

echo "Are you sure you want to run crawl.sh, this will restart entire crawl"
read check

if [ "$check" == "y" ] || [ "$check" == "yes" ]; then
    echo "you entered yes"
    sleep 5
    
    mvn install
    rm nohup.out
    rm -rf CrawlerData frontier
    nice -19 nohup java -classpath target/test-classes:dep.jar:target/classes/:slf4j.jar edu.uci.ics.crawler4j.hw.TheController . 12 1 &
    sleep 2
    tail -f nohup.out
else
    echo "You entered no"
fi
