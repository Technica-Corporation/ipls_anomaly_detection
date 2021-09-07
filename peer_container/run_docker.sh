dir=$PWD
parentdir="$(dirname "$dir")"

docker run --rm -it \
    --name ipls_test_peer \
    --network host \
    --volume $PWD/resources/adConfig.json:/opt/ipls_peer/adConfig.json \
    --volume $PWD/data:/workspace/data \
    ad_demo/ipls_peer:1.0 /bin/bash /workspace/ipls_demo.sh