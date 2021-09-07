package anomalydetection.train;


import java.io.File;
import java.io.IOException;
import java.util.List;

import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerMinMaxScaler;
import org.nd4j.linalg.dataset.api.preprocessor.serializer.NormalizerSerializer;

/**
 * Test an Anomaly Detection Model like Smartfog's "Federated Learning Anomaly Detection" in the DeepLearning4J framework
 * 
 * @author mbernardo
 *
 */
public class AdTestModel {
	public static final String MODEL_PATH = "E:\\\\IPFS\\\\AD_DATA\\\\ad_model";
	public static final String SCALER_PATH = "E:\\IPFS\\AD_DATA\\scaler";

	public static final String TRAIN_DATA_PATH = "E:\\IPFS\\AD_DATA\\training";
	public static final String FULL_ANOMALIES_PATH = "E:\\IPFS\\AD_DATA\\full_anomalies";
	public static final String FULL_NOTANOMALIES_PATH = "E:\\IPFS\\AD_DATA\\full_notanomalies";
	public static final double THRESHOLD = .1;

	/**
	 * Test the model to make sure it works on a given dataset just prints out how
	 * many anomalies there were
	 * 
	 * @param dataset
	 * @param model
	 */
	public static int[] testModel(DataSet dataset, MultiLayerNetwork model, int trueLabel) {

		// 0 is normal, 1 is anomaly

		int anomaly = 0;
		int total = 0;
		int tp = 0;
		int fp = 0;
		int fn = 0;
		
		int predLabel = 0;

		double score = 0;
		
		for (int i = 0; i < dataset.getFeatures().size(0); i++) {
			INDArray input = dataset.getFeatures().getRow(i);

			INDArray expected = input.reshape(1, 4);
			INDArray predicted = model.output(expected);
            score = model.score(new DataSet(expected, expected));
		
			
			INDArray diff = predicted.sub(expected);
			double error = diff.norm2(1).getDouble();

			if (error > THRESHOLD) {
				predLabel = 1;
				anomaly++;
			} else {
				predLabel = 0;
			}

			if (trueLabel == 1) {
			    if(predLabel == 1) {
					tp++;			    	
			    }
			    if(predLabel == 0) {
					fn++;			    	
			    }  
			    
			} else if (trueLabel == 0) {
			    if(predLabel == 0) {
					tp++;			    	
			    }
			    if(predLabel == 1) {
					fp++;			    	
			    }  
			}

			total++;
		}
		
		int[] values = {tp, fp, fn};

		//System.out.println("    True Positive: " + tp);
		//System.out.println("    False Negative: " + fn);
		//System.out.println("    False Positive: " + fp);
		System.out.println("    Loss: " + score);
		System.out.println("    (Predicted Anomaly / Total Records): " + anomaly + " / " + total);
		
		
		return values;
	}

	/**
	 * Predict if an input is an anomly, and fit on it if it was
	 * 
	 * @param dataset
	 * @param model
	 */
	public static boolean predictAnomaly(INDArray input, MultiLayerNetwork model) {
		INDArray expected = input.reshape(1, 4);
		INDArray predicted = model.output(expected);

		INDArray diff = predicted.sub(expected);
		double error = diff.norm2(1).getDouble();
		boolean anomaly = false;

		if (error > THRESHOLD) {
			anomaly = true;
			model.fit(new DataSet(expected, expected));
		}

		return anomaly;
	}

