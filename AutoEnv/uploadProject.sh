#!/bin/bash 

upload () {
ssh -p 2345 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'rm -rf /home/vagrant/parallel-lab/vanillapull/'$1
ssh -p 1234 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'rm -rf /home/vagrant/parallel-lab/vanillapull/'$1
scp -P 1234 -i ~/.vagrant.d/insecure_private_key -r "../vanillapull/"$1 vagrant@127.0.0.1:/home/vagrant/parallel-lab/vanillapull/$1
scp -P 2345 -i ~/.vagrant.d/insecure_private_key -r "../vanillapull/"$1 vagrant@127.0.0.1:/home/vagrant/parallel-lab/vanillapull/$1
}

upload pom.xml
upload gatling/pom.xml
upload gatling/src/
upload scripts/
upload webapp/pom.xml
upload webapp/src