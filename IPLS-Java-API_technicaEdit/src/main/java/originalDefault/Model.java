package originalDefault;
import io.ipfs.api.Sub;
import org.apache.commons.cli.*;
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

public class Model {
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

    public static void save_model(String path, MultiLayerNetwork model) throws IOException{
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject((INDArray)model.params());
        oos.flush();
        oos.close();
        fos.close();
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
        int rngSeed = 123; // random number seed for reproducibility
        double rate = 0.0015; // learning rate
        int i;
        DataSetIterator mnistTrain = null, mnistTest = null;
        parse_arguments(args);
        
        // Only load datasets if not the bootstrapper
        if(!isBootstraper){
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

        INDArray orig_tmp = model.params(); //Nd4j.zeros(1,model.params().length());
        INDArray orig = Nd4j.zeros(1,model.params().length());
        INDArray first = Nd4j.zeros(1,model.params().length());

        for(int j = 0; j < model.params().length(); j++){
        	orig.put(0,j, orig.getDouble(j));
        }
        
        /* =================================================================================== */

        Sub SUB = new Sub(topic+"reply",Path,queue,true);
        SUB.start();

        //DebugConsole console = new DebugConsole(ipls);
        //console.start();

        INDArray TotalInput = Nd4j.zeros(model.params().length(),784);
        INDArray TotalLabels = Nd4j.zeros(model.params().length(),10);

        INDArray Dumm = Nd4j.zeros(1, model.params().length());
        INDArray gradient = Nd4j.zeros(1,80730);
        List<Double> arr = new ArrayList<>();
        List<Double> acc = new ArrayList<>();

        // CREATE IPLS OBJECT
        ipls = new IPLS();

        System.out.println(Path);
        System.out.println('/'+DataPath+'/'+"ETHModel");
        System.out.println(Bootstrapers);
        System.out.println(isBootstraper);
        System.out.println(model.params().length());
        
        // START INITIALIZATION PHASE
        ipls.init(Path, '/'+DataPath+'/'+"ETHModel", Bootstrapers, isBootstraper, model.params().length());
        //ipls.init(Path,"/workspace/data/MNIST_Partitioned_Dataset/ETHModel",Bootstrapers,isBootstraper,model.params().length());

        //IF I AM BOOTSTRAPER THEN DO NOT CONTINUE
        if(isBootstraper){
            while (true){
            }
        }

        DataSet myData = new DataSet(TotalInput,TotalLabels);
        List<DataSet> Dlist = myData.asList();
        
        /*
        DataSetIterator mni = new ListDataSetIterator(Dlist,100);
        FileOutputStream fos = new FileOutputStream("/workspace/data/MNIST_Partitioned_Dataset/"+topic+"data");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(mni);
        oos.close();
        fos.close();
        */
        
        System.out.println(model.params().length());
        int x = new Integer(topic);
        System.out.println(model.params());
        // TRAIN THE MODEL
        for(i = 0; i < Epochs; i++){
            /* ===================================== */
            // GET THE GLOBAL PARTITIONS FROM THE IPLS SYSTEM
            arr = ipls.GetPartitions();            
            /* ===================================== */
            for(int j = 0; j < model.params().length(); j++){
               Dumm.put(0,j,arr.get(j));
            }

            if (i == 0) {                
                for(int j = 0; j < model.params().length(); j++){
                	first.put(0,j, Dumm.getDouble(j));
                }
            }
            
            model.setParams(Dumm);
            if (i > 0) {
                System.out.println("Evaluate model....");
                Evaluation eval = model.evaluate(mnistTest);
                System.out.println(eval.stats());
                System.out.println("****************Example finished********************");
            }
            // This method is going to train the model in only one processor so that you can run
            // many nodes when you are using only one pc and you want to experiment with IPLS.
            
            // Remote fit with computation server
            //remote_fit();
            local_fit(mnistTrain);        

            // In this function you get the W[i] - W[i-1] (difference of the locally updated model and the global model received from ipls.GetPartitions();)
            gradient = GetDiff(model.params(),Dumm);
            gradient = gradient.mul(1);

            
            //HERE GO UPDATE METHOD
            if(PeerData.isSynchronous){
                System.out.println("ITERATION : " + PeerData.middleware_iteration);
            }
            else{
                System.out.println("ITERATION : "+ i);
            }
            // UPDATE THE MODEL USING IPLS
            ipls.UpdateGradient(Doubles.asList(gradient.getRow(0).toDoubleVector()));

            System.gc();
            System.runFinalization();
        }
        arr = ipls.GetPartitions();
        for(int j = 0; j < model.params().length(); j++){
            Dumm.put(0,j,arr.get(j));
        }
        model.setParams(Dumm);
        System.out.println("Evaluate model....");

        Evaluation eval = model.evaluate(mnistTest);
        System.out.println(eval.stats());
        System.out.println("****************Example finished********************");
        save_model('/'+DataPath+'/'+"NEWModel", model);
        
//        System.out.println("-------------------------------------");
//        System.out.println("\nORIGINAL");
//        for(int j = 0; j < 10; j++){
//        	System.out.println("-->"+orig.getDouble(j)); 
//        }   
//        System.out.println("-------------------------------------");
//       
//        System.out.println("\nFIRST");
//        for(int j = 0; j < 10; j++){
//        	System.out.println("-->"+first.getDouble(j)); 
//        }        
//        System.out.println("-------------------------------------");
//
//        System.out.println("\nLAST");
//        for(int j = 0; j < 10; j++){
//        	System.out.println("-->"+Dumm.getDouble(j)); 
//        }    
        
        
//        File f = new File("DataRecv"+topic);
//        f.createNewFile();
//        
//        FileOutputStream fos = new FileOutputStream("DataRecv"+topic);
//        ObjectOutputStream oos = new ObjectOutputStream(fos);
//        oos.writeObject(PeerData.RecvList);
//        oos.close();
//        fos.close();
//        
//        f = new File("ChartData" + topic);
//        f.createNewFile();
//        
//        fos = new FileOutputStream("ChartData" + topic);
//        oos = new ObjectOutputStream(fos);
//        oos.writeObject(acc);
//        oos.close();
//        fos.close();
    }
}
