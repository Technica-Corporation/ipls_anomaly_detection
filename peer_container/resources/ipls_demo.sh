configFilePath="/opt/ipls_peer/adConfig.json"

# Initialize IPFS
export IPFS_PATH="/workspace/.ipfs"
ipfs init

# Add swarm key for private network
cp /workspace/swarm.key $IPFS_PATH

# Add bootstrapper configs
ipfs bootstrap rm --all
for val in $(cat $configFilePath | jq '.ipfsBootstrapperAddresses'); do
    if [ "$val" != "[" ] && [ "$val" != "]" ]; then
        val=`echo "$val" | sed 's/[",]//g'`
        ipfs bootstrap add $val
    fi
done

export LIBP2P_FORCE_PNET=1
if [ ! -e $IPFS_PATH/api ]
then 
    ipfs daemon --enable-pubsub-experiment &
else
   echo "IPFS already started"
fi
while [ ! -e $IPFS_PATH/api ]
do 
   echo "Waiting for IPFS to start";
   sleep 10
done

sleep 5

# The IPFS Path for the communication of the IPLS with the IPFS daemon.
ipfsAddress="$(ipfs config Addresses.API)"

echo "--------"
java -cp /workspace/java-ipfs-http-client-v1.2.3.jar AnomalyDetectionDriver \
-a $ipfsAddress \
-c $configFilePath
echo "--------"

ipfs shutdown