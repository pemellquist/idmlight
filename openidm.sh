#!/bin/bash
JAR="/opt/openidm/openidm-0.1.00-jar-with-dependencies.jar"
KILLPATTERN="/opt/openidm/openidm-"
LOGCFG="file:/opt/openidm/log4j.properties"
CONFIG="./idmlight.config"
NOW=$(date +"%F-%H%M%S")
OPENIDMSTDOUT=/var/log/openidm/openidm-stdout-$NOW.log
OPENIDMLOG=/var/log/openidm/openidm.log

if [ $# -lt 1 ]
then
	echo "openidm.sh start | stop" 
        exit 0
fi

if [ $1 = "start" ]
then
	echo "starting openidm ..."
        RPID=$(ps -ef | grep $KILLPATTERN | grep java | awk '{print $2}' | head -1)
	if [ -z $RPID ]
	then
		echo "application     :" $JAR
		echo "logging cfg     :" $LOGCFG
		echo "openidm stdout  :" $OPENIDMSTDOUT
		echo "openidm log     :" $OPENIDMLOG
		java -Dlog4j.configuration=$LOGCFG -jar $JAR $CONFIG > $OPENIDMSTDOUT 2>&1 & 
		echo "started"
		exit 0
	else
		echo "openidm already running! " 
		exit 0
	fi
fi

if [ $1 = "stop" ]
then
	echo "stopping openidm ..."
	RPID=$(ps -ef | grep $KILLPATTERN | grep java | awk '{print $2}' | head -1)
	if [ -z $RPID ]
	then
		echo "openidm does not appear to be running!"
		exit 0
	fi
	echo "stopping pid " $RPID
	kill $RPID
	echo "stopped"
	exit 0
fi

echo "openidm unknown option" $1
exit 0 
