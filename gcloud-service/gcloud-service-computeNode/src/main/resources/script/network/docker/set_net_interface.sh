#!/bin/bash

do_setip()
{

  dcId=$2
  dcUuid=$3
  aftName=$4
  preName=$5
  ifname=$6
  ipadd=$7
  netMask=$8
  gateway=$9
  macAddr=${10}
  
  if [ "$dcUuid" == "" ];then
     exit 101
  fi  

  if [ "$aftName" == "" ];then
     exit 102
  fi

  if [ "$ifname" == "" ];then
     exit 103
  fi

  if [ "$ipadd" == "" ];then
     exit 104
  fi

  if [ "$netMask" == "" ];then
     exit 105
  fi
  
  if [ "$gateway" == "" ];then
     exit 106
  fi
  if [ "$macAddr" == "" ];then
     exit 113
  fi
  

  dockerPid=`docker inspect --format '{{ .State.Pid }}' $dcId`
  
  if [ $? -ne 0  ];then
    do_rollback_and_exit 107 $preName
  fi
 
  ifconfig | grep ^$preName:
  if [ $? -ne 0 ];then
    echo $preName not exist, create
    do_echo_exec ip link add $preName type veth peer name $aftName
  fi
 
  
  do_echo_exec ip link set $aftName netns $dockerPid
  
  if [ $? -ne 0  ];then
    do_rollback_and_exit 108 $preName
  fi
  
  do_echo_exec nsenter -t $dockerPid -n ip link set dev $aftName name $ifname
  
  if [ $? -ne 0  ];then
    do_rollback_and_exit 109 $preName
  fi
  
  do_echo_exec nsenter -t $dockerPid -n ip link set $ifname up
  
  if [ $? -ne 0  ];then
    do_rollback_and_exit 110 $preName
  fi
  
  do_echo_exec nsenter -t $dockerPid -n ip link set dev $ifname address $macAddr
  if [ $? -ne 0  ];then
    do_rollback_and_exit 114 $preName
  fi

 
  do_echo_exec nsenter -t $dockerPid -n ip addr add $ipadd/$netMask dev $ifname
  
  if [ $? -ne 0  ];then
    do_rollback_and_exit 111 $preName
  fi
  
  if [ "$gateway" != "none" ];then
		
    do_echo_exec nsenter -t $dockerPid -n ip route del default
    do_echo_exec nsenter -t $dockerPid -n ip route add default via $gateway dev $ifname
    if [ $? -ne 0  ];then
      do_rollback_and_exit 112 $preName
    fi

  else	
    echo "gateway is null"
  fi
  

}


do_setupenv()
{
    
  portId=$2
  dcUuid=$3
  preName=$4
  aftName=$5
  macAddr=$6
  isForce=$7
  
  if [ "$portId" == ""];then
     exit 140
  fi
  
  if [ "$dcUuid" == "" ];then
     exit 141
  fi
  
  if [ "$preName" == "" ];then
     exit 143
  fi
  
  if [ "$aftName" == "" ];then
     exit 144
  fi
  
  if [ "$macAddr" == "" ];then
     exit 145
  fi
  
  if [ "$isForce" == "" ];then
     isForce=0
  fi
  
  expr $isForce "+" 10 &> /dev/null
  if [ $? -ne 0 ];then
    isForce=0
  fi
  
  do_echo_exec ifconfig | grep ^$preName:
  if [ $? -ne 0 ];then
  	echo "$preName not exist"
  	do_echo_exec ip link add $preName type veth peer name $aftName
  	if [ $? -ne 0 -a $isForce -ne 0 ];then
      exit 146
    fi
  fi  
  
  do_echo_exec ifconfig $preName up
  if [ $? -ne 0 -a $isForce -ne 0 ];then
     exit 147
  fi
  
  do_echo_exec ifconfig | grep ^$aftName:
  if [ $? -eq 0 ];then
  	do_echo_exec ifconfig $aftName up
  	if [ $? -ne 0 -a $isForce -ne 0 ];then
     exit 148
    fi	
  fi

  do_echo_exec ovs-vsctl --timeout=120 -- --if-exists del-port $preName -- add-port br-int $preName -- set Interface $preName external-ids:iface-id=$portId external-ids:iface-status=active external-ids:attached-mac=$macAddr external-ids:vm-uuid=$dcUuid
  if [ $? -ne 0 -a $isForce -ne 0 ];then
    exit 149
  fi	
  

}

do_rollback_and_exit()
{
  exitcode=$1
  preName=$2
  
  if [ "$exitcode" == "" ];then
     exit 120
  fi  
  
  if [ "$preName" == "" ];then
     exit 121
  fi  
  
  do_delete $preName 1
  exit $exitcode
	
}

do_delete()
{
  devName=$1
  isExit=$2

  if [ "$exitcode" == "" ];then
     exit 131
  fi  
  
  if [ "$isExit" == "" ];then
     isExit=0
  fi  

  ip link del $devName

  if [ $? -ne 0 -a $isExit -eq 0 ];then
    exit 132
  fi 
  
  ovs-vsctl del-port br-int $devName
  if [ $? -ne 0 -a $isExit -eq 0 ];then
    exit 133
  fi 

}

do_echo_exec()
{
  cmd=$*
  echo $cmd
  $cmd
}

do_help()
{
  echo "USAGE:set_net_interface [setip|setupenv|delete] [parmas]"
  echo "  set_net_interface setip containerName containerUuid aftName preName ifname ipadd netMask gateway"
  echo "    eg.: set_net_interface setip tomcat1 xxx-xxxx-xxxxx-xxxxxx-xxxxxx aftA00000001 preA00000001 eth0 10.10.10.10 24 10.10.10.1"
  echo "  set_net_interface delete preA00000001"
  echo "  set_net_interface setupenv portId dcUuid preName aftName macAddr force([0|1])"
  ecih

}

case "$1" in
  setip)
    do_setip "$@"
    ;;
  delete)
    do_delete "$@" 0
    ;;
  setupenv)
    do_setupenv "$@"
    ;;
  *)
    do_help
    ;;
esac

