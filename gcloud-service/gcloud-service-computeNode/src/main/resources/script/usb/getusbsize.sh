#!/bin/bash
fdisk -l | grep "Disk /dev" |grep -v 1073741824| awk '{print $5}'