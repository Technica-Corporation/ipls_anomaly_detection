REPO="IPLS-Java-API"
JAR="java-ipfs-http-client-v1.2.3.jar"

cd /workspace/$REPO
mvn clean package
cp /workspace/$REPO/target/$JAR /workspace/resources
chmod 777 /workspace/resources/$JAR

while getopts ":l" option; do
   case $option in
      l) cp -R /workspace/$REPO/target/libs /workspace/resources;
         cp -R /workspace/$REPO/target/libs /workspace/resources;
         chmod -R 777 /workspace/resources/libs;;
   esac
done

rm -R /workspace/$REPO/target

