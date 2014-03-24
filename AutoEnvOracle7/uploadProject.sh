#!/bin/bash 

upload () {
ssh -p 23457 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'rm -rf /home/vagrant/parallel-lab/vanillapull/'$1
scp -P 23457 -i ~/.vagrant.d/insecure_private_key -r "../vanillapull/"$1 vagrant@127.0.0.1:/home/vagrant/parallel-lab/vanillapull/$1
}

upload pom.xml
upload scripts/
upload webapp/pom.xml
upload webapp/src