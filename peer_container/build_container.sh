#!/bin/bash
dir=$PWD
parentdir="$(dirname "$dir")"
JAR="java-ipfs-http-client-v1.2.3.jar"

cp $parentdir/resources/$JAR $PWD/resources/$JAR
cp -R $parentdir/resources/libs $PWD/resources/libs

cp $parentdir/resources/ad_ipls_params $PWD/resources/ad_ipls_params
cp $parentdir/resources/scaler $PWD/resources/scaler
cp $parentdir/resources/swarm.key $PWD/resources/swarm.key

docker build -t ad_demo/ipls_peer:1.0 .

rm $PWD/resources/$JAR
rm -R $PWD/resources/libs
rm $PWD/resources/ad_ipls_params
rm $PWD/resources/scaler
rm $PWD/resources/swarm.key