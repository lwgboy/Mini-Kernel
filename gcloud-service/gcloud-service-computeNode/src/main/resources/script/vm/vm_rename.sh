#!/bin/bash
instanceId=$1
instanceName=$2
strategy=$3
guestname=
if [ ${strategy}x == "id"x ]; then
	guestname=${instanceId}
elif [ ${strategy}x == "name"x ]; then
	guestname=${instanceName}
elif [ ${strategy}x == "seat"x ]; then
	guestname=${instanceName##*_}
else
	guestname=${instanceId}
fi
cat << EOF >/tmp/${instanceId}.reg

[HKLM\SYSTEM\ControlSet001\Control\ComputerName\ActiveComputerName]
"ComputerName"="${guestname}"

[HKLM\SYSTEM\ControlSet001\Control\ComputerName\ComputerName]
"ComputerName"="${guestname}"

[HKLM\SYSTEM\ControlSet001\Services\Tcpip\Parameters]
"Hostname"="${guestname}"

[HKLM\SYSTEM\ControlSet001\Services\Tcpip\Parameters]
"NV Hostname"="${guestname}"

EOF
#get the full path
fullpath=$(virsh dumpxml ${instanceId}|grep ${instanceId}_snap|awk -F\' '{print $2}')
virt-win-reg --merge $fullpath /tmp/${instanceId}.reg
rm -rf /tmp/${instanceId}.reg
