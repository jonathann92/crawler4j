#!/bin/bash

echo "Are you sure you want to run crawl.sh, this will restart entire crawl"
read check

if [ "$check" == "y" ] || [ "$check" == "yes" ]; then
    echo "you entered yes"
    sleep 1
    
    rm -rf target
    mkdir target

    cd src
    pwd
    javac -cp .:../dep.jar edu/uci/ics/crawler4j/hw/*.java -d ../target
    cd ..

    rm -rf CrawlerData frontier
    echo "START" > nohup.out
    nice -19 nohup java -Xmx16g -classpath target:dep.jar:slf4j.jar edu/uci/ics/crawler4j/hw/TheController . 9 1 &
    tail -f nohup.out
else
    echo "You entered no"
fi

