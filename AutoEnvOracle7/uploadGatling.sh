#!/bin/bash 

upload () {
ssh -p 12347 -i ~/.vagrant.d/insecure_private_key vagrant@127.0.0.1 'rm -rf /home/vagrant/parallel-lab/vanillapull/'$1
scp -P 12347 -i ~/.vagrant.d/insecure_private_key -r "../vanillapull/"$1 vagrant@127.0.0.1:/home/vagrant/parallel-lab/vanillapull/$1
}

upload gatling-json/
upload scripts/