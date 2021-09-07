package anomalydetection.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.shade.guava.primitives.Doubles;
import anomalydetection.TrainParameters;
import originalDefault.IPLS;

public class AdTrainHandler implements Runnable {
	private IPLS ipls;
	private MultiLayerNetwork model;
	private TrainParameters trainParams;
	private BlockingQueue<DataSet> trainingDataQueue;
	private BlockingQueue<INDArray> modelUpdateQueue;
	private INDArray initialParams;
    private List<DataSet> cache;
	private int counter;
	private int updateFrequency;
	private int stabilityCounter;
	
	public AdTrainHandler(TrainParameters trainParams, String modelPath, String ipfsAddress, List<String> Bootstrappers,
			boolean iplsIsBootstrapper, int iplsPort, BlockingQueue<DataSet> trainingDataQueue,
			BlockingQueue<INDArray> modelUpdateQueue) {
		this.trainParams = trainParams;
		this.trainingDataQueue = trainingDataQueue;
		this.modelUpdateQueue = modelUpdateQueue;

		cache = new ArrayList<DataSet>();
		counter = 0;
		stabilityCounter = 0;
		//updateFrequency = trainParams.getUpdateFrequency();
		updateFrequency = 1;
		
		// 	Init IPLS
		try {
			ipls = new IPLS();
			ipls.init(ipfsAddress, modelPath, Bootstrappers, iplsIsBootstrapper, iplsPort);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model = new MultiLayerNetwork(trainParams.getModelConf());
		model.init();
	}

	/**
	 * 
	 */
	public void run() {
		DataSet data = null;
		boolean hasNan = false;
		
		try {
			while (!Thread.currentThread().isInterrupted()) {
				data = null;
											
				// Poll for data
				if (!trainingDataQueue.isEmpty()) {					
					if(counter == 0) {
						initialParams = getIPLSParams();
						model.setParams(initialParams.dup());
					}
					
					// Get Data
					data = trainingDataQueue.remove();
					cache.add(data);

					// Update Peer partitions and add to inference model queue when data input counter reaches updateFrequency
					if(counter == updateFrequency) {
						DataSetIterator dIter = new ListDataSetIterator(cache);
						model.fit(dIter, trainParams.getEpochs());	
						updatePeerPartitions(initialParams);						
						
						if(stabilityCounter < trainParams.getStabilityCount()) {
							stabilityCounter++;
						} else {
							updateFrequency = trainParams.getUpdateFrequency();
							modelUpdateQueue.add(model.params());	
						}
						cache.clear();
						counter = 0;
					} else {
						counter++;
					}
				} else {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		} finally {
			System.out.println("AdTrainHandler - FINALLY");
		}
	}
	
	/**
	 * 
	 * @param initialParams
	 * @param currentParams
	 * @return
	 */
	private INDArray getDiff(INDArray initialParams, INDArray currentParams) {
		return currentParams.sub(initialParams);
	}
	
	/**
	 * 
	 * @return
	 */
	private INDArray getIPLSParams() {
		List<Double> iplsParams = new ArrayList<>();
		INDArray dl4jParams = Nd4j.zeros(1, model.params().length());

		try {
			iplsParams = ipls.GetPartitions();
			for (int j = 0; j < iplsParams.size(); j++) {
				dl4jParams.put(0, j, iplsParams.get(j));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dl4jParams;
	}
	
	/**
	 * 
	 * @param initialParams
	 */
	private void updatePeerPartitions(INDArray initialParams) {
		INDArray dl4jParams, gradient;
		dl4jParams = model.params();
        List<Double> paramsList;
		gradient = getDiff(model.params(), initialParams);
		gradient = gradient.mul(1);
		try {
			paramsList = Doubles.asList(gradient.getRow(0).toDoubleVector());
			ipls.UpdateGradient(Doubles.asList(gradient.getRow(0).toDoubleVector()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
