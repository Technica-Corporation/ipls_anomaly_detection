var config = {};

/*********************************** Server and MQTT ***********************************/
config.port = 5601;
config.mqttBrokerHost = '0.0.0.0';
config.mqttBrokerPort = 1883;
config.defaultQoS = 1;
config.mqttSubscriptions = {
/*
  'v1/sensor1': config.defaultQoS,
  'v1/sensor2': config.defaultQoS,
  'v1/sensor3': config.defaultQoS,
  'v1/sensor4': config.defaultQoS,
  'v1/sensor5': config.defaultQoS,
  'v1/sensor6': config.defaultQoS,
  'v1/sensor7': config.defaultQoS,
  'v1/sensor8': config.defaultQoS,
  'v1/sensor9': config.defaultQoS,
  'v2/sensor1': config.defaultQoS,
  'v2/sensor2': config.defaultQoS,
  'v2/sensor3': config.defaultQoS,
  'v2/sensor4': config.defaultQoS,
  'v2/sensor5': config.defaultQoS,
  'v2/sensor6': config.defaultQoS,
  'v2/sensor7': config.defaultQoS,
  'v2/sensor8': config.defaultQoS,
  'v2/sensor9': config.defaultQoS,
  'v3/sensor1': config.defaultQoS,
  'v3/sensor2': config.defaultQoS,
  'v3/sensor3': config.defaultQoS,
  'v3/sensor4': config.defaultQoS,
  'v3/sensor5': config.defaultQoS,
  'v3/sensor6': config.defaultQoS,
  'v3/sensor7': config.defaultQoS,
  'v3/sensor8': config.defaultQoS,
  'v3/sensor9': config.defaultQoS,
  'v4/sensor1': config.defaultQoS,
  'v4/sensor2': config.defaultQoS,
  'v4/sensor3': config.defaultQoS,
  'v4/sensor4': config.defaultQoS,
  'v4/sensor5': config.defaultQoS,
  'v4/sensor6': config.defaultQoS,
  'v4/sensor7': config.defaultQoS,
  'v4/sensor8': config.defaultQoS,
  'v4/sensor9': config.defaultQoS,
*/
  'v1/anomaly': config.defaultQoS,
  'v2/anomaly': config.defaultQoS,
  'v3/anomaly': config.defaultQoS,
  'v4/anomaly': config.defaultQoS,
  'vehicle/1': config.defaultQoS,
  'vehicle/2': config.defaultQoS,
  'vehicle/3': config.defaultQoS,
  'vehicle/4': config.defaultQoS,
  'global/updates': config.defaultQoS,
  'model/live': config.defaultQoS
};
config.avgCalculationBuffer = 50;
/**************************************************************************************/

/********************************** Graph Parameters **********************************/
var graphConfig = {};
graphConfig.graphBufferSize = 50;

graphConfig.sensor1= {
  range: [-5, 5],
  ticks:{right: 5, left: 5},
  axes: ['bottom','left']
};

graphConfig.sensor2= {
  range: [-8, 8],
  ticks:{right: 5, left: 5},
  axes: ['bottom','left']
};

graphConfig.sensor3= {
  range: [-7, 7],
  ticks:{right: 5, left: 5},
  axes: ['bottom','left']
};

graphConfig.sensor4= {
  range: [-8, 8],
  ticks:{right: 5, left: 5},
  axes: ['bottom','left']
};

/*
graphConfig.sensor5= {
  range: [-10, 60],
  ticks:{right: 5, left: 5},
  axes: ['bottom','left']
};

graphConfig.sensor6= {
  range: [-40, 40],
  ticks:{right: 5, left: 5},
  axes: ['bottom','left']
};

graphConfig.sensor7= {
  range: [0, 90],
  ticks:{right: 5, left: 5},
  axes: ['bottom','left']
};

graphConfig.sensor8= {
  range: [0, 90],
  ticks:{right: 5, left: 5},
  axes: ['bottom','left']
};

graphConfig.sensor9= {
  range: [-10, 70],
  ticks:{right: 5, left: 5},
  axes: ['bottom','left']
};
*/

config.graphConfig = graphConfig;
/**************************************************************************************/

module.exports = config;
