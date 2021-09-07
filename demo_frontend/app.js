var http = require('http');
var fs = require('fs');
var config = require('./config');
var mqtt = require('mqtt');

// NEVER use a Sync function except at start-up!
var main_css = fs.readFileSync(__dirname + '/css/main.css');
var bootstrap_min_css = fs.readFileSync(__dirname + '/css/bootstrap.min.css');
var epoch_min_css = fs.readFileSync(__dirname + '/css/epoch.min.css');
var index_html = fs.readFileSync(__dirname + '/html/index.html');
var sensor_ctrl_html = fs.readFileSync(__dirname + '/html/sensor_ctrl.html');
var index_js = fs.readFileSync(__dirname + '/js/index.js');
var sensor_ctrl_js = fs.readFileSync(__dirname + '/js/sensor_ctrl.js');
var bootstrap_min_js = fs.readFileSync(__dirname + '/js/bootstrap.min.js');
var d3_v3_min_js = fs.readFileSync(__dirname + '/js/d3.v3.min.js');
var epoch_min_js = fs.readFileSync(__dirname + '/js/epoch.min.js');
var jquery_min_js = fs.readFileSync(__dirname + '/js/jquery.min.js');
var popper_min_js = fs.readFileSync(__dirname + '/js/popper.min.js');
var humvee_png = fs.readFileSync(__dirname + '/images/humvee.png');

var app = http.createServer(function (req, res) {
  switch (req.url) {
    case '/css/main.css':
      res.writeHead(200, {
        'Content-Type': 'text/css'
      });
      res.write(main_css);
      break;
    case '/css/bootstrap.min.css':
      res.writeHead(200, {
        'Content-Type': 'text/css'
      });
      res.write(bootstrap_min_css);
      break;
    case '/css/epoch.min.css':
      res.writeHead(200, {
        'Content-Type': 'text/css'
      });
      res.write(epoch_min_css);
      break;
    case '/js/index.js':
      res.writeHead(200, {
        'Content-Type': 'application/javascript'
      });
      res.write(index_js);
      break;
    case '/js/sensor_ctrl.js':
      res.writeHead(200, {
        'Content-Type': 'application/javascript'
      });
      res.write(sensor_ctrl_js);
      break;

    case '/js/bootstrap.min.js':
      res.writeHead(200, {
        'Content-Type': 'application/javascript'
      });
      res.write(bootstrap_min_js);
      break;
    case '/js/d3.v3.min.js':
      res.writeHead(200, {
        'Content-Type': 'application/javascript'
      });
      res.write(d3_v3_min_js);
      break;
    case '/js/epoch.min.js':
      res.writeHead(200, {
        'Content-Type': 'application/javascript'
      });
      res.write(epoch_min_js);
      break;
    case '/js/jquery.min.js':
      res.writeHead(200, {
        'Content-Type': 'application/javascript'
      });
      res.write(jquery_min_js);
      break;
    case '/js/popper.min.js':
      res.writeHead(200, {
        'Content-Type': 'application/javascript'
      });
      res.write(popper_min_js);
      break;
    case '/sensor_ctrl':
      res.writeHead(200, {
        'Content-Type': 'text/html'
      });
      res.write(sensor_ctrl_html);
      break;
    case '/images/humvee.png':
      res.writeHead(200, {
        'Content-Type': 'image'
      });
      res.write(humvee_png);
      break;
    default:
      res.writeHead(200, {
        'Content-Type': 'text/html'
      });
      res.write(index_html);
  }
  res.end();
});

// Socket.io server listens to our app
var io = require('socket.io').listen(app);

var options = {
  'host': config.mqttBrokerHost,
  'port': config.mqttBrokerPort
  //'ca': fs.readFileSync('ca-chain.cert.pem'),
  //'rejectUnauthorized': true,
  //'protocol': 'ssl'
};

var client = mqtt.connect(options);

var sensorTypes = ['sensor1', 'sensor2', 'sensor3', 'sensor4', 'sensor5', 'sensor6', 'sensor7', 'sensor8', 'sensor9'];

var sensorVals = [
  [
    [],
    [],
    [],
    [],
    [],
    [],
    [],
    [],
    []
  ],
  [
    [],
    [],
    [],
    [],
    [],
    [],
    [],
    [],
    []
  ],
  [
    [],
    [],
    [],
    [],
    [],
    [],
    [],
    [],
    []
  ],
  [
    [],
    [],
    [],
    [],
    [],
    [],
    [],
    [],
    []
  ]
];

