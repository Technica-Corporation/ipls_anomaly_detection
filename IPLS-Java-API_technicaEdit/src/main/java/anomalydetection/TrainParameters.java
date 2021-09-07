package anomalydetection;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;

public class TrainParameters {

	private MultiLayerConfiguration modelConf;
	private int epochs;
	private int updateFrequency;
	private int stabilityCount;
	private double learningRate;
	
	public TrainParameters(MultiLayerConfiguration modelConf, int epochs, int updateFrequency, double learningRate, int stabilityCount){
		this.updateFrequency = updateFrequency;
		this.modelConf = modelConf;
		this.epochs = epochs;
		this.learningRate = learningRate;
		this.stabilityCount = stabilityCount;
	}

	public MultiLayerConfiguration getModelConf() {
		return modelConf;
	}

	public void setModelConf(MultiLayerConfiguration modelConf) {
		this.modelConf = modelConf;
	}

	public int getUpdateFrequency() {
		return updateFrequency;
	}
	
	public int getStabilityCount() {
		return stabilityCount;
	}
	
	public int getEpochs() {
		return epochs;
	}

	public void setEpochs(int epochs) {
		this.epochs = epochs;
	}

	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}
	
	
	
}
