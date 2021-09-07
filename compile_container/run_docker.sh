#!/bin/bash

dir=$PWD
parentdir="$(dirname "$dir")"

FLAG=""

while getopts ":l" option; do
   case $option in
      l) FLAG="-l";;
   esac
done

docker run --rm -it \
    --name ipls_compile \
    --volume $parentdir/IPLS-Java-API_technicaEdit:/workspace/IPLS-Java-API \
    --volume $parentdir/resources:/workspace/resources \
    ipls_ad/compile:1.0 /bin/bash /workspace/compile.sh ${FLAG}