 #!/bin/bash 

mkdir -p /home/vagrant/parallel-lab/vanillapull/gatling
mkdir -p /home/vagrant/parallel-lab/vanillapull/webapp

sudo apt-get -y update
sudo apt-get -y install python-software-properties
sudo add-apt-repository ppa:webupd8team/java -y
sudo apt-get -y update
echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
sudo apt-get -y install oracle-java7-installer
sudo apt-get install oracle-java7-set-default
sudo apt-get -y install maven
sudo apt-get -y install collectd
sudo cp /vagrant/collectd.conf /etc/collectd/collectd.conf
cd
sudo apt-get install -y git
git clone https://github.com/gdbtek/setup-graphite.git
cd setup-graphite/
sudo ./ubuntu.bash --login 'root' --password 'root' --email 'bfi@octo.com'
cd bin/
sudo ./start
