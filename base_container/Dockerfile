FROM openjdk:8u302-jre-slim-buster

RUN mkdir /workspace
WORKDIR "/workspace" 

RUN apt-get update && \
    apt-get install -y --no-install-recommends wget ca-certificates curl jq libgomp1

RUN export arch="$(dpkg --print-architecture)";  \
    case "$arch" in \
        amd64) wget https://golang.org/dl/go1.16.3.linux-amd64.tar.gz; \
               tar -C /usr/local -xzf go1.16.3.linux-amd64.tar.gz; \
               wget https://dist.ipfs.io/go-ipfs/v0.7.0/go-ipfs_v0.7.0_linux-amd64.tar.gz; \
               tar -xvf go-ipfs_v0.7.0_linux-amd64.tar.gz; \
               mv go-ipfs/ipfs /usr/local/bin; \
               rm go-ipfs_v0.7.0_linux-amd64.tar.gz; \
               rm go1.16.3.linux-amd64.tar.gz;; \
        arm64) wget https://golang.org/dl/go1.16.3.darwin-arm64.tar.gz; \
               tar -C /usr/local -xzf go1.16.3.darwin-arm64.tar.gz; \
               wget https://dist.ipfs.io/go-ipfs/v0.7.0/go-ipfs_v0.7.0_linux-arm64.tar.gz; \
               tar -xvf go-ipfs_v0.7.0_linux-arm64.tar.gz; \
               mv go-ipfs/ipfs /usr/local/bin; \
               rm go-ipfs_v0.7.0_linux-arm64.tar.gz; \
               rm go1.16.3.darwin-arm64.tar.gz;; \
    esac; 

ENV PATH="$PATH:/usr/local/go/bin"
ENV LC_CTYPE=en_US.UTF-8
ENV LC_ALL=C.UTF-8
ENV LANG C.UTF-8