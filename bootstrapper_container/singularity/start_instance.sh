dir=$PWD
parentdir="$(dirname "$dir")"

singularity instance start \
    --no-mount hostfs \
    --writable-tmpfs \
    --bind $parentdir/resources/adConfig.json:/opt/ipls_bootstrapper/adConfig.json \
    bootstrapper.sif ipls_bootstrapper