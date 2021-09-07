dir=$PWD
parentdir="$(dirname "$dir")"

docker save ad_demo/ipls_bootstrapper:1.0 -o ipls_bootstrapper.tar
singularity build docker_bootstrapper.sif docker-archive://ipls_bootstrapper.tar
singularity build --fakeroot bootstrapper.sif bootstrapper.def

rm ipls_bootstrapper.tar
rm docker_bootstrapper.sif