#!/bin/bash

oper=$1
setLogPath="/usr/share/gcloud/gcsetip.log"
delLogPath="/usr/share/gcloud/gcdelip.log"
routeAddPy=/usr/share/gcloud/netTools/route_add.py
routeDelPy=/usr/share/gcloud/netTools/route_del.py
routeSetupPy=/usr/share/gcloud/netTools/route_setup.py

targetEth=""

do_setip()
{
  echo "begin set ip"
  echo param1="$1",param2="$2",param3="$3",param4="$4"
  date
  
  echo "do_setup_rc_local"
  do_setup_rc_local

  targetMac=$1
  targetIp=$2
  targetGw=$3
  targetMask=$4

  
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
  
  if [ "$targetGw" == "none" ];then
     targetGw=
  fi
  
  do_get_target_eth $targetMac

  if [ "$targetEth" == "" ];then
     echo "target eth not found exit"
     exit 104
  fi
  
  echo $targetEth
  
  echo "do_route_add $targetEth $targetIp $targetMask $targetGw"
  do_route_add $targetEth $targetIp $targetMask $targetGw
  
}


do_delete()
{
 
  targetMac=$1
  
  if [ "$targetMac" == "" ];then
     exit 111
  fi

  do_get_target_eth $targetMac
  
  echo "$targetEth"

  if [ "$targetEth" == "" ];then
     echo "target eth not found exit"
     exit 112
  fi
 
  echo "do_route_del $targetEth"
  do_route_del $targetEth

}


do_get_target_eth()
{
  
  targetMac=$1
  
  ethFindTime=1
  tootalTime=10
  
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


}


do_route_add()
{
  addEth=$1
  addIp=$2
  addMask=$3
  addGw=$4
  
  if [ "$addGw" == "" ];then
    echo "gw is null, do not set route"
    return 0
  fi
  
  echo "python $routeAddPy $addEth $addIp $addMask $addGw"
  python $routeAddPy $addEth $addIp $addMask $addGw
  
  returnCode=$?
  echo "python add, returnCode = $returnCode"
  if [ $returnCode -eq 3 ];then
    exit 133
  elif [ $returnCode -gt 0 ];then
    exit 134
  fi
  

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
  echo "  gciptool setip target_mac target_ip target_gateway target_netmask"
  echo "  gciptool delete del_mac1"
}


case "$1" in
  setip)
    do_setip $2 $3 $4 $5 > $setLogPath 2>&1
    ;;
  delete)
    do_delete $2 > $delLogPath 2>&1
    ;;
  *)
    do_help
    ;;
esac

