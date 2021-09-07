dir=$PWD
parentdir="$(dirname "$dir")"

docker save ad_demo/ipls_peer:1.0 -o ipls_peer.tar
singularity build docker_peer.sif docker-archive://ipls_peer.tar
singularity build --fakeroot peer.sif peer.def

rm ipls_peer.tar
rm docker_peer.sif