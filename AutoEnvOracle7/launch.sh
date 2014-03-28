#!/bin/bash 

vagrant up > vagrant.log
./uploadProject.sh
launchDate=`date '+%d-%m-%Y.%T'`
#Deployment of the last version of webapp
scp -P 23457 -i ~/.vagrant.d/insecure_private_key -r "settings.xml" vagrant@127.0.0.1:/home/vagrant/.m2/settings.xml
ssh -p 23457 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'sudo JAVA_OPTS="-Dimplementation=mono -Diterations=50000" ~/apache-tomcat-7.0.52/bin/startup.sh'
ssh -p 23457 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'cd /home/vagrant/parallel-lab/vanillapull/; mvn -pl webapp tomcat7:undeploy  -Dimplementation=mono -Diterations=50000'
ssh -p 23457 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'sudo ~/apache-tomcat-7.0.52/bin/shutdown.sh'


launch () {

        echo 'Launching implementation : ' $1
        ssh -p 23457 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'sudo JAVA_OPTS="-Dimplementation='$1' -Diterations='$3'" ~/apache-tomcat-7.0.52/bin/startup.sh'
        ssh -p 23457 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'cd /home/vagrant/parallel-lab/vanillapull/; mvn -pl webapp tomcat7:deploy -Dimplementation='$1' -Diterations='$3
        
        sleep 1
        echo 'Loading gatling for the first time'
        ssh -p 12347 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1  'cd /home/vagrant/parallel-lab/vanillapull/gatling-json/gatling-highcharts/; mvn -pl pricingsimulation gatling:execute -Dgatling.simulationClass=PricingSimulation -Dip=192.168.17.3 -Dusers='$2' -Dimplementation='$1' -Diterations='$3' -DlogPath=/home/vagrant/datalog-'$launchDate' -Dduration='$4
        #ssh -p 23457 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'wget "http://127.0.0.1:8080/services/stopper/stop?users='$2'" >> ~/result.csv'

        echo 'Rapport: '$1''
        rm -r 'results/'$1'_'$2'users_'$3'iter_results/'
        mkdir -p 'results/'$1'_'$2'users_'$3'iter_results/'
        mkdir -p 'results/json/'
        mkdir -p 'results/csv/'
        scp -P 12347 -i ~/.vagrant.d/insecure_private_key -r vagrant@127.0.0.1:/home/vagrant/datalog-$launchDate.json 'results/json/datalog-'$launchDate'.json'
        scp -P 12347 -i ~/.vagrant.d/insecure_private_key -r vagrant@127.0.0.1:/home/vagrant/datalog-$launchDate.csv 'results/csv/datalog-'$launchDate'.csv'
        scp -P 12347 -i ~/.vagrant.d/insecure_private_key -r vagrant@127.0.0.1:/home/vagrant/parallel-lab/vanillapull/gatling-clean/gatling-highcharts/pricingsimulation/target/gatling/results/ 'results/'$1'_'$2'users_'$3'iter_results/'
        ssh -p 12347 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1  'rm -r /home/vagrant/parallel-lab/vanillapull/gatling-clean/gatling-highcharts/pricingsimulation/target/gatling/results/'

        echo 'Stopping remote tomcat'
        ssh -p 23457 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'cd /home/vagrant/parallel-lab/vanillapull/; mvn -pl webapp tomcat7:undeploy  -Dimplementation='$1' -Diterations='$3' ; ~/apache-tomcat-7.0.52/bin/shutdown.sh'

        #ssh -p 23458 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'kill `lsof -t -i :9090`'
        sleep 1
}

launch_all() {
        launch akka $1 $2 $3;
        launch naive $1 $2 $3;
        launch mono $1 $2 $3;
        launch executor $1 $2 $3;
        launch pool $1 $2 $3;
}

launch_all 15 5000000 120;
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
vagrant halt >> vagrant.log;