vagrant up
./uploadProject.sh

launch () {
    ssh -p 23458 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'cd /home/vagrant/parallel-lab/vanillapull/; mvn -pl webapp'
    ssh -p 23458 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'cd /home/vagrant/parallel-lab/vanillapull/; mvn -pl webapp tomcat7:run -Dimplementation='$1 &
    ssh -p 12348 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1  'cd /home/vagrant/parallel-lab/vanillapull/; mvn -pl gatling gatling:execute -Dgatling.simulationClass=PricingSimulation -Dip=192.168.18.3'
}
launch naive;
sleep 30;
vagrant halt