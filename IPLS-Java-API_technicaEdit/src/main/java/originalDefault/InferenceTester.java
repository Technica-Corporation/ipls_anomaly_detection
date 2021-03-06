package originalDefault;
import io.ipfs.api.Sub;

import org.apache.commons.cli.*;
import org.deeplearning4j.datasets.iterator.impl.*;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import io.ipfs.api.IPFS;
import org.nd4j.shade.guava.primitives.Doubles;
import java.io.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// This is an example of use of IPLS. In this example you can start at most 5 peers just because i created only 5 different datasets.
// The datasets and other related files are on the file MNIST_Partitioned_Dataset
// If you want to run this program, then you should follow these steps:
// 1) Create a private IPFS network
// 2) Start a Model process for the bootstraper process with : -a /ip4/127.0.0.1/tcp/(Port_number of the IPFS API address of that node) -topic 1 -bID XXXX(the peers ID) -p 16 -mp 2 -b true -n 4
// 3) Start the Computational_Server in order not to train ever model in parallel (otherwise it might destroy your PC ) with : /ip4/127.0.0.1/tcp/YYYY
// The start each peer and for each of the 4 peers use the bellow :
// 4) -a /ip4/127.0.0.1/tcp/(Port_number of the IPFS API address of that node) -topic 2 -bID XXXX (Bootstrapers hash id) -p 16 -mp 4 -b false -n 4 -IPNS false
// 5) -a /ip4/127.0.0.1/tcp/(Port_number of the IPFS API address of that node) -topic 3 -bID XXXX (Bootstrapers hash id) -p 16 -mp 4 -b false -n 4 -IPNS false
// 6) -a /ip4/127.0.0.1/tcp/(Port_number of the IPFS API address of that node) -topic 4 -bID XXXX (Bootstrapers hash id) -p 16 -mp 4 -b false -n 4 -IPNS false
// 7) -a /ip4/127.0.0.1/tcp/(Port_number of the IPFS API address of that node) -topic 5 -bID XXXX (Bootstrapers hash id) -p 16 -mp 4 -b false -n 4 -IPNS false


/*
    In order to start an IPLS project, you must define also some important hyper parameters which are:
        * The IPFS Path for the communication of the IPLS with the IPFS daemon.
        * The number of partitions you want to partition the model. All the peers must be informed with the same partition number.
        * The minimum number of responsibilities you want the peers to have. For example you can decide to partition the model in 16 segments and each peer must be responsible for at least 2 of the 16 segments.
        * A boolean value to inform the peer if he is bootstraper of the private network or not.
        * The IPFS hash id of the bootstraper peer.
        * The minimum amount of peers that must be gathered before they proceed to the training phase.
        * A boolean value indicating if you the system runs Synchronous Gradient Descent or Asynchronous Gradient Descent
*/
public class InferenceTester {
	public static IPLS ipls;
    public static String topic;
    public  static MultiLayerNetwork model;
    public static List<String> Bootstrapers = new ArrayList<>();
    public static boolean isBootstraper;
    public static String Path;
    public static String DataPath;
    public static int Epochs;
     
    public static INDArray GetDiff(INDArray Dumm,INDArray model){
        return model.sub(Dumm);
    }
    public static INDArray GetGrad(MultiLayerNetwork model){
        return model.getGradientsViewArray();
    }

    //For computational server
    public static BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
    public static String taskReply;

    public static void remote_fit() throws Exception {
        IPFS ipfs = new IPFS(PeerData.Path);
        FileOutputStream fos = new FileOutputStream(topic);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject((INDArray)model.params());
        System.out.println("old params");
        System.out.println(model.params());
        oos.flush();
        oos.close();
        fos.close();
        ipfs.pubsub.pub("server",topic);
        taskReply = queue.take();
        System.out.println(taskReply);
        FileInputStream bis = new FileInputStream(topic);
        ObjectInput in = new ObjectInputStream(bis);
        model.setParams((INDArray) in.readObject());
        System.out.println(model.params());
        System.out.println("new params");
        in.close();
        bis.close();
    }
    
    public static void local_fit(DataSetIterator mni){
        model.fit(mni,1);
    }

    public static void parse_arguments(String[] args){

        Options options = new Options();
        Option path = new Option("a", "address", true, "The address of the IPFS API");
        path.setRequired(true);
        options.addOption(path);

        Option partitions = new Option("p", "partitions", true, "The number of partitions you want to partition the model");
        partitions.setRequired(true);
        options.addOption(partitions);

        Option minimum_partitions = new Option("mp", "minimum_partitions", true, "The minimum number of partitions a peer required to be responsible for");
        minimum_partitions.setRequired(true);
        options.addOption(minimum_partitions);


        Option is_bootstraper = new Option("b", "is_bootstraper", true, "If true then the process becomes bootstraper. Note that this process must be the first process of the system");
        is_bootstraper.setRequired(true);
        options.addOption(is_bootstraper);

        Option min_peers = new Option("n", "min_peers", true, "The minimum number of peers required to proceed to training phase");
        min_peers.setRequired(true);
        options.addOption(min_peers);

        // This variable is not of importance but it helps for the file names of the dataset.
        Option my_id_number = new Option("topic", "id_number",true,"The id number of the peer");
        my_id_number.setRequired(false);
        options.addOption(my_id_number);

        Option Bootstraper = new Option("bID", "Bootstraper_ID",true,"The bootstrapers hash id");
        my_id_number.setRequired(true);
        options.addOption(Bootstraper);

        Option IPNS = new Option("IPNS", "IPNS",true,"Provide Indirect communication, instead of using message passing protocols use IPFS file system capabilities");
        my_id_number.setRequired(false);
        options.addOption(IPNS);

        Option asynchronous = new Option("async", "Async",true,"If is true then you turn the protocol in asynchronous mod where you do not have to wait for others to complete the iteration");
        my_id_number.setRequired(false);
        options.addOption(asynchronous);

        Option dataPath = new Option("d", "datapath", true, "Location of the datasets");
        dataPath.setRequired(true);
        options.addOption(dataPath);
        
        Option epochs = new Option("e", "epochs", true, "Number of epochs for training");
        epochs.setRequired(true);
        options.addOption(epochs);
        
        
        DefaultParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            Path = cmd.getOptionValue("address");
            
            Epochs = Integer.parseInt(cmd.getOptionValue("epochs")); 
            DataPath = cmd.getOptionValue("datapath");
            
            PeerData._PARTITIONS  = new Integer(cmd.getOptionValue("partitions"));
            PeerData._MIN_PARTITIONS = new Integer(cmd.getOptionValue("minimum_partitions"));
            isBootstraper = new Boolean(cmd.getOptionValue("is_bootstraper"));
            PeerData.isBootsraper = isBootstraper;

            PeerData.Min_Members = new Integer(cmd.getOptionValue("min_peers"));
            topic =  cmd.getOptionValue("id_number");
            Bootstrapers.add(cmd.getOptionValue("Bootstraper_ID"));

            if(cmd.getOptionValue("IPNS") != null){
                if(cmd.getOptionValue("IPNS").equals("true")){
                    PeerData.IPNS_Enable = true;
                }
            }
            if(cmd.getOptionValue("async") != null){
                if(cmd.getOptionValue("async").equals("true")){
                    PeerData.isSynchronous = false;
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("BEGIN");

    	final int numRows = 28;
        final int numColumns = 28;
        int outputNum = 10; // number of output classes
        int batchSize = 100; // batch size for each epoch
        int rngSeed = 123; // random number seed for reproducibility
        int numEpochs = 15; // number of epochs to perform
        double rate = 0.0015; // learning rate
        int i;
        DataSetIterator lfw = null,mnistTrain = null,mnistTest = null;
        parse_arguments(args);
        try {

            FileInputStream fis = new FileInputStream('/'+DataPath+'/'+ topic + "TrainDataset");
        	//FileInputStream fis = new FileInputStream("/workspace/data/MNIST_Partitioned_Dataset/" + topic + "TrainDataset");
            ObjectInput fin = new ObjectInputStream(fis);
            mnistTrain = (DataSetIterator) fin.readObject();
            System.out.println(mnistTrain);
            System.out.println("OKKKK");
            
            fis = new FileInputStream('/'+DataPath+'/'+"MnistTest");
            //fis = new FileInputStream("/workspace/data/MNIST_Partitioned_Dataset/"+"MnistTest");
            fin = new ObjectInputStream(fis);
            mnistTest = (DataSetIterator) fin.readObject();
        }
        catch (Exception e){
        	System.out.print(e);
        	
            System.out.println("Could not find iterator ");
            System.exit(-1);
        }
        /* =================================================================================== */

        ///                             CONFIGURE YOUR MODEL

        /* =================================================================================== */
        // NOTE!!! You can use whatever framework you want as long as for each training round you can receive the
        // new parameters of the model and also change the weights of the model.
        /*
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(rngSeed) //include a random seed for reproducibility
                .updater(new Sgd(0.1))
                .activation(Activation.RELU)
                .weightInit(WeightInit.XAVIER)
                .l2(rate * 0.005) // regularize learning model
                .list()
                .layer(new DenseLayer.Builder() //create the first input layer.
                        .nIn(numRows * numColumns)
                        .nOut(500)
                        .build())
                .layer(new DenseLayer.Builder() //create the second input layer
                        .nIn(500)
                        .nOut(100)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD) //create hidden layer
                        .activation(Activation.SOFTMAX)
                        .nOut(outputNum)
                        .build())
                .build();

		*/
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(rngSeed) //include a random seed for reproducibility
                .updater(new Sgd(0.1))
                .activation(Activation.RELU)
                .weightInit(WeightInit.XAVIER)
                .l2(rate * 0.005) // regularize learning model
                .list()
                .layer(new DenseLayer.Builder() //create the first input layer.
                        .nIn(numRows * numColumns)
                        .nOut(20)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD) //create hidden layer
                        .activation(Activation.SOFTMAX)
                        .nOut(outputNum)
                        .build())
                .build();


        model = new MultiLayerNetwork(conf);
        model.init();
        model.setListeners(new ScoreIterationListener(1));  //print the score with every iteration


        /* =================================================================================== */

        Sub SUB = new Sub(topic+"reply",Path,queue,true);
        SUB.start();

        //Console console = new Console();
        //console.start();

        INDArray TotalInput = Nd4j.zeros(model.params().length(),784);
        INDArray TotalLabels = Nd4j.zeros(model.params().length(),10);

        INDArray Dumm = Nd4j.zeros(1,model.params().length());
        INDArray gradient = Nd4j.zeros(1,80730);
        List<Double> arr = new ArrayList<>();
        List<Double> acc = new ArrayList<>();

        // CREATE IPLS OBJECT
        ipls = new IPLS();

        // START INITIALIZATION PHASE
        ipls.init(Path,'/'+DataPath+'/'+"ETHModel",Bootstrapers,isBootstraper,model.params().length());
        //ipls.init(Path,"/workspace/data/MNIST_Partitioned_Dataset/ETHModel",Bootstrapers,isBootstraper,model.params().length());

        DataSet myData = new DataSet(TotalInput,TotalLabels);
        List<DataSet> Dlist = myData.asList();
                
        System.out.println(model.params().length());
        int x = new Integer(topic);
        System.out.println(model.params());

        //
        
        arr = ipls.GetPartitions();
        for(int j = 0; j < model.params().length(); j++){
            Dumm.put(0,j,arr.get(j));
        }
        model.setParams(Dumm);
        System.out.println("Evaluate model....");

        Evaluation eval = model.evaluate(mnistTest);
        System.out.println(eval.stats());
        System.out.println("****************Example finished********************");
        
        File f = new File("DataRecv"+topic);
        f.createNewFile();
        
        FileOutputStream fos = new FileOutputStream("DataRecv"+topic);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(PeerData.RecvList);
        oos.close();
        fos.close();
        
        f = new File("ChartData" + topic);
        f.createNewFile();
        
        fos = new FileOutputStream("ChartData" + topic);
        oos = new ObjectOutputStream(fos);
        oos.writeObject(acc);
        oos.close();
        fos.close();
    }
}
