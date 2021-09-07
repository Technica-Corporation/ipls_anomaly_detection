import paho.mqtt.client as mqtt
import itertools
import logging
import sys 
import csv
import time
import json
from configparser import ConfigParser
from random import shuffle 

def on_connect(client, userdata, flags, rc):
	logging.info("(!) Connected with result code "+str(rc))

def on_message(client, userdata, msg):
	global runmode, modeChanged
	print("MQTT:" + msg.payload.decode("utf-8"))
	logging.info("Got a message from MQTT: " + msg.payload.decode("utf-8"))
	data = json.loads(msg.payload.decode("utf-8"))
	newmode = data["mode"]
	if newmode != runmode:
		logging.info("Changing run mode to: " + newmode)
		modeChanged = True
		runmode = newmode
	else:
		logging.info("Current mode same as new mode, not changing")

def setup_client(port, host, cert=None):
	client = mqtt.Client(client_id="", clean_session=True, userdata=None, protocol=mqtt.MQTTv311)
	client.on_connect = on_connect
	client.on_message = on_message
	if cert is not None:
		client.tls_set(cert, tls_version=ssl.PROTOCOL_TLSv1_2)
	client.connect(host, port, 60)
	return client

def main():
    print("Start")
    parser = ConfigParser()
    parser.read("/workspace/generators.conf")

    client = setup_client(parser.getint('mqtt','port'), parser.get('mqtt','host'))

    publish_interval = parser.getfloat('service', 'publish_interval')
    pub_topic = parser.get('mqtt','pub_topic')
    runtime = parser.getint('service', 'total_runtime')
    anomaly_file = parser.get('service', 'anomaly_file')
    normal_file = parser.get('service', 'normal_file')

    type="None"

    if sys.argv[1] == "normal":
        print("--Normal Data--")
        type="normal"
        input_file = normal_file
    elif sys.argv[1] == "anomaly":
        print("--Anomaly Data--")
        type="anomaly"
        input_file = anomaly_file
    else:
        print("--Invalid Input--")
        print("    Add 'normal' or 'anomaly' as a parameter to the mython script ")
        print("    ex: python generate_data.py normal ")
        print("-----------")
        sys.exit()

    with open(input_file, 'r') as f:
        reader = csv.reader(f)
        data = list(reader)

    shuffle(data)
    map(shuffle, data)
    licycle = itertools.cycle(data)

    dict={}
    t_end = time.time() + runtime
    if runtime > 0:
        while time.time() <= t_end:            
            dict['value']=next(licycle)
            message = json.dumps(dict)
            print(type, ":", message)
            client.publish(pub_topic, message)
            time.sleep(publish_interval)
    else:
        while True:
            dict['value']=next(licycle)
            message = json.dumps(dict)
            print(type, ":", message)
            client.publish(pub_topic, message)
            time.sleep(publish_interval)

    print("End")

if __name__ == "__main__":
    # execute only if run as a script
    main()