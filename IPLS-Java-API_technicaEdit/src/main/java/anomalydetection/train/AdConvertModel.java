package anomalydetection.train;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.shade.guava.primitives.Doubles;

/**
 * Take a trained dl4j model and save it as a file containing a list of doubles in the format IPLS is expecting
 * 
 * @author mbernardo
 *
 */
public class AdConvertModel {
	public static final String MODEL_PATH = "E:\\IPFS\\AD_DATA\\ad_model";
	public static final String SCALER_PATH = "E:\\IPFS\\AD_DATA\\scaler";
	public static final String TRAIN_DATA_PATH = "E:\\IPFS\\AD_DATA\\training";
	public static final String FULL_ANOMALIES_PATH = "E:\\IPFS\\AD_DATA\\full_anomalies";
	public static final String FULL_NOTANOMALIES_PATH = "E:\\IPFS\\AD_DATA\\full_notanomalies";
	public static final double THRESHOLD = .1;

	public static void main(String[] args) {
	    try {
		    File modelFile = new File(MODEL_PATH);
	    	MultiLayerNetwork model = MultiLayerNetwork.load(modelFile, true);
	    
	        INDArray modelParams = model.params();
	    	List<Double> iplsParams = Doubles.asList(modelParams.getRow(0).toDoubleVector());
	    	System.out.println(iplsParams.size());
	    		    	
	    	FileOutputStream fos = new FileOutputStream("E:\\IPFS\\AD_DATA\\ad_ipls_params");
	        ObjectOutputStream oos = new ObjectOutputStream(fos);
	        oos.writeObject(iplsParams);
	    	//oos.writeObject((INDArray) model.params());
	    	
	        FileInputStream fin = new FileInputStream("E:\\IPFS\\AD_DATA\\ad_ipls_params");
	        ObjectInputStream oin = new ObjectInputStream(fin);
	        List<Double> Lmodel = (List<Double>) oin.readObject();
	    	
	    	System.out.println("readin "+Lmodel.size());

	    	// Load in IPLS params file
	        //FileInputStream fin = new FileInputStream(fileName);
	        //ObjectInputStream oin = new ObjectInputStream(fin);
	        //List<Double> Lmodel = (List<Double>)oin.readObject();
	    	//INDArray modelParamsCopy = Nd4j.zeros(1, model.params().length());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
