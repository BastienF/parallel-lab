#!/bin/bash

date=`date +%s`

poolLogPath=""

while [ $# -gt 0 ]
do
    case "$1" in
  --scenario) scenario="$2"; shift;;
  --server) server="$2"; shift;;
  --nbThreads) nbThreads="$2"; shift;;
  --use-aws) aws=true;;
  --poolLogPath) poolLogPath="$2"; shift;;
  -*) echo >&2 \
      "wrong var: usage: $0 --scenario scenario [--server server] [--nbThreads nbThreads] [--use-aws] [--poolLogPath poolLogPath]"
      exit 1;;
  *)  break;; # terminate while loop
    esac
    shift
done

if [ -z "$scenario" ]; then
  echo >&2 \
      "not enougth vars: usage: $0 --scenario scenario [--server server] [--nbThreads nbThreads] [--use-aws] [--poolLogPath poolLogPath]"
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
  ansible-playbook -i $HOSTS -l servero7 upload_aws.yml --private-key=$PKEY
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

launchDate="$LANGUAGE-$server-nbThreads$nbThreads-$synchName`date '+%d-%m-%Y.%T'`"
if [ "$aws" = true ] ; then
    launchDate="AWS-$launchDate"
fi

static_param='launchDate='$launchDate' server='$server' scenario='$scenario' distant_user='$distant_user' aws='$aws' language='$LANGUAGE' nbThreads='$nbThreads' poolLogPath='$poolLogPath

echo "Server setted at" $server
ansible-playbook -i $HOSTS -l $VM_SERVER deploy_webapp.yml --private-key=$PKEY --extra-vars "$static_param"
if [ "$?" != "0" ]; then
	echo "[ERROR] webapp deployement failed !"
	exit 2;
fi

launch () {
	      date

        if [ $synchronizationMode == true ]; then
          iterations=$3
          delay=0
        else
          iterations=0
          delay=$3
        fi
        echo "Run with iterations=$iterations delay=$delay implementation=$1 users=$2 launchDate=$launchDate duration=$4 server=$server scenario=$scenario"
        ansible-playbook -i $HOSTS -l $VM_SERVER run_once.yml --private-key=$PKEY --extra-vars "implementation=$1 iterations=$iterations noSynchronization_delay=$delay users=$2 duration=$4 synchronizationMode=$synchronizationMode $static_param"
        if [ "$?" = "0" ]; then
        	ansible-playbook -i $HOSTS -l gatling run_once.yml --private-key=$PKEY --extra-vars "implementation=$1 iterations=$iterations noSynchronization_delay=$delay users=$2 duration=$4 synchronizationMode=$synchronizationMode $static_param"
        else
        	echo "[ERROR] run_once of server failed !"
        fi
}

launch_all() {
        launch akka $1 $2 $3;
        launch naive $1 $2 $3;
       	launch mono $1 $2 $3;
        launch fork $1 $2 $3;
       	launch executor $1 $2 $3;
       	launch pool $1 $2 $3;
}
first=false
synchronizationMode=true
while IFS=';' read users iter dur
do 
if [ $first == false ]; then
	first=true;
  if [ "$iter" == "iterations" ]; then
    synchronizationMode=true
  else
    synchronizationMode=false
  fi
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
  a=3;
fi