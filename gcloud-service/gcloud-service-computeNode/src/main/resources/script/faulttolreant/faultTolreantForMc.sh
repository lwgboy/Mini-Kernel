#!/bin/bash
if [ "x$1" != "x" -a "x$2" != "x" -a "x$3" != "x" -a "x$4" != "x" ];then
    sourceIp=$1
	targetIp=$2
	port=$3
	instanceId=$4
else
        echo "please input sourceIp, targetIp, port, instanceId for faultTolreant"
        exit 1;
fi

virsh -c qemu+tcp://$sourceIp/system qemu-monitor-command $instanceId --hmp "migrate_set_capability mc on"
virsh -c qemu+tcp://$sourceIp/system qemu-monitor-command $instanceId --hmp "migrate_set_capability mc-net-disable on"
virsh -c qemu+tcp://$sourceIp/system qemu-monitor-command $instanceId --hmp "migrate_set_capability mc-disk-disable off"
virsh -c qemu+tcp://$sourceIp/system qemu-monitor-command $instanceId --hmp "migrate_set_capability rdma-keepalive on"
virsh -c qemu+tcp://$sourceIp/system qemu-monitor-command $instanceId --hmp "migrate_set_capability mc-rdma-copy on"
virsh -c qemu+tcp://$sourceIp/system qemu-monitor-command $instanceId --hmp "migrate -d tcp:$targetIp:$port"