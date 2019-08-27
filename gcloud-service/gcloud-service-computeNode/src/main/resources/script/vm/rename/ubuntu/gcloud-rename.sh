#!/bin/bash
if [ $# -eq 0 ]; then
 exit 1;
else
 name=$1;
 echo ${name} > /etc/hostname;
 hostname ${name};
 exit 0
fi
