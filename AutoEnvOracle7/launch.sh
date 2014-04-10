#!/bin/bash

if [[ -z "$1" ]]; then
	echo "[ERROR] No scenario defined"
	exit 1
fi

vagrant up > vagrant.log
launchDate=`date '+%d-%m-%Y.%T'`
server=$2
scenario=$1

if [ "$server" != "tomcat" ] && [ "$server" != "jetty" ] && [ "$server" != "httpcore" ]; then 
	server="tomcat"
fi

echo "Server setted at" $server "<br/>"
ansible-playbook -i hosts -l server deploy_webapp.yml --private-key=~/.vagrant.d/insecure_private_key --extra-vars 'server='$server
if [ "$?" != "0" ]; then
	echo "[ERROR] webapp deployement failed !"
	exit 2;
fi
launch () {
	      date
        echo "Run with iterations=$3 implementation=$1 users=$2 launchDate=$launchDate duration=$4 server=$server scenario=$scenario"
        ansible-playbook -i hosts -l server run_once.yml --private-key=~/.vagrant.d/insecure_private_key --extra-vars 'iterations='$3' implementation='$1' users='$2' launchDate='$launchDate' duration='$4' server='$server' scenario='$scenario
        if [ "$?" = "0" ]; then
        	ansible-playbook -i hosts -l gatling run_once.yml --private-key=~/.vagrant.d/insecure_private_key --extra-vars 'iterations='$3' implementation='$1' users='$2' launchDate='$launchDate' duration='$4' server='$server' scenario='$scenario
        else
        	echo "[ERROR] run_once of server failed !"
        fi
}

launch_all() {
        launch akka $1 $2 $3;
        launch naive $1 $2 $3;
       	launch mono $1 $2 $3;
       	launch executor $1 $2 $3;
       	launch pool $1 $2 $3;
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