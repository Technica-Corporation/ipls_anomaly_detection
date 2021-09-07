docker run --rm -it \
    --name ad_gui \
    --network host \
    -p 5601:5601 \
    ad-gui:latest /bin/bash