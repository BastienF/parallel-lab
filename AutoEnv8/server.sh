 #!/bin/bash 

mkdir -p /home/vagrant/parallel-lab/vanillapull/gatling
mkdir -p /home/vagrant/parallel-lab/vanillapull/webapp

sudo apt-get -y update
sudo apt-get -y install python-software-properties
sudo add-apt-repository ppa:webupd8team/java -y
sudo apt-get -y update
echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
sudo apt-get -y install oracle-java8-installer
sudo apt-get install oracle-java8-set-default
sudo apt-get -y install maven
