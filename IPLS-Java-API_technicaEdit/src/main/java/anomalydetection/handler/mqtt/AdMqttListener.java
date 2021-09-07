package anomalydetection.handler.mqtt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.serializer.NormalizerSerializer;
import org.nd4j.linalg.factory.Nd4j;

public class AdMqttListener extends AdMqttClient {

	private DataNormalization normalizer;
	private BlockingQueue<DataSet> inferenceDataQueue;
	private BlockingQueue<DataSet> trainingDataQueue;

	public AdMqttListener(String brokerUrl, String clientId, boolean cleanSession, boolean quietMode,
			DataNormalization normalizer, BlockingQueue<DataSet> inferenceDataQueue,
			BlockingQueue<DataSet> trainingDataQueue) {
		super(brokerUrl, clientId, cleanSession, quietMode);
		this.inferenceDataQueue = inferenceDataQueue;
		this.trainingDataQueue = trainingDataQueue;
        this.normalizer = normalizer;
	}

	/**
	 * 
	 * @param topic
	 * @return message
	 */
	@Override
	public void messageArrived(String topic, MqttMessage message) throws MqttException {
		//log("#####");
		//log("##### -> MESSAGE ARRIVED");

		// Convert JSON string to list of strings
		// ex: [ 0.0, 0.0, 0.0, 0.0 ]
		JSONObject json = new JSONObject(new String(message.getPayload()));
		JSONArray jsonArray = json.getJSONArray("value");
		List<String> record = new ArrayList<String>();
		for (int i = 0; i < jsonArray.length(); i++) {
			record.add(jsonArray.getString(i));
		}
		
		// Convert List<String> to dl4j DataSet
		DataSet data = processData(record);

		// Add the data to the queues for inference and training
		inferenceDataQueue.add(data);
		trainingDataQueue.add(data);
		//log("#####     -> Added to QUEUES"+jsonArray.toString());
		
		//log("##### -> MESSAGE PROCESSED");
		//log("#####");
	}

	/**
	 * 
	 * @param record
	 * @return
	 */
	private DataSet processData(List<String> record) {
		INDArray input = Nd4j.zeros(1, record.size());
		double[] array = new double[record.size()];

		// Convert list of Strings to INDArray of Doubles
		for (int j = 0; j < record.size(); j++)
			array[j] = Double.valueOf(record.get(j));
		input.putRow(0, Nd4j.create(array));

		// Use the normalizer to transform the input
		normalizer.transform(input);

		// Create a DataSet object, with the normalized data as the 'Data' and the
		// 'Label'
		DataSet dataset = new DataSet(input, input);

		return dataset;
	}
	
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
	}

	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("### --> CONNECTION LOST <-- ###");
		this.badConnection = true;
	}
}
