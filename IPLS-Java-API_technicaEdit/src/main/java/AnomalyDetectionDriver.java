
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.weights.WeightInit;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import anomalydetection.TrainParameters;
import anomalydetection.handler.AdBootstrapperHandler;
import anomalydetection.handler.AdInferenceHandler;
import anomalydetection.handler.AdMqttHandler;
import anomalydetection.handler.AdTrainHandler;
import anomalydetection.train.AdUtils;
import originalDefault.IPLS;
import originalDefault.PeerData;

public class AnomalyDetectionDriver {
	public static final String CONFIG_FILE = "adConfig.json";
	public static IPLS ipls = null;
	public static List<String> Bootstrappers = new ArrayList<>();
	public static boolean isBootstrapper;
	public static boolean DONE = false;

	public static void main(String[] args) {
		String protocol, broker, userName, password, mqttUrl, subTopic, pubTopic, modelPath, scalerPath;
		String ipfsAddress, ipfsBootstrapperId;
		int port, epochs, iplsModelPartitions, iplsMinPartitions, iplsMinPeers, updateFrequency, stabilityCount;
		List<String> subTopics = new ArrayList<String>();
		double learningRate;
		boolean iplsIsBootstrapper, peerDataisSynchronous;
		TrainParameters trainParams;
		
		Options options = new Options();
		Option path = new Option("a", "address", true, "The address of the IPFS API");
		path.setRequired(true);
		options.addOption(path);

		Option configFile = new Option("c", "config", true, "The Configuratio File for Anomaly Detection");
		configFile.setRequired(true);
		options.addOption(configFile);

		DefaultParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;

		System.out.println("Start");
		Thread bootstrapperHandler = null, mqttHandler = null, inferenceHandler = null, trainHandler = null;
		
		// Get Args values
		try {
			cmd = parser.parse(options, args);
			ipfsAddress = cmd.getOptionValue("address");
			String jsonString = AdUtils.readLineByLineJava8(cmd.getOptionValue("config"));
			JSONObject jsonConfig = new JSONObject(jsonString);

			// Get Configuration file values
			modelPath = jsonConfig.getString("modelPath");
			scalerPath = jsonConfig.getString("scalerPath");
			protocol = jsonConfig.getString("mqttProtocol");
			broker = jsonConfig.getString("mqttBrokerip");
			port = jsonConfig.getInt("mqttPort");
			password = null;
			userName = null;
			mqttUrl = protocol + broker + ":" + port;
			subTopic = jsonConfig.getString("mqttSubTopic");
			subTopics.add(subTopic);
			subTopics.add("exit");
			pubTopic = jsonConfig.getString("mqttPubTopic");
			epochs = jsonConfig.getInt("epochs");
			learningRate = jsonConfig.getDouble("learningRate");
			ipfsAddress = jsonConfig.getString("ipfsAddress");
			//ipfsBootstrapperId = jsonConfig.getString("ipfsBootstrapperId");
			JSONArray bootstrappers = jsonConfig.getJSONArray("ipfsBootstrapperIds");
			
			iplsModelPartitions = jsonConfig.getInt("iplsModelPartitions");
			iplsMinPartitions = jsonConfig.getInt("iplsMinPartitions");
			iplsMinPeers = jsonConfig.getInt("iplsMinPeers");
			iplsIsBootstrapper = jsonConfig.getBoolean("iplsIsBootstrapper");
			peerDataisSynchronous = jsonConfig.getBoolean("peerDataisSynchronous");
			updateFrequency = jsonConfig.getInt("updateFrequency");
			stabilityCount = jsonConfig.getInt("stabilityCount");

			// IPLS configuration (Moslty ripped from IPLS original Example)
			PeerData._PARTITIONS = iplsModelPartitions;
			PeerData._MIN_PARTITIONS = iplsMinPartitions;
			PeerData.isBootsraper = iplsIsBootstrapper;
			PeerData.Min_Members = new Integer(iplsMinPeers);
			PeerData.isSynchronous = peerDataisSynchronous; // We probably want this false?

			// Handle multiple bootstrappers later
			//Bootstrappers.add(ipfsBootstrapperId);
			for (int i = 0; i < bootstrappers.length(); i++) { // Walk through the Array.
	            String bootstrapperId = (String) bootstrappers.get(i);
	            Bootstrappers.add(bootstrapperId);
			}
			
			// Define Model to use
			// -> Federated Learning Anomaly Detection Model <-
			int rngSeed = 123;
			// double learningRate = 0.001;
			MultiLayerConfiguration modelConf = new NeuralNetConfiguration.Builder().seed(rngSeed)
					.weightInit(WeightInit.XAVIER).updater(new Adam(learningRate)) // Original Learning rate used in original training = 0.001
					.activation(Activation.RELU).list().layer(new DenseLayer.Builder().nIn(4).nOut(64).build())
					.layer(new DenseLayer.Builder().nIn(64).nOut(32).build())
					.layer(new DenseLayer.Builder().nIn(32).nOut(16).build())
					.layer(new DenseLayer.Builder().nIn(16).nOut(32).build())
					.layer(new DenseLayer.Builder().nIn(32).nOut(64).build())
					.layer(new OutputLayer.Builder().nIn(64).nOut(4).lossFunction(LossFunctions.LossFunction.XENT)
							.activation(Activation.SIGMOID).build())
					.build();

			if (!iplsIsBootstrapper) {
		        BlockingQueue<DataSet> inferenceInputQueue = new LinkedBlockingDeque<>();
		        BlockingQueue<String> inferenceOutputQueue = new LinkedBlockingDeque<>();
		        BlockingQueue<DataSet> trainingInputQueue = new LinkedBlockingDeque<>();
		        BlockingQueue<INDArray> modelUpdateQueue = new LinkedBlockingDeque<>();
	   
		        trainParams = new TrainParameters(modelConf, epochs, updateFrequency, learningRate, stabilityCount);

		        trainHandler = new Thread(new AdTrainHandler(trainParams, modelPath, ipfsAddress, Bootstrappers, iplsIsBootstrapper, 5844, trainingInputQueue, modelUpdateQueue));	
		        inferenceHandler = new Thread(new AdInferenceHandler(modelConf, modelPath, inferenceInputQueue, inferenceOutputQueue, modelUpdateQueue));
				mqttHandler = new Thread(new AdMqttHandler(mqttUrl, MqttAsyncClient.generateClientId(), true, false, subTopics, pubTopic, scalerPath, inferenceInputQueue, inferenceOutputQueue, trainingInputQueue));

                trainHandler.start();
                inferenceHandler.start();
                mqttHandler.start();
				
                trainHandler.join();
                inferenceHandler.join();
                mqttHandler.join();
			} else {
		        bootstrapperHandler = new Thread(new AdBootstrapperHandler(modelPath, ipfsAddress, Bootstrappers, iplsIsBootstrapper, 5844));	
		        bootstrapperHandler.start();
		        bootstrapperHandler.join();
			}
	        
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("utility-name", options);
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(!Objects.isNull(ipls)) {
				try {
					ipls.terminate();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("IPLS -- FAILED TERMINATION");
				}
			}
			if(!Objects.isNull(mqttHandler)) {
				mqttHandler.interrupt();	
			}
			if(!Objects.isNull(inferenceHandler)) {
				inferenceHandler.interrupt();	
			}
			if(!Objects.isNull(trainHandler)) {
				trainHandler.interrupt();	
			}	
			if(!Objects.isNull(bootstrapperHandler)) {
				bootstrapperHandler.interrupt();	
			}	
			
            System.gc();
            System.runFinalization();
		}
		System.out.println("End");
	}
}
