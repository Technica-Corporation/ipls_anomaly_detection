FROM ad_demo/ipfs_base:1.0

ENV IPFS_PATH="/workspace/.ipfs"

COPY resources/libs /workspace/libs
COPY resources/java-ipfs-http-client-v1.2.3.jar /workspace/
COPY resources/ad_ipls_params resources/scaler resources/swarm.key /workspace/
COPY resources/ipls_demo.sh /workspace

CMD bash /workspace/ipls_demo.sh
