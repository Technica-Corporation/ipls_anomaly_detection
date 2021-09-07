FROM openjdk:jdk-buster

RUN mkdir /workspace
WORKDIR "/workspace" 

RUN apt-get update && \
    apt-get install -y --no-install-recommends wget ca-certificates libc6 locales

RUN wget https://mirror.nodesdirect.com/apache/maven/maven-3/3.8.1/binaries/apache-maven-3.8.1-bin.tar.gz && \
    tar xvf apache-maven-3.8.1-bin.tar.gz

COPY resources/compile.sh /workspace

ENV PATH="/workspace/apache-maven-3.8.1/bin:$PATH"
ENV LC_CTYPE=en_US.UTF-8
ENV LC_ALL=C.UTF-8
ENV LANG C.UTF-8