#!/bin/bash
#/usr/share/gTunnel/xm.sh 10.0.55.2 5900 5800
#echo $1:$2 $3

ps -aux|grep $1:$2 |grep websockify.py
#echo "xiongm--->$?"
if [ $? -eq 0 ] ; then
        echo "xiongm--->$3 has exist,do not need to transform port"
else
        echo "xiongm===="
        python /usr/share/gcloud/network/websockify/websockify.py $3 $1:$2 -D
fi
