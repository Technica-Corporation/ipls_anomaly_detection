#!/bin/bash

dir=$PWD
parentdir="$(dirname "$dir")"
SCRIPT=""

while getopts "an" option; do
    case "${option}" in
        a) SCRIPT="anomaly_data.sh"
           ;;
        n) SCRIPT="normal_data.sh"
           ;;
    esac
done

#
# Start MQTT container if it isnt running
#
if [ ! "$(docker ps -q -f name=ipls_mqtt)" ]; then

    if [ "$(docker ps -aq -f status=exited -f name=ipls_mqtt)" ]; then
        # cleanup
        docker rm ipls_mqtt
    fi

    # run your container
    docker run -d --name ipls_mqtt \
    --network host \
    -u root \
    --volume $PWD/resources/mosquitto.conf:/mosquitto/config/mosquitto.conf:ro \
    eclipse-mosquitto
fi

#
# Run Generator container
#
docker run --rm -it \
    --name ipls_generator \
    --network host \
    --volume $PWD/resources/generators.conf:/workspace/generators.conf \
    --volume $parentdir/resources/data:/workspace/data \
    ipls_ad/generator:1.0 /bin/bash ${SCRIPT}

