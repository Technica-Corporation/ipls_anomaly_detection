package anomalydetection.handler;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.serializer.NormalizerSerializer;

import anomalydetection.handler.mqtt.AdMqttClient;
import anomalydetection.handler.mqtt.AdMqttListener;

public class AdMqttHandler implements Runnable {

	private String brokerUrl;
	private String clientId;
	private boolean cleanSession;
	private boolean quietMode;
	private DataNormalization normalizer;
	private BlockingQueue<DataSet> inferenceInputQueue;
	private BlockingQueue<String> inferenceOutputQueue;
	private BlockingQueue<DataSet> trainingInputQueue;
	private List<String> subTopics;
	private String pubTopic;

	public AdMqttHandler(String brokerUrl, String clientId, boolean cleanSession, boolean quietMode,
			List<String> subTopics, String pubTopic, String normalizerPath, BlockingQueue<DataSet> inferenceInputQueue, BlockingQueue<String> inferenceOutputQueue,
			BlockingQueue<DataSet> trainingDataQueue) {
		this.brokerUrl = brokerUrl;
		this.clientId = clientId;
		this.cleanSession = cleanSession;
		this.quietMode = quietMode;
		this.inferenceInputQueue = inferenceInputQueue;
		this.inferenceOutputQueue = inferenceOutputQueue;
		this.trainingInputQueue = trainingDataQueue;
		this.subTopics = subTopics;
		this.pubTopic = pubTopic;

		try {
			NormalizerSerializer s = NormalizerSerializer.getDefault();
			normalizer = s.restore(normalizerPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		AdMqttClient client = null;
		String payload;
		
        try {
    		client = new AdMqttListener(brokerUrl, clientId, cleanSession, quietMode, normalizer,
    				inferenceInputQueue, trainingInputQueue);
    		client.connect(subTopics, 0);
    		
			while (!Thread.currentThread().isInterrupted()) {
				if (client.badConnection) {
					System.out.println(" !!!!!!!!!! BAD CONNECTION !!!!!!!!!!!!!");
					client.close();
					client = new AdMqttListener(brokerUrl, clientId, cleanSession, quietMode, normalizer,
							inferenceInputQueue, trainingInputQueue);
					client.connect(subTopics, 0);
				}
								
				// Poll for error Scores from the Inference Handler Thread; Publish it if there was one
				
				if(!inferenceOutputQueue.isEmpty()) {
					payload = inferenceOutputQueue.remove();
					client.publish(pubTopic, payload);
				}		
			}
        } catch (MqttSecurityException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		} finally {
            System.out.println("AdMqttHandler - FINALLY");
			if(!Objects.isNull(client)) {
				try {
					client.close();
				} catch (MqttException e) {
					e.printStackTrace();
				}
			}
        }
	}
}
