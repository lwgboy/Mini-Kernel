#!/bin/bash

oper=$1
setLogPath="/usr/share/gcloud/gcsetip.log"
delLogPath="/usr/share/gcloud/gcdelip.log"
routeAddPy=/usr/share/gcloud/netTools/route_add.py
routeDelPy=/usr/share/gcloud/netTools/route_del.py
routeSetupPy=/usr/share/gcloud/netTools/route_setup.py

do_setip()
{
  echo "begin set ip"
  echo param1="$1",param2="$2",param3="$3",param4="$4",param5="$5",param6="$6"
  date
  
  echo "do_setup_rc_local"
  do_setup_rc_local

  targetMac=$1
  targetIp=$2
  targetGw=$3
  targetMask=$4
  targetDns=$5
  timeout=$6
  
  if [ "$targetMac" == "" ];then
     exit 105
  fi

  if [ "$targetIp" == "" ];then
     exit 106
  fi

  if [ "$targetGw" == "" ];then
     exit 107
  fi

  if [ "$targetMask" == "" ];then
     exit 108
  fi
  
  if [ "$targetDns" == "" ];then
     exit 109
  fi
  
  if [ "$targetGw" == "none" ];then
     targetGw=
  fi

  if [ "$targetDns" == "none" ];then
     targetDns=
  fi

  if [ "$timeout" == ""  ];then
     timeout=0
  elif [[ $timeout == *[!0-9]* ]]; then
     timeout=0
  fi  

  ethFindTime=1
  tootalTime=10
  
  targetEth=
  ethcfgPath="/sys/class/net"  

  while [ $ethFindTime -le $tootalTime ] 
  do
  
    for ethcfg in `ls $ethcfgPath`
    do
      addressPath=$ethcfgPath/$ethcfg/address
      echo $addressPath
      if [ -f $addressPath ];then
         macaddr=`cat $addressPath`
         if [ "$macaddr" = "$targetMac" ];then
            targetEth=$ethcfg
            break
         fi
      fi
    done
    
    if [ "$targetEth" == ""  ];then  
       echo "target eth not found, sleep, eth find time="$ethFindTime
       if [ $ethFindTime -lt $tootalTime ];then
         echo "sleep 1"
         sleep 1
       fi
    else
       break
    fi 
    ethFindTime=$(($ethFindTime+1))
 
  done
   
  if [ "$targetEth" == "" ];then
     echo "target eth not found exit"
     exit 104
  fi
  
  echo $targetEth
  
  netScriptPath="/etc/sysconfig/network-scripts"
  ethFile="$netScriptPath/ifcfg-$targetEth"
  
  echo $ethFile
  if [ -d "$netScriptPath"  ];then
    echo "NAME=$targetEth" > $ethFile
    echo "DEVICE=$targetEth" >> $ethFile
    echo "TYPE=Ethernet" >> $ethFile
    echo "BOOTPROTO=none" >> $ethFile
    echo "ONBOOT=yes" >> $ethFile
    echo "IPADDR=$targetIp" >> $ethFile
    echo "NETMASK=$targetMask" >> $ethFile
    echo "HWADDR=$targetMac" >> $ethFile
    
    if [ "$targetGw" != "" ];then
       echo "GATEWAY=$targetGw" >> $ethFile
    fi
    
    if [ "$targetDns" != "" ];then
      i=1
      dnsList=`echo $targetDns | cut -d \, --output-delimiter=" " -f 1-`
      echo $dnsList
      
      for tDns in $dnsList
      do
        echo "DNS$i=$tDns" >> $ethFile
        i=$(($i+1))
      done

    fi
    
    cat $ethFile  
  
    echo "ifdown $targetEth"
    ifdown $targetEth
    
    if [ $timeout -gt 0  ];then
      echo "do_timeout "ifup $targetEth" $timeout"
      do_timeout "ifup $targetEth" $timeout
    else
      echo "ifup $targetEth"
      ifup $targetEth
    fi

    if [ $? -gt 0 ];then
      echo "do_del_eth $targetEth"
      do_del_eth $targetEth
      exit 103
    fi
    
    echo "do_route_add $targetEth $targetIp $targetMask $targetGw"
    do_route_add $targetEth $targetIp $targetMask $targetGw
    if [ $? -gt 0 ];then
      echo "do_route_add fail"
      do_del_eth $targetMac $targetEth
      exit 113
    fi
    
  else
    exit 101
  fi

}


