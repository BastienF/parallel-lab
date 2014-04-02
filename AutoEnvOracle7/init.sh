ssh-keygen -R [127.0.0.1]:12347
ssh-keygen -R [127.0.0.1]:23457
sudo chown `whoami`:staff /etc/exports
vagrant up
ansible-playbook -i hosts provisioning.yml --private-key=~/.vagrant.d/insecure_private_key