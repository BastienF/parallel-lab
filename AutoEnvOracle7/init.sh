#!/bin/bash

date=`date +%s`
while [ $# -gt 0 ]
do
    case "$1" in
  --use-aws) aws=true; shift;;
  -*) echo >&2 \
      "wrong var: usage: $0 [--use-aws]"
      exit 1;;
  *)  break;; # terminate while loop
    esac
    shift
done

if [ -z "$aws" ]; then
	aws=false
fi

if [ "$aws" = true ] ; then
	echo 'You have to have run start_aws.sh'
	HOSTS=tmp/hosts_aws
	PKEY=provisioning/parallelLab.pem
	distant_user='ubuntu'
	full=false
else
	HOSTS=hosts
	PKEY=~/.vagrant.d/insecure_private_key
	distant_user='vagrant'
	ssh-keygen -R [127.0.0.1]:12347
	ssh-keygen -R [127.0.0.1]:23457
	ssh-keygen -R [127.0.0.1]:23458
	sudo chown `whoami`:staff /etc/exports
	vagrant up
	full=true
fi

ansible-playbook -i tmp/hosts_aws provisioning.yml --private-key=provisioning/parallelLab.pem --extra-vars 'distant_user='$distant_user' aws='$aws' full='$full

ansible-playbook -i $HOSTS -l gatling provisioning.yml --private-key=$PKEY --extra-vars 'distant_user='$distant_user' aws='$aws' full='$full 1>tmp/provisioning-gatling.log &
WAITPID=$!
echo "pid: $WAITPID"
ansible-playbook -i $HOSTS -l server provisioning.yml --private-key=$PKEY --extra-vars 'distant_user='$distant_user' aws='$aws' full='$full
wait $WAITPID

echo "executed in" $(((`date +%s` - $date)/60)) "minutes"