	public static void main(String[] args) {

		// Load Model and Scaler
		try {
			File modelFile = new File(MODEL_PATH);
			MultiLayerNetwork model = MultiLayerNetwork.load(modelFile, true);

			NormalizerSerializer s = NormalizerSerializer.getDefault();
			DataNormalization normalizer = s.restore(SCALER_PATH);

			List<List<String>> trainList = AdUtils.getRecords(TRAIN_DATA_PATH);
			DataSet trainData = AdUtils.getDataset(trainList);
			DataSet trainData_t = trainData.copy();

			List<List<String>> anomalyList = AdUtils.getRecords(FULL_ANOMALIES_PATH);
			DataSet anomData = AdUtils.getDataset(anomalyList);
			DataSet anomData_t = anomData.copy();

			List<List<String>> normalList = AdUtils.getRecords(FULL_NOTANOMALIES_PATH);
			DataSet normData = AdUtils.getDataset(normalList);
			DataSet normData_t = normData.copy();

			normalizer.transform(trainData_t);
			normalizer.transform(anomData_t);
			normalizer.transform(normData_t);

			/*
			 * // Check to make sure the data was actually transformed INDArray original =
			 * normData.getFeatures().getRow(0); INDArray transform =
			 * normData_t.getFeatures().getRow(0);
			 * System.out.println("Original :"+original);
			 * System.out.println("Transform:"+transform);
			 */

			System.out.println("Error Threshold: " + THRESHOLD);
			System.out.println();

			int tp = 0;
			int fp = 0;
			int fn = 0;
			int total_tp = 0;
			int total_fp = 0;
			int total_fn = 0;
					
			double precision;
			double recall; 
			double f1;
			double total_precision;
			double total_recall; 
			double total_f1;
			System.out.println("----------");
			System.out.println("    TRAIN DATA");
			int[] values = testModel(trainData_t, model, 0);

			tp = values[0];
			fp = values[1];
			fn = values[2];
			total_tp += tp;
			total_fp += fp;
			total_fn += fn;
			
			precision = (double) tp / (tp+fp);
			recall = (double) tp / (tp+fn);
			f1 = (double) 2*((precision*recall) / (precision+recall));
			//System.out.println("    Precision: "+precision);
			//System.out.println("    Recall: "+recall);
			System.out.println("    F1: "+f1);
		
			System.out.println("    ----------");
			System.out.println("    ANOMALY DATA");
			values = testModel(anomData_t, model, 1);
			tp = values[0];
			fp = values[1];
			fn = values[2];
			total_tp += tp;
			total_fp += fp;
			total_fn += fn;
			
			precision = (double) tp / (tp+fp);
			recall = (double) tp / (tp+fn);
			f1 = (double) 2*((precision*recall) / (precision+recall));
			
			//System.out.println("    Precision: "+precision);
			//System.out.println("    Recall: "+recall);			
			System.out.println("    F1: "+f1);
			
			System.out.println("    ----------");
			System.out.println("    NORMAL DATA");
			values = testModel(normData_t, model, 0);
			tp = values[0];
			fp = values[1];
			fn = values[2];
			total_tp += tp;
			total_fp += fp;
			total_fn += fn;
			
			precision = (double) tp / (tp+fp);
			recall = (double) tp / (tp+fn);
			f1 = (double) 2*((precision*recall) / (precision+recall));
			//System.out.println("    Precision: "+precision);
			//System.out.println("    Recall: "+recall);
			System.out.println("    F1: "+f1);
			
		
			System.out.println("--------");
			total_precision = (double) total_tp / (total_tp+total_fp);
			total_recall = (double) total_tp / (total_tp+total_fn);
			total_f1 = (double) 2*((total_precision*total_recall) / (total_precision+total_recall));
			//System.out.println("Precision: "+total_precision);
			//System.out.println("Recall: "+total_recall);
			System.out.println("    All DataSets F1: "+total_f1);
			System.out.println("----------");
			/*
			int newCount = 0;
			int newTotal = 0;
			boolean anoms = true;
			DataSet dataset = anomData_t.copy();
			dataset.shuffle(123);

			while (anoms) {

				boolean hadAnoms = false;

				for (int i = 0; i < dataset.getFeatures().size(0); i++) {
					INDArray input = dataset.getFeatures().getRow(i);

					boolean anomaly = predictAnomaly(input, model);

					if (anomaly) {
						hadAnoms = true;
						newCount++;
					}
					newTotal++;
				}

				if (!hadAnoms) {
					anoms = false;
				}

			}

			System.out.println("anomalies: " + newCount);
			System.out.println("total: " + newTotal);
			*/

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
