#!/bin/bash

date=`date +%s`
vpcId=`aws ec2 create-vpc --cidr-block 10.0.0.0/16 | jq ".Vpc.VpcId" | sed 's/"//g'`
echo "vpcId: $vpcId"


sleep 1
internetGWId=`aws ec2 create-internet-gateway | jq ".InternetGateway.InternetGatewayId" | sed 's/"//g'`
echo "internetGWId: $internetGWId"


sleep 1
aws ec2 attach-internet-gateway --internet-gateway-id $internetGWId --vpc-id $vpcId

sleep 1
subnetId=`aws ec2 create-subnet --vpc-id $vpcId --cidr-block 10.0.0.0/16 | jq ".Subnet.SubnetId" | sed 's/"//g'`
echo "subnetId: $subnetId"

sleep 1
routeTableId=`aws ec2 create-route-table --vpc-id $vpcId | jq ".RouteTable.RouteTableId" | sed 's/"//g'`
echo "routeTableId: $routeTableId"

sleep 1
aws ec2 create-route --route-table-id $routeTableId --destination-cidr-block 0.0.0.0/0 --gateway-id $internetGWId

sleep 1
aws ec2 associate-route-table --route-table-id $routeTableId --subnet-id $subnetId

sleep 1
secuGroupId=`aws ec2 create-security-group --group-name parallelLabSecurityGroupTmp --description "SecurityGroup of parallel-lab benchtmp" --vpc-id $vpcId | jq ".GroupId" | sed 's/"//g'`
echo "secuGroupId: $secuGroupId"

openTCPPort () {
	aws ec2 authorize-security-group-ingress --group-id $secuGroupId --protocol tcp --port $1 --cidr `curl ifconfig.me`/0
	aws ec2 authorize-security-group-egress --group-id $secuGroupId --protocol tcp --port $1 --cidr `curl ifconfig.me`/0
}

sleep 1
openTCPPort 80
openTCPPort 8080
openTCPPort 22





sleep 1
aws ec2 run-instances --image-id ami-776d9700 --key-name parallelLab --instance-type c3.xlarge --subnet-id $subnetId --security-group-ids $secuGroupId 1>./tmp/aws-output.json # --image-id ami-af7abed8 --instance-type t1.micro ## --image-id ami-776d9700 --instance-type c3.xlarge

iidServ=`cat ./tmp/aws-output.json | jq ".Instances[0].InstanceId" | sed 's/"//g'`
echo "instanceServId: $iidServ"

privateIpServ=`cat ./tmp/aws-output.json | jq ".Instances[0].PrivateIpAddress" | sed 's/"//g'`
echo "privateIpServ: $privateIpServ"

sleep 1
aws ec2 run-instances --image-id ami-776d9700 --key-name parallelLab --instance-type m3.medium --subnet-id $subnetId --security-group-ids $secuGroupId 1>./tmp/aws-output.json # --image-id ami-af7abed8 --instance-type t1.micro ## --image-id ami-776d9700  --instance-type m1.small

iidGat=`cat ./tmp/aws-output.json | jq ".Instances[0].InstanceId" | sed 's/"//g'`
echo "instanceGatId: $iidGat"

privateIpGat=`cat ./tmp/aws-output.json | jq ".Instances[0].PrivateIpAddress" | sed 's/"//g'`
echo "privateIpGat: $privateIpGat"

sleep 15
aws ec2 allocate-address --domain vpc 1>./tmp/aws-ip-output.json
allocIdServ=`cat ./tmp/aws-ip-output.json | jq ".AllocationId" | sed 's/"//g'`
publicIpServ=`cat ./tmp/aws-ip-output.json | jq ".PublicIp" | sed 's/"//g'`
echo "IP Serv: $publicIpServ . allocIdServ : $allocIdServ"

sleep 1
aws ec2 allocate-address --domain vpc 1>./tmp/aws-ip-output.json
allocIdGat=`cat ./tmp/aws-ip-output.json | jq ".AllocationId" | sed 's/"//g'`
publicIpGat=`cat ./tmp/aws-ip-output.json | jq ".PublicIp" | sed 's/"//g'`
echo "IP Gat: $publicIpGat . allocIdGat : $allocIdGat"

echo -e "[server]\nservero7 ansible_ssh_host=$publicIpServ ansible_ssh_user=ubuntu ansible_ssh_port=22 host_key_checking=false privateIp=$privateIpServ" > tmp/hosts_aws
echo -e "\n[gatling]\ngatlingo7 ansible_ssh_host=$publicIpGat ansible_ssh_user=ubuntu ansible_ssh_port=22 host_key_checking=false privateIp=$privateIpGat" >> tmp/hosts_aws

sleep 15
aws ec2 associate-address --instance-id $iidServ --allocation-id $allocIdServ
aws ec2 associate-address --instance-id $iidGat --allocation-id $allocIdGat

echo "./stop_aws.sh --instance-id-serv $iidServ --instance-id-gat $iidGat --allocation-id-serv $allocIdServ --allocation-id-gat $allocIdGat --route-table-id $routeTableId --internet-gateway-id $internetGWId --vpc-id $vpcId --group-id $secuGroupId --subnet-id $subnetId" > tmp/stopAwsCommand
cat tmp/stopAwsCommand

echo "executed in" $(((`date +%s` - $date)/60)) "minutes"

ssh -i ~/Downloads/parallelLab.pem ubuntu@$publicIpServ