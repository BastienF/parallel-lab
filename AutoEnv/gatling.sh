 #!/bin/bash 

mkdir -p /home/vagrant/parallel-lab/vanillapull/gatling
mkdir -p /home/vagrant/parallel-lab/vanillapull/webapp

sudo apt-get -y update
sudo apt-get -y install openjdk-7-jdk
sudo apt-get -q -y install maven
