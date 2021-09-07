package anomalydetection.train;


import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.AdaDelta;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class AnomalyDetectionModel {
	public static int rngSeed = 123;
	public static MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
			.seed(rngSeed)
			.weightInit(WeightInit.XAVIER)
			//.updater(new AdaDelta())
			.updater(new Adam(0.001))
			.activation(Activation.RELU)
			.list()
			.layer(new DenseLayer.Builder().nIn(4).nOut(64).build())
			.layer(new DenseLayer.Builder().nIn(64).nOut(32).build())
			.layer(new DenseLayer.Builder().nIn(32).nOut(16).build())
			.layer(new DenseLayer.Builder().nIn(16).nOut(32).build())
			.layer(new DenseLayer.Builder().nIn(32).nOut(64).build())
			.layer(new OutputLayer.Builder().nIn(64).nOut(4).lossFunction(LossFunctions.LossFunction.XENT).activation(Activation.SIGMOID).build())
			.build();
}
