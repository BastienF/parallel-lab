#!/bin/bash 

vagrant up > vagrant.log
launchDate=`date '+%d-%m-%Y.%T'`
server=$1
if [ "$server" != "tomcat" ] && [ "$server" != "jetty" ]; then 
	server="tomcat"
fi
echo "Server setted at" $server
ansible-playbook -i hosts -l server deploy_webapp.yml --private-key=~/.vagrant.d/insecure_private_key --extra-vars 'server='$server
launch () {
        ansible-playbook -i hosts -l server run_once.yml --private-key=~/.vagrant.d/insecure_private_key --extra-vars 'iterations='$3' implementation='$1' users='$2' launchDate='$launchDate' duration='$4' server='$server
        ansible-playbook -i hosts -l gatling run_once.yml --private-key=~/.vagrant.d/insecure_private_key --extra-vars 'iterations='$3' implementation='$1' users='$2' launchDate='$launchDate' duration='$4' server='$server
}

launch_all() {
        launch akka $1 $2 $3;
        launch naive $1 $2 $3;
       	launch mono $1 $2 $3;
       	launch executor $1 $2 $3;
       	launch pool $1 $2 $3;
}

launch_all 15 5000000 5;
vagrant halt >> vagrant.log;