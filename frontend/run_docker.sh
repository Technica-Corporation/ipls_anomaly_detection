#/bin/bash

#
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

if [ ! "$(docker ps -q -f name=ad_gui)" ]; then

    if [ "$(docker ps -aq -f status=exited -f name=ad_gui)" ]; then
        # cleanup
        docker rm ad_gui
    fi

    # run your container
    #docker run --net="host" -d -p 5601:5601 ad-gui 
    docker run -d \
        --name ad_gui \
        --network host \
        -p 5601:5601 \
        ad-gui:latest
fi




