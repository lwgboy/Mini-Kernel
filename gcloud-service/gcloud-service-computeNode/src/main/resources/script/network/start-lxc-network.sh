#!/bin/bash
configPath=/etc/gcloud-compute-node.cfg
insConfigName=LXC_CONFIG_PATH
nodeIpName=NODE_IP
insConfigPath=
nodeIp=

nodeInfoName="nodeinfo"
scriptPre="start_net"

#init_params

insConfigName="$insConfigName="
nodeIpName="$nodeIpName="

while read LINE
do
  if [[ $LINE = "$nodeIpName"* ]];then
     nodeIp=${LINE#*$nodeIpName}
  fi
  if [[ $LINE = "$insConfigName"* ]];then
     insConfigPath="${LINE#*$insConfigName}"
  fi
done < $configPath

if [ -z "$nodeIp" ];then
  echo "node ip is null"
  exit 1
fi

if [ -z "$insConfigPath" ];then
  echo "config path is null"
  exit 1
fi


for userId in `ls $insConfigPath`
do
  for instanceId in `ls $insConfigPath/$userId`
    do
      if [ -d $insConfigPath/$userId/$instanceId ];then
         instancePath="$insConfigPath/$userId/$instanceId"
         nodeinfoPath="$instancePath/$nodeInfoName"
         ip=`cat $nodeinfoPath`
         if [ "$nodeIp" = "$ip" ]; then
            for cfgFile in `ls $instancePath`
            do
               if [[ $cfgFile = "start_net"* ]];then
                  sh $instancePath/$cfgFile
               fi
            done
         fi
      fi
    done
done
