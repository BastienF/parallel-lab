#!/bin/bash 

vagrant up > vagrant.log
#ansible-playbook -i hosts uploadProject.yml --private-key=~/.vagrant.d/insecure_private_key
launchDate=`date '+%d-%m-%Y.%T'`
ansible-playbook -i hosts -l server deploy_webapp.yml --private-key=~/.vagrant.d/insecure_private_key
launch () {
        ansible-playbook -i hosts -l server run_once.yml --private-key=~/.vagrant.d/insecure_private_key --extra-vars 'iterations='$3' implementation='$1' users='$2' launchDate='$launchDate' duration='$4
        ansible-playbook -i hosts -l gatling run_once.yml --private-key=~/.vagrant.d/insecure_private_key --extra-vars 'iterations='$3' implementation='$1' users='$2' launchDate='$launchDate' duration='$4
}

launch_all() {
        launch akka $1 $2 $3;
        launch naive $1 $2 $3;
        launch mono $1 $2 $3;
        launch executor $1 $2 $3;
        launch pool $1 $2 $3;
}

launch_all 15 5000000 5;
#launch_all 15 50000000 5;
<<COMMENT
launch_all 15 1000 120;
launch_all 15 1500 120;
launch_all 15 2000 120;
launch_all 15 2500 120;
launch_all 15 3500 120;
launch_all 15 5000 120;
launch_all 15 6000 120;
launch_all 15 9000 120;
launch_all 15 12000 120;
launch_all 15 15000 120;
launch_all 15 20000 120;
launch_all 15 25000 120;
launch_all 15 37500 120;
launch_all 15 50000 120;
launch_all 15 65000 120;
launch_all 15 90000 120;
launch_all 15 118000 120;
launch_all 15 150000 120;
launch_all 15 200000 120;
launch_all 15 280000 120;
launch_all 15 375000 120;
launch_all 15 500000 120;
launch_all 15 667000 120;
launch_all 15 890000 120;
launch_all 15 1180000 120;
launch_all 15 1580000 120;
launch_all 15 2100000 120;
launch_all 15 2810000 120;
launch_all 15 3750000 120;
launch_all 15 5000000 120;
#launch_all 100 93750 30;
#launch_all 100 125000 30;
#launch_all 100 187500 30;
#launch_all 100 250000 30;
#launch_all 100 325000 30;
#launch_all 100 500000 30;
#launch_all 100 750000 30;
#launch_all 100 1000000 30;
#launch_all 100 1500000 30;
#launch_all 100 2000000 30;
#launch_all 100 3000000 30;
#launch_all 100 4000000 30;
COMMENT
#vagrant halt >> vagrant.log;