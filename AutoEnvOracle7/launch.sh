#!/bin/bash

date=`date +%s`

while [ $# -gt 0 ]
do
    case "$1" in
  --scenario) scenario="$2"; shift;;
  --server) server="$2"; shift;;
  --nbThreads) nbThreads="$2"; shift;;
  --use-aws) aws=true;;
  -*) echo >&2 \
      "wrong var: usage: $0 --scenario scenario [--server server] [--nbThreads nbThreads] [--use-aws]"
      exit 1;;
  *)  break;; # terminate while loop
    esac
    shift
done

if [ -z "$scenario" ]; then
  echo >&2 \
      "not enougth vars: usage: $0 --scenario scenario [--server server] [--nbThreads nbThreads] [--use-aws]"
  exit 1
fi

if [ -z "$aws" ]; then
  aws=false
fi

JAVA_VERSION=7
VM_SERVER=servero$JAVA_VERSION
LANGUAGE=JAVA7

if [ "$aws" = true ] ; then
  echo 'You have to have run start_aws.sh'
  HOSTS=tmp/hosts_aws
  PKEY=provisioning/parallelLab.pem
  distant_user='ubuntu'
else
  vagrant up gatlingo7 > vagrant.log
  vagrant up $VM_SERVER >> vagrant.log 
  HOSTS=hosts
  PKEY=~/.vagrant.d/insecure_private_key
  distant_user='vagrant'
fi

if [ -z "$nbThreads" ]; then
  nbThreads=200
fi

if [ -z "$server" ]; then 
	server="tomcat"
fi

launchDate="$LANGUAGE-$server-nbThreads$nbThreads-`date '+%d-%m-%Y.%T'`"
if [ "$aws" = true ] ; then
    launchDate="AWS-$launchDate"
fi


echo "Server setted at" $server
ansible-playbook -i $HOSTS -l $VM_SERVER deploy_webapp.yml --private-key=$PKEY --extra-vars 'server='$server' distant_user='$distant_user
if [ "$?" != "0" ]; then
	echo "[ERROR] webapp deployement failed !"
	exit 2;
fi
launch () {
	      date
        echo "Run with iterations=$3 implementation=$1 users=$2 launchDate=$launchDate duration=$4 server=$server scenario=$scenario"
        ansible-playbook -i $HOSTS -l $VM_SERVER run_once.yml --private-key=$PKEY --extra-vars 'iterations='$3' implementation='$1' users='$2' launchDate='$launchDate' duration='$4' server='$server' scenario='$scenario' nbThreads='$nbThreads' distant_user='$distant_user' aws='$aws
        if [ "$?" = "0" ]; then
        	ansible-playbook -i $HOSTS -l gatling run_once.yml --private-key=$PKEY --extra-vars 'iterations='$3' implementation='$1' users='$2' launchDate='$launchDate' duration='$4' server='$server' scenario='$scenario' distant_user='$distant_user' aws='$aws
        else
        	echo "[ERROR] run_once of server failed !"
        fi
}

launch_all() {
        #launch mock $1 $2 $3;
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

echo "executed in" $(((`date +%s` - $date)/60)) "minutes"
if [ "$aws" = true ] ; then
  aws ec2 reboot-instances --instance-ids `cat tmp/aws_instances_id`
  echo "Don't forget to run \`cat tmp/stopAwsCommand\` to shutdown AWS"
else
  vagrant halt >> vagrant.log;
fi