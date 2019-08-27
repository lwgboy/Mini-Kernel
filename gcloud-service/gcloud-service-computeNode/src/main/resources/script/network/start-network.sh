#!/bin/bash
configPath=/etc/gcloud-compute-node.cfg
insConfigName=INSTANCE_CONFIG_PATH
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


for userId in `ls $insConfigPath/$nodeIp`
do
  for instanceId in `ls $insConfigPath/$nodeIp/$userId`
    do
      if [ -d $insConfigPath/$nodeIp/$userId/$instanceId ];then
         instancePath="$insConfigPath/$nodeIp/$userId/$instanceId"
         for cfgFile in `ls $instancePath`
         do
            if [[ $cfgFile = "$scriptPre"* ]];then
               sh $instancePath/$cfgFile
            fi
         done
      fi
    done
done