function pushVal(vid, sid, val) {
  if (sensorVals[vid][sid].length >= config.avgCalculationBuffer) {
    sensorVals[vid][sid].shift();
    sensorVals[vid][sid].push(val);
  } else {
    sensorVals[vid][sid].push(val);
  }
}

function calcAvg(vid, sid) {
  let count = 0;
  let total = 0;
  sensorVals[vid][sid].forEach(function (val) {
    total += val;
    count++;
  });
  return { // TODO remove count and total from object
    'vehicleId': vid,
    'sensorId': sid,
    'count': count,
    'total': total,
    'value': (total / count)
  };
}

function getSensorId(s) {
  let sid = null;
  switch(s) {
    case 'sensor1':
      sid = 0;
      break;
    case 'sensor2':
      sid = 1;
      break;
    case 'sensor3':
      sid = 2;
      break;
    case 'sensor4':
      sid = 3;
      break;
    case 'sensor5':
      sid = 4;
      break;
    case 'sensor6':
      sid = 5;
      break;
    case 'sensor7':
      sid = 6;
      break;
    case 'sensor8':
      sid = 7;
      break;
    case 'sensor9':
      sid = 8;
      break;
    case 'anomaly':
      sid = 100;
      break;
  }
  return sid;
}

client.on('connect', function ()  {
  console.log(`Connection to MQTT broker <${config.mqttBrokerHost}:${config.mqttBrokerPort}> established...`);
  client.subscribe(config.mqttSubscriptions);
});

client.on('message',  function  (topic,  message)  {
  let processMessage = true; //mesage should be processed the original way
  let splitTopic = topic.split('/');
  let vehicleId = null;
  let sensorId = null;

  //see if this is a fed learning topic
  if(topic == 'model/live' || topic == 'global/updates') { 
    m = {};
    m.topic = topic;
    io.emit('fedlearning', m);
    return;
  }

  switch (splitTopic[0]) { //will create the sensorId with format: '<vehicleNumber>/<sensorNumber>'
    case 'v1':
      vehicleId = 0;
      sensorId = getSensorId(splitTopic[1]);
      break;
    case 'v2':
      vehicleId = 1;
      sensorId = getSensorId(splitTopic[1]);
      break;
    case 'v3':
      vehicleId = 2;
      sensorId = getSensorId(splitTopic[1]);
      break;
    case 'v4':
      vehicleId = 3;
      sensorId = getSensorId(splitTopic[1]);
      break;
    case 'vehicle':
      processMessage = false; //this message will be processed the new way
      vehicleId = splitTopic[1] - 1;
      let vals = JSON.parse(message).value;
      if(vals.length === 4) {
        vals.forEach(function(v, i) {
          v = Number(v);
          sensorId = i;
          let m = {};
          m.vehicleId = vehicleId;
          m.sensorId = sensorId;
          m.timestamp = Math.trunc(Date.now() / 1000);
          m.value = v;
          pushVal(vehicleId, sensorId, v);
          m.average = calcAvg(vehicleId, sensorId);
          io.emit('sensorData', m);
        });
      }
      break;
  }

  if(processMessage) {
    if (sensorId !== null && vehicleId !== null) {
      if (sensorId == 100) {
        //This is an anomaly
        console.log("ANOMALY")
        console.log(message.toString())
        message = JSON.parse(message.toString());
        message.vehicleId = vehicleId;
        message.sensorId = sensorId;
        io.emit('anomalyData', message);
      }
      else {
        //normal sensor reading
        message = JSON.parse(message.toString());
        message.vehicleId = vehicleId;
        message.sensorId = sensorId;
        message.timestamp = Math.trunc(message.timestamp / 1000);
        pushVal(vehicleId, sensorId, message.value);
        message.average = calcAvg(vehicleId, sensorId);
        io.emit('sensorData', message);
      }
    }
    else {
      console.log('ERROR: Vehicle/Sensor ID could not be created!');
    }
  }
});

// Send graph configuration on client connection
io.on('connection', function(socket) {
  socket.on('dataGenerationModes', function(data) {
    client.publish('vehicle/1/generator', JSON.stringify({mode: data.vehicle1Mode}));
    client.publish('vehicle/2/generator', JSON.stringify({mode: data.vehicle2Mode}));
    client.publish('vehicle/3/generator', JSON.stringify({mode: data.vehicle3Mode}));
    client.publish('vehicle/4/generator', JSON.stringify({mode: data.vehicle4Mode}));
  });

  socket.emit('configuration', {
    graphConfig: config.graphConfig
  });
});

app.listen(config.port);