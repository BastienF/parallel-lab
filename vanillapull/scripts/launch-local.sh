
 #!/bin/bash 

launch () {

        echo 'Launching implementation : ' $1
        mvn -pl webapp tomcat7:run -Dimplementation=$1 &
        sleep 10
        echo 'Loading gatling for the first time'
        mvn -pl gatling gatling:execute -Dgatling.simulationClass=PricingSimulation -Dip=127.0.0.1

        echo 'Rapport: '$1''
        rm -r 'gatling/target/gatling/'$1'_results/'
        rmdir 'gatling/target/gatling/'$1'_results'
        mv 'gatling/target/gatling/results/' 'gatling/target/gatling/'$1'_results/'

        echo 'Stopping remote tomcat'
        kill `lsof -t -i :9090`
        sleep 10
}
cd ..
kill `lsof -t -i :9090`
launch naive;
launch akka;
launch mono;
launch executor;
launch pool;
cd scripts
