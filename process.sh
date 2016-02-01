#!/bin/bash

java -classpath target/test-classes:dep.jar:target/classes/:slf4j.jar edu.uci.ics.crawler4j.hw.ProcessData .
