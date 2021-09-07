docker run --rm -it \
    --name ipls_bootstrap_address \
    --network host \
    ad_demo/ipls_bootstrapper:1.0 /bin/bash /workspace/get_address.sh