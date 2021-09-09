# IPLS_Java_API_technicaEdit

This is the main code for IPLS; it is from the Github Repository [IPLS-Java-API](https://github.com/ChristodoulosPappas/IPLS-Java-API) by ChristodoulosPappas.

You may want to read the included presentation pdf to get a better understanding on how IPLS itself works.

The repository is included here as we have made a few changes:

- Everything in the 'default' package was moved into a new package called 'originalDefault'

- An AnomalyDetectionDriver.java was added to the 'default' package

- An 'anomalydetection' package was added which includes all our code for IPLS Anomaly Detection

- In the pom.xml the version for deeplearning4j and nd4j was upgraded to 1.0.0-M1.1

  > **_NOTE:_**  The previous version pulled in a specific version of OpenBlas that has a bug on Arm64 platforms causing calculations to sporadically result in NaN.



Everything below this point is the READE included with the original IPLS_Java_API repository

------



# Java IPLS Client



> A Java client for the IPFS http api
### IPFS installation

#### Command line

Download ipfs from https://dist.ipfs.io/#go-ipfs and run with `ipfs daemon --enable-pubsub-experiment`

## Usage

Create an IPFS instance with:
```Java
IPLS ipls = new IPLS();
```

To initialize and get model partition responsibilities select IPFS Path (in the example is "/ip4/127.0.0.1/tcp/5001") and use:
```Java
ipls.init("/ip4/127.0.0.1/tcp/5001",String Path_of_model_file,List<String> Bootstrapers,boolean is_bootstraper,int model_size);
```


Then to Update model Gradients after an iteration or a set of iterations use:
```Java
ipls.UpdateGradient(List<Double> Gradients);
```

To get a new the updated model from the distributed shared memory use:
```Java
 List<Double> Parameters = ipls.GetPartitions();
```

## Useful Notations
* IPLS (Inter-Planetary Learning System) is a framework for decentralized Federated Learning.
For more information you can read the whitepaper given this link : https://arxiv.org/pdf/2101.01901.pdf .

* If you want to see how to use the API check the Model.java located at the /src/main/java. 

* For a quick explanation of the protocol check the given pdf file.
## License
[MIT](LICENSE)
[Παπαγιώργη!](LICENSE)
