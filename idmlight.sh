#!/bin/bash
JAR="/opt/idmlight/openidm-0.1.00-jar-with-dependencies.jar"
KILLPATTERN="/opt/idmlight/openidm-"
LOGCFG="file:/opt/idmlight/log4j.properties"
CONFIG="./idmlight.config"
NOW=$(date +"%F-%H%M%S")
IDMSTDOUT=/var/log/idmlight/idmlight-stdout-$NOW.log
IDMLOG=/var/log/openidm/idmlight.log

if [ $# -lt 1 ]
then
	echo "idmlight.sh start | stop" 
        exit 0
fi

if [ $1 = "start" ]
then
	echo "starting idmlight ..."
        RPID=$(ps -ef | grep $KILLPATTERN | grep java | awk '{print $2}' | head -1)
	if [ -z $RPID ]
	then
		echo "application     :" $JAR
		echo "logging cfg     :" $LOGCFG
		echo "openidm stdout  :" $IDMSTDOUT
		echo "openidm log     :" $IDMLOG
		java -Dlog4j.configuration=$LOGCFG -jar $JAR $CONFIG > $IDMSTDOUT 2>&1 & 
		echo "started"
		exit 0
	else
		echo "idmlight already running! " 
		exit 0
	fi
fi

if [ $1 = "stop" ]
then
	echo "stopping idmlight ..."
	RPID=$(ps -ef | grep $KILLPATTERN | grep java | awk '{print $2}' | head -1)
	if [ -z $RPID ]
	then
		echo "idmlight does not appear to be running!"
		exit 0
	fi
	echo "stopping pid " $RPID
	kill $RPID
	echo "stopped"
	exit 0
fi

echo "idmlight unknown option" $1
exit 0 
