#!/bin/bash 

upload () {
ssh -p 23457 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'rm -rf /home/vagrant/parallel-lab/vanillapull/'$1
ssh -p 12347 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'rm -rf /home/vagrant/parallel-lab/vanillapull/'$1
scp -P 12347 -i ~/.vagrant.d/insecure_private_key -r "../vanillapull/"$1 vagrant@127.0.0.1:/home/vagrant/parallel-lab/vanillapull/$1
scp -P 23457 -i ~/.vagrant.d/insecure_private_key -r "../vanillapull/"$1 vagrant@127.0.0.1:/home/vagrant/parallel-lab/vanillapull/$1
}

upload pom.xml
upload gatling/pom.xml
upload gatling/src/
upload scripts/
upload webapp/pom.xml
upload webapp/src