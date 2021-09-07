peerid="$(ipfs config Identity.PeerID)"
bootstrapip="$(getent hosts $(hostname) | awk '{ print $1 }')"
command="/ip4/$bootstrapip/tcp/4001/ipfs/$peerid"


echo "----------"
echo "PEER ID: "
echo $peerid
echo "----------"
echo "BOOTSTRAP CONFIG ENTRY: "
echo $command
echo "----------"