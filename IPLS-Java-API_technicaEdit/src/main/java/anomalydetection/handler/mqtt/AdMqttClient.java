package anomalydetection.handler.mqtt;


import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

public abstract class AdMqttClient implements MqttCallback {
	private MqttClient client;
	private String brokerUrl;
	private boolean quietMode;
	private MqttConnectOptions conOpt;
	private String password;
	private String userName;
	private String clientId;
	public boolean badConnection;
    
	public AdMqttClient(String brokerUrl, String clientId, boolean cleanSession, boolean quietMode) {
		this.clientId = clientId;
		this.brokerUrl = brokerUrl;
		this.quietMode = quietMode;
		this.password = null;
		this.userName = null;
        this.badConnection = false;
		
		try {
			conOpt = new MqttConnectOptions();
			//conOpt.setCleanSession(clean);
			conOpt.setCleanSession(cleanSession);
			conOpt.setAutomaticReconnect(true);
			conOpt.setConnectionTimeout(0);
            conOpt.setKeepAliveInterval(3);
            conOpt.setMaxReconnectDelay(3);
			conOpt.setMaxInflight(40);
			
            if (password != null) {
				conOpt.setPassword(this.password.toCharArray());
			}
			if (userName != null) {
				conOpt.setUserName(this.userName);
			}
			
			client = new MqttClient(this.brokerUrl, this.clientId, null);
			client.setCallback(this);

		} catch (MqttException e) {
			e.printStackTrace();
			log("Unable to set up client: " + e.toString());
			System.exit(1);
		}
	}
	
	protected void log(String message) {
		if (!quietMode) {
			System.out.println(message);
		}
	}
		
	public void connect(List<String> subTopics, int qos) throws MqttSecurityException, MqttException {
		client.connect(conOpt);
		
        for(String subTopic : subTopics) {
    		log("Connected to " + brokerUrl + " with client ID " + client.getClientId());
    		log("Subscribing to topic \"" + subTopic + "\" qos " + 1);
    		client.subscribe(subTopic, 0);        
        }
	}
	
	public void disconnect() throws MqttException {
		client.disconnect();
		log("Disconnected");
	
	}
	
	public void close() throws MqttException {
		client.close();
	}
	
	public void publish(String pubTopic, String payload) {
		MqttMessage message = new MqttMessage(payload.getBytes());
		try {
			//log("&&&&&");
			//log("&&&&& -> TRYING TO PUBLISH to: "+pubTopic);
			client.publish(pubTopic, message);
			//log("&&&&& -> PUBLISHED");
			//log("&&&&&");
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void forceReconnect(String subTopic) {
		System.out.println("### --> FORCE RECONNECT <-- ###");
	    try {
	    	log("Enforcing a reconnect...");
	    	client.connect(conOpt);
			client.subscribe(subTopic);
			client.subscribe("exit");	 
	    } catch (MqttException mqttException) {
	    	System.out.println(String.format("Failed to force reconnect: %s", mqttException));
	    	mqttException.printStackTrace();
	    }
	}
}