do_delete()
{
  echo "begin del ip"
  echo param1="$1"
  date
   
  echo "delete"
  deleteMac=$1
  
  echo "$deleteMac";
   
  declare -A macMap=()

  ethcfgPath="/sys/class/net"
  
  for ethcfg in `ls $ethcfgPath`
  do
    addressPath=$ethcfgPath/$ethcfg/address
    echo $addressPath
    if [ -f $addressPath ];then
       macaddr=`cat $addressPath`
       if [ "$macaddr" != ""  ];then
         macMap["$macaddr"]="$ethcfg"
       fi
    fi
  done

  macList=`echo $deleteMac | cut -d \, --output-delimiter=" " -f 1-`
  echo "maclist="$macList
  for dMac in $macList
  do
    deleteEth=${macMap["$dMac"]}
    echo "del$deleteEth"
    if [ "$deleteEth" != ""  ];then
      echo "do_del_eth $deleteEth"
      do_del_eth $deleteEth
      do_route_del $deleteEth
      
      if [ $? -gt 0 ];then
        echo "do_route_del $deleteEth fail"
        exit 117
      fi      
  
    fi
 
  done

}

do_del_eth()
{

  deleteEth=$1
  
  if [ "$deleteEth" == "" ];then
     exit 111
  fi
  
  deleteEthCfg="/etc/sysconfig/network-scripts/ifcfg-$deleteEth"
  echo "$deleteEthCfg"
  if [ -f "$deleteEthCfg" ];then
     echo "file exist"
     rm -f $deleteEthCfg
  fi
  
}

do_timeout()
{

  command=$1
  timeout=$2

  echo "$command"
  echo "$timeout"

  if [ "$timeout" == ""  ];then
     timeout=0
  elif [[ $timeout == *[!0-9]* ]]; then
     timeout=0
  fi

  $command &
  cmdpid=$!

  echo $cmdpid

  (sleep $timeout; kill -9 $cmdpid > /dev/null 2>&1) &

  sleeppid=$!

  echo $sleeppid

  wait $cmdpid > /dev/null 2>&1
 
  kill -9 $sleeppid > /dev/null 2>&1
  
  return $?

}

do_route_add()
{
  addEth=$1
  addIp=$2
  addMask=$3
  addGw=$4
  
  if [ "$addGw" == "" ];then
    echo "gw is null, do not set route"
    return 0;
  fi
  
  echo "python $routeAddPy $addEth $addIp $addMask $addGw"
  python $routeAddPy $addEth $addIp $addMask $addGw
  

}

do_route_del()
{
  delEth=$1
  
  echo "python $routeDelPy $delEth"
  python $routeDelPy $delEth
}

do_setup_rc_local()
{

  echo "cat /etc/rc.local  | grep ^\ *python\ *$routeSetupPy"
  cat /etc/rc.local  | grep ^\ *python\ *$routeSetupPy
  if [ $? -gt 0 ];then
    echo "$routeSetupPy not exit"
    
    echo "cat /etc/rc.local | grep ^\ *python\ */usr/lib/route-setup.py"
    cat /etc/rc.local | grep ^\ *python\ */usr/lib/route-setup.py
    if [ $? -gt 0 ];then
      echo "old py opt not exit, add at the end"
      echo python $routeSetupPy >> /etc/rc.local
    else
      echo "old py opt exit, replace"
      sed -i s/^\ *python\ *\\/usr\\/lib\\/route-setup.py/python\ \\/usr\\/share\\/gcloud\\/netTools\\/route_setup.py/ /etc/rc.local
      
    fi
    
  fi

}



do_help()
{
  echo "USAGE:gciptool [setip|delete] [parmas]"
  echo "  gciptool setip target_mac target_ip target_gateway target_netmask target_dns1,target_dns2"
  echo "  gciptool delete del_mac1,del_mac2"
}


case "$1" in
  setip)
    do_setip $2 $3 $4 $5 $6 $7 > $setLogPath 2>&1
    ;;
  delete)
    do_delete $2 > $delLogPath 2>&1
    ;;
  *)
    do_help
    ;;
esac

