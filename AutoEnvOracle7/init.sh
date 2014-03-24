vagrant up
./uploadProject.sh
./uploadGatling.sh

launch () {
	ssh -p 23457 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'if [ ! -d "/home/vagrant/apache-tomcat-7.0.52" ]; then echo "download tomcat"; wget http://apache.lehtivihrea.org/tomcat/tomcat-7/v7.0.52/bin/apache-tomcat-7.0.52.tar.gz; tar xvfz apache-tomcat-7.0.52.tar.gz; else echo "tomcat already installed"; fi'
	scp -P 23457 -i ~/.vagrant.d/insecure_private_key -r "tomcat-users.xml" vagrant@127.0.0.1:/home/vagrant/apache-tomcat-7.0.52/conf/tomcat-users.xml
	scp -P 23457 -i ~/.vagrant.d/insecure_private_key -r "settings.xml" vagrant@127.0.0.1:/home/vagrant/.m2/settings.xml
	ssh -p 23457 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'sudo JAVA_OPTS="-Dimplementation='$1' -Diterations='$3'" ~/apache-tomcat-7.0.52/bin/startup.sh'
    ssh -p 23457 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'cd /home/vagrant/parallel-lab/vanillapull/; mvn -pl webapp tomcat7:deploy -Dimplementation='$1' -Diterations='$3
    ssh -p 12347 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1  'cd /home/vagrant/parallel-lab/vanillapull/gatling-json/gatling/; mvn clean install'
    ssh -p 12347 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1  'cd /home/vagrant/parallel-lab/vanillapull/gatling-json/gatling-highcharts/; mvn clean install'
    ssh -p 12347 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1  'cd /home/vagrant/parallel-lab/vanillapull/gatling-json/gatling-highcharts/; mvn -pl pricingsimulation gatling:execute -Dgatling.simulationClass=PricingSimulation -Dip=192.168.17.3 -Dusers='$2' -Dimplementation='$1' -Diterations='$3' -DlogPath=/home/vagrant/initlog -Dduration='$4
    ssh -p 23457 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'cd /home/vagrant/parallel-lab/vanillapull/; mvn -pl webapp tomcat7:undeploy -Dimplementation='$1' -Diterations='$3
}
launch naive 50 62500 10;
vagrant halt