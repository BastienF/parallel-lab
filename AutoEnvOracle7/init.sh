vagrant up
./uploadProject.sh
./uploadGatling.sh

launch () {
    ssh -p 23457 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'cd /home/vagrant/parallel-lab/vanillapull/; mvn -pl webapp'
    ssh -p 23457 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'cd /home/vagrant/parallel-lab/vanillapull/; mvn -pl webapp tomcat7:run -Dimplementation='$1' -Diterations='$3 &
    ssh -p 12347 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1  'cd /home/vagrant/parallel-lab/vanillapull/gatling-json/gatling/; mvn clean install'
    ssh -p 12347 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1  'cd /home/vagrant/parallel-lab/vanillapull/gatling-json/gatling-highcharts/; mvn clean install'
    ssh -p 12347 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1  'cd /home/vagrant/parallel-lab/vanillapull/gatling-json/gatling-highcharts/; mvn -pl pricingsimulation gatling:execute -Dgatling.simulationClass=PricingSimulation -Dip=192.168.17.3 -Dusers='$2' -Dimplementation='$1' -Diterations='$3' -DlogPath=/home/vagrant/initlog.json'
}
launch naive 50 62500;
sleep 60;
vagrant halt