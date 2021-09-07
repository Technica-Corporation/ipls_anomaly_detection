package anomalydetection.train;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerMinMaxScaler;
import org.nd4j.linalg.dataset.api.preprocessor.serializer.NormalizerSerializer;

/**
 * Train an Anomaly Detection Model like Smartfog's "Federated Learning Anomaly Detection" in the DeepLearning4J framework
 * 
 * @author mbernardo
 *
 */
public class AdTrainModel {
	public static final String MODEL_PATH = "E:\\\\IPFS\\\\AD_DATA\\\\ad_model";
	public static final String SCALER_PATH = "E:\\IPFS\\AD_DATA\\scaler";
	public static final String TRAIN_DATA_PATH = "E:\\IPFS\\AD_DATA\\full_training";
	//public static final String TRAIN_DATA_PATH = "E:\\IPFS\\AD_DATA\\training";

	public static final String FULL_DATA_PATH = "E:\\IPFS\\AD_DATA\\full_data";
    public static final int EPOCHS = 20;
    public static final int BATCH_SIZE = 32;
    
	public static void main(String[] args) throws FileNotFoundException {		
		// Get Training dataset
		List<List<String>> records = AdUtils.getRecords(TRAIN_DATA_PATH);
		
		// Get set of all partitions combined to fit the Normalizer
		List<List<String>> records2 = AdUtils.getRecords(FULL_DATA_PATH);

		// Load in Model configuration/definition
		MultiLayerNetwork model = new MultiLayerNetwork(AnomalyDetectionModel.conf);
		model.init();
		model.setListeners(new ScoreIterationListener(1)); // print the score with every iteration
		
		// Read in Data from CSV files and convert to dl4j DataSet
		DataSet fullData = AdUtils.getDataset(records2);

		// Initialize normalizer, transform data, and save to file
		DataNormalization normalizer = new NormalizerMinMaxScaler(0, 1);
		normalizer.fit(fullData);	    

		// Pulls in data from a .csv file and uses the normalizer to transform the features and labels
		DataSet myData = AdUtils.getTrainingDataset(records, normalizer);
		
        List<DataSet> Dlist = myData.asList();
        DataSetIterator iter = new ListDataSetIterator(Dlist, BATCH_SIZE);
  
        // Train
        if(EPOCHS > 0) {
            model.fit(iter, EPOCHS);
        }
              
        // Save Model and Scaler to File
        try {
            File modelFile = new File(MODEL_PATH);
            File normalizerFile = new File(SCALER_PATH);
            NormalizerSerializer s = NormalizerSerializer.getDefault();
			model.save(modelFile);
			s.write(normalizer, normalizerFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
                
        System.out.println("--Finished----");
        System.out.println("    Epochs: "+EPOCHS);        
        System.out.println("    Batch Size: "+BATCH_SIZE);
	}
}
