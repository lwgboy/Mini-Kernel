#!/bin/bash

path=$1

if [[ -z $path ]]; then
    echo "parameter not set"
    exit 1  # path is empty
fi

while [[ $path == */ ]]; do
    path=${path%/}
done
echo "path=$path"

mnt=`mount | grep -w $path | grep -E "fuse.ceph-fuse | nfs"`
if [[ -z $mnt ]]; then
    echo "$path not mounted"
    exit 2  # path not mounted
fi

echo "good"
