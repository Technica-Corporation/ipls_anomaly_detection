#/bin/bash

#
#
if [ ! "$(docker ps -q -f name=ipls_mqtt)" ]; then

    if [ "$(docker ps -aq -f status=exited -f name=ipls_mqtt)" ]; then
        # cleanup
        docker rm ipls_mqtt
    fi

    # run your container
    echo "STARTING THE MQTT CONTAINER"
    docker run -d --name ipls_mqtt \
    --network host \
    -u root \
    --volume $PWD/resources/mosquitto.conf:/mosquitto/config/mosquitto.conf:ro \
    eclipse-mosquitto

else
    docker stop ipls_mqtt
    echo "STOPPING THE MQTT CONTAINER"
    if [ "$(docker ps -aq -f status=exited -f name=ipls_mqtt)" ]; then
        # cleanup
        docker rm ipls_mqtt
    fi
fi

