#!/bin/bash 

vagrant up > vagrant.log
./uploadProject.sh
vagrant halt servero7 >> vagrant.log

launch () {

        echo 'Launching implementation : ' $1
        vagrant up servero7 >> vagrant.log
        ssh -p 23457 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'cd /home/vagrant/parallel-lab/vanillapull/; mvn -pl webapp tomcat7:run -Dimplementation='$1' -Diterations='$3 &
        sleep 15
        echo 'Loading gatling for the first time'
        ssh -p 12347 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1  'cd /home/vagrant/parallel-lab/vanillapull/; mvn -pl gatling gatling:execute -Dgatling.simulationClass=PricingSimulation -Dip=192.168.17.3 -Dusers='$2
        ssh -p 23457 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'wget "http://127.0.0.1:9090/services/stopper/stop?users='$2'" >> ~/result.csv'

        echo 'Rapport: '$1''
        rm -r 'results/'$1'_'$2'users_'$3'iter_results/'
        mkdir -p 'results/'$1'_'$2'users_'$3'iter_results/'
        scp -P 12347 -i ~/.vagrant.d/insecure_private_key -r vagrant@127.0.0.1:/home/vagrant/parallel-lab/vanillapull/gatling/target/gatling/results/ 'results/'$1'_'$2'users_'$3'iter_results/'
        ssh -p 12347 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1  'rm -r /home/vagrant/parallel-lab/vanillapull/gatling/target/gatling/results/'

        echo 'Stopping remote tomcat'

        vagrant halt servero7 >> vagrant.log
        #ssh -p 23458 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'kill `lsof -t -i :9090`'
        sleep 1
}

launch_all() {
        launch akka $1 $2;
        launch naive $1 $2;
        launch mono $1 $2;
        launch executor $1 $2;
        launch pool $1 $2;
}

launch_all 100 62500;
launch_all 100 93750;
launch_all 100 125000;
launch_all 100 187500;
launch_all 100 250000;
launch_all 100 325000;
launch_all 100 500000;
launch_all 100 750000;
launch_all 100 1000000;
launch_all 100 1500000;
launch_all 100 2000000;
launch_all 100 3000000;
launch_all 100 4000000;

vagrant halt >> vagrant.log