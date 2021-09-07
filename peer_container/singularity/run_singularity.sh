dir=$PWD
parentdir="$(dirname "$dir")"

singularity run \
    --no-mount hostfs \
    --writable-tmpfs \
    --bind $parentdir/resources/adConfig.json:/opt/ipls_peer/adConfig.json \
    peer.sif ipls_peer