#!/bin/bash
JAR="/opt/tokenidm/tokenidm-0.1.00-jar-with-dependencies.jar"
KILLPATTERN="/opt/tokenidm/tokenidm-"
LOGCFG="file:/opt/tokenidm/log4j.properties"
CONFIG="./tokenidm.config"
NOW=$(date +"%F-%H%M%S")
TOKENIDMSTDOUT=/var/log/tokenidm/tokenidm-stdout-$NOW.log
TOKENIDMLOG=/var/log/tokenidm/tokenidm.log

if [ $# -lt 1 ]
then
	echo "tokenidm.sh start | stop" 
        exit 0
fi

if [ $1 = "start" ]
then
	echo "starting tokenidm ..."
        RPID=$(ps -ef | grep $KILLPATTERN | grep java | awk '{print $2}' | head -1)
	if [ -z $RPID ]
	then
		echo "application     :" $JAR
		echo "logging cfg     :" $LOGCFG
		echo "tokenidm stdout :" $TOKENIDMSTDOUT
		echo "tokenidm log    :" $TOKENIDMLOG
		java -Dlog4j.configuration=$LOGCFG -jar $JAR $CONFIG > $TOKENIDMSTDOUT 2>&1 & 
		echo "started"
		exit 0
	else
		echo "tokenidm already running! " 
		exit 0
	fi
fi

if [ $1 = "stop" ]
then
	echo "stopping tokenidm ..."
	RPID=$(ps -ef | grep $KILLPATTERN | grep java | awk '{print $2}' | head -1)
	if [ -z $RPID ]
	then
		echo "tokenidm does not appear to be running!"
		exit 0
	fi
	echo "stopping pid " $RPID
	kill $RPID
	echo "stopped"
	exit 0
fi

echo "tokenidm unknown option" $1
exit 0 
