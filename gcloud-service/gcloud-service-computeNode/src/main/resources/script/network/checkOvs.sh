#!/bin/bash
if [ $# -ne 1 ]; then
  echo "params num not match"
  exit 1
fi
ovs-vsctl show | grep Port\ \"$1\"
