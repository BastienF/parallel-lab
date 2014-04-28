#!/bin/bash

if [[ -z "$1" ]]; then
	echo "[ERROR] No scenario defined"
	exit 1
fi

JAVA_VERSION=7
VM_SERVER=servero$JAVA_VERSION
LANGUAGE=JAVA7

vagrant up gatlingo7 > vagrant.log
vagrant up $VM_SERVER >> vagrant.log

server=$2
scenario=$1

if [[ -z "$3" ]]; then
  nbThreads=6
else
  nbThreads=$3
fi

if [ "$server" != "tomcat" ] && [ "$server" != "jetty" ] && [ "$server" != "httpcore" ]; then 
	server="tomcat"
fi

launchDate="$LANGUAGE-$server-nbThreads$nbThreads-`date '+%d-%m-%Y.%T'`"

echo "Server setted at" $server
ansible-playbook -i hosts -l $VM_SERVER deploy_webapp.yml --private-key=~/.vagrant.d/insecure_private_key --extra-vars 'server='$server
if [ "$?" != "0" ]; then
	echo "[ERROR] webapp deployement failed !"
	exit 2;
fi
launch () {
	      date
        echo "Run with iterations=$3 implementation=$1 users=$2 launchDate=$launchDate duration=$4 server=$server scenario=$scenario"
        ansible-playbook -i hosts -l $VM_SERVER run_once.yml --private-key=~/.vagrant.d/insecure_private_key --extra-vars 'iterations='$3' implementation='$1' users='$2' launchDate='$launchDate' duration='$4' server='$server' scenario='$scenario' nbThreads='$nbThreads
        if [ "$?" = "0" ]; then
        	ansible-playbook -i hosts -l gatling run_once.yml --private-key=~/.vagrant.d/insecure_private_key --extra-vars 'iterations='$3' implementation='$1' users='$2' launchDate='$launchDate' duration='$4' server='$server' scenario='$scenario
        else
        	echo "[ERROR] run_once of server failed !"
        fi
}

launch_all() {
        #launch mock $1 $2 $3;
        launch akka $1 $2 $3;
        #launch naive $1 $2 $3;
       	#launch mono $1 $2 $3;
       	#launch executor $1 $2 $3;
       	#launch pool $1 $2 $3;
}
first=false
while IFS=';' read users iter dur
do 
if [ $first == false ]; then
	first=true;
else
	launch_all $users $iter $dur
fi
done < "scenarios/$scenario/scenario.csv"
vagrant halt >> vagrant.log;