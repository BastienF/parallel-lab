#!/bin/bash
date=`date +%s`
while [ $# -gt 0 ]
do
    case "$1" in
	--instance-id-serv) iidServ="$2"; shift;;
	--instance-id-gat) iidGat="$2"; shift;;
	--allocation-id-serv) allocIdServ="$2"; shift;;
	--allocation-id-gat) allocIdGat="$2"; shift;;
	--route-table-id) routeTableId="$2"; shift;;
	--internet-gateway-id) internetGWId="$2"; shift;;
	--vpc-id) vpcId="$2"; shift;;
	--group-id) secuGroupId="$2"; shift;;
	--subnet-id) subnetId="$2"; shift;;
	-*) echo >&2 \
	    "wrong var: usage: $0 --instance-id-serv iidServ --instance-id-gat iidGat --allocation-id-serv allocIdServ --allocation-id-gat allocIdGat --route-table-id routeTableId --internet-gateway-id internetGWId --vpc-id vpcId --group-id secuGroupId --subnet-id subnetId"
	    exit 1;;
	*)  break;;	# terminate while loop
    esac
    shift
done

if [ -z "$iidServ" ] || [ -z "$allocIdServ" ] || [ -z "$routeTableId" ] || [ -z "$internetGWId" ] || [ -z "$vpcId" ] || [ -z "$secuGroupId" ] || [ -z "$subnetId" ]; then
	echo >&2 \
	    "not enougth vars: usage: $0 --instance-id-serv iidServ --instance-id-gat iidGat --allocation-id-serv allocIdServ --allocation-id-gat allocIdGat --route-table-id routeTableId --internet-gateway-id internetGWId --vpc-id vpcId --group-id secuGroupId --subnet-id subnetId"
	exit 1
fi
echo "[Stop Instance Serv]"
aws ec2 terminate-instances --instance-ids $iidServ

echo "[Stop Instance Gat]"
aws ec2 terminate-instances --instance-ids $iidGat

echo "[Release IP Serv]"
addressOK=`aws ec2 release-address --allocation-id $allocIdServ 2>&1`
echo $addressOK

while [[ $addressOK == *"is in use."* ]]
do
	echo -n "retry... "
	sleep 5
	addressOK=`aws ec2 release-address --allocation-id $allocIdServ 2>&1`
done
echo "OK"
echo $addressOK

echo "[Release IP Gat]"
addressOK=`aws ec2 release-address --allocation-id $allocIdGat 2>&1`
echo $addressOK

while [[ $addressOK == *"is in use."* ]]
do
	echo -n "retry... "
	sleep 5
	addressOK=`aws ec2 release-address --allocation-id $allocIdGat 2>&1`
done
echo "OK"
echo $addressOK

echo "[Detach Internet Gateway]"
sleep 1
aws ec2 detach-internet-gateway --internet-gateway-id $internetGWId --vpc-id $vpcId

echo "[Delete Internet Gateway]"
sleep 1
aws ec2 delete-internet-gateway --internet-gateway-id $internetGWId

echo "[Delete Security Group]"
sleep 1
aws ec2 delete-security-group --group-id $secuGroupId

echo "[Delete Subnet]"
sleep 1
aws ec2 delete-subnet --subnet-id $subnetId

echo "[Delete Route Table]"
sleep 1
aws ec2 delete-route-table --route-table-id $routeTableId

echo "[Delete VPC]"
sleep 1
aws ec2 delete-vpc --vpc-id $vpcId

echo "Check instances stopped : "

aws ec2 describe-instances | jq ".Reservations[].Instances[].State.Name" | sed 's/"terminated"//g' | sed 's/..*/\*\*\*\*\*Error : instance running\*\*\*\*\*/g'
echo "executed in" $(((`date +%s` - $date)/60)) "minutes"