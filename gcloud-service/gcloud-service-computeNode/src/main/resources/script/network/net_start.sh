#!/bin/bash
if [ $# -ne 6 ]; then
  echo "params num not match"
  exit 1
fi
portId=$1
vmUuid=$2
brName=$3
preName=$4
aftName=$5
macAddr=$6

brctl addbr $brName
ifconfig  $brName  up
ip link add $preName type veth peer name $aftName
ifconfig $preName up;ifconfig $aftName up
brctl addif $brName $preName
ovs-vsctl --timeout=120 -- --if-exists del-port $aftName -- add-port br-int $aftName -- set Interface $aftName external-ids:iface-id=$portId external-ids:iface-status=active external-ids:attached-mac=$macAddr external-ids:vm-uuid=$vmUuid
