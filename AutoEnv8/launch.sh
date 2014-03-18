#!/bin/bash 

vagrant up > vagrant.log
./uploadProject.sh
vagrant halt server8 >> vagrant.log

launch () {

        echo 'Launching implementation : ' $1
        vagrant up server8 >> vagrant.log
        ssh -p 23458 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'cd /home/vagrant/parallel-lab/vanillapull/; mvn -pl webapp tomcat7:run -Dimplementation='$1 &
        sleep 10
        echo 'Loading gatling for the first time'
        ssh -p 12348 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1  'cd /home/vagrant/parallel-lab/vanillapull/; mvn -pl gatling gatling:execute -Dgatling.simulationClass=PricingSimulation -Dip=192.168.18.3'

        echo 'Rapport: '$1''
        rm -r 'results/'$1'_results/'
        mkdir -p 'results/'$1'_results/'
        scp -P 12348 -i ~/.vagrant.d/insecure_private_key -r vagrant@127.0.0.1:/home/vagrant/parallel-lab/vanillapull/gatling/target/gatling/results/ 'results/'$1'_results/'
        ssh -p 12348 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1  'rm -r /home/vagrant/parallel-lab/vanillapull/gatling/target/gatling/results/'

        echo 'Stopping remote tomcat'
        vagrant halt server8 >> vagrant.log
        #ssh -p 23458 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'kill `lsof -t -i :9090`'
        sleep 10
}
launch naive;
launch akka;
launch mono;
launch executor;
launch pool;

vagrant halt >> vagrant.log