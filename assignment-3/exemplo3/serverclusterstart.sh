#!/bin/sh
kill $(ps ax |grep "java -jar HttpTrickyServer.jar [0-9]*" |grep -v "grep"|awk '{print $1}')
sleep 1
echo "HttpTrickyServer 0"
java -jar HttpTrickyServer.jar 8080 &
echo "HttpTrickyServer 1"
java -jar HttpTrickyServer.jar 8081 &
echo "HttpTrickyServer 2"
java -jar HttpTrickyServer.jar 8082 &
echo "HttpTrickyServer 3"
java -jar HttpTrickyServer.jar 8083 &
