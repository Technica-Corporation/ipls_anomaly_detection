package anomalydetection.handler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.json.JSONObject;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;

public class AdInferenceHandler implements Runnable {
	
	private MultiLayerNetwork model;
	private BlockingQueue<DataSet> inferenceInputQueue;
	private BlockingQueue<String> inferenceOutputQueue;
	private BlockingQueue<INDArray> modelUpdateQueue;
	private MultiLayerConfiguration modelConf;
	
	public AdInferenceHandler(MultiLayerConfiguration modelConf, String modelPath, BlockingQueue<DataSet> inferenceInputQueue, BlockingQueue<String> inferenceOutputQueue, BlockingQueue<INDArray> modelUpdateQueue) {

		this.modelConf = modelConf;
		this.inferenceInputQueue = inferenceInputQueue;
		this.inferenceOutputQueue = inferenceOutputQueue;
		this.modelUpdateQueue = modelUpdateQueue;
		
        model = new MultiLayerNetwork(this.modelConf);
        model.init();
        
		List<Double> Lmodel;
		try (ObjectInputStream oin = new ObjectInputStream(new FileInputStream(modelPath))) {
			Lmodel = (List<Double>)oin.readObject();
			updateModel(Lmodel);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		DataSet data = null;
		String payload = null;
		INDArray modelParams = null;
		
        try {
			while (!Thread.currentThread().isInterrupted()) {
				// Poll for model changes; update the model if there are
				if(!modelUpdateQueue.isEmpty()) {
					modelParams = modelUpdateQueue.remove();
					//updateModel(iplsModelParams);
					model.setParams(modelParams);
				}
				
				// Poll for data on input queue, run inference and put it on the output queue
				if(!inferenceInputQueue.isEmpty()) {
					data = inferenceInputQueue.remove();
					//System.out.println("    $$$$ GOT DATA FROM QUEUE $$$$");
					payload = runInference(data);
					
					if(!Objects.isNull(payload)) {
						inferenceOutputQueue.add(payload);
					} else {
						System.out.println("$$$$ OH NO!!! -- INFERENCE EMPTY $$$$");
						System.exit(1);
					}	
				}
				
				if(Objects.isNull(data)) {
					Thread.sleep(100);
				} 
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
            System.out.println("AdInferenceHandler - FINALLY");
        }
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	private String runInference(DataSet data) {
		INDArray input, expected, predicted, diff;
		double error;
		JSONObject json;
		String payload = null;
		try {
			for (int i = 0; i < data.getFeatures().size(0); i++) {

				input = data.getFeatures().getRow(i);
				expected = input.reshape(1, 4);
				predicted = model.output(expected);

				diff = predicted.sub(expected);
				error = diff.norm2(1).getDouble();
				json = new JSONObject();

				List<Double> list = new ArrayList<Double>();
				list.add(error);
				json.put("anomaly_score", list);
				long unixTime = System.currentTimeMillis() / 1000L;
				json.put("timestamp", unixTime);

				payload = json.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return payload;
	}
	
	/**
	 * 
	 * @param iplsParams
	 */
	private void updateModel(List<Double> iplsParams) {
		INDArray dl4jParams;

		dl4jParams = Nd4j.zeros(1, model.params().length());
		for (int i = 0; i < iplsParams.size(); i++) {
			dl4jParams.put(0, i, iplsParams.get(i));
		}
		model.setParams(dl4jParams);
	}
}
