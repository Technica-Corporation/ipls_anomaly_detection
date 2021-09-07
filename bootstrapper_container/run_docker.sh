dir=$PWD
parentdir="$(dirname "$dir")"

docker run --rm -it \
    --name ipls_test_bootstrapper \
    --network host \
    --volume $PWD/resources/adConfig.json:/opt/ipls_bootstrapper/adConfig.json \
    ad_demo/ipls_bootstrapper:1.0 /bin/bash /workspace/ipls_demo.sh