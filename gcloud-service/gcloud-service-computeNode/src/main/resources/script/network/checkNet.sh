#!/bin/bash
if [ $# -ne 1 ]; then
  echo "params num not match"
  exit 1
fi
ifconfig | grep ^$1:
