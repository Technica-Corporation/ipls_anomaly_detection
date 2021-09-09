var socket = io();

var vehicle1Mode = "normal";
var vehicle2Mode = "normal";
var vehicle3Mode = "normal";
var vehicle4Mode = "normal";

function sendData() {
  let m = {};
  m.vehicle1Mode = vehicle1Mode;
  m.vehicle2Mode = vehicle2Mode;
  m.vehicle3Mode = vehicle3Mode;
  m.vehicle4Mode = vehicle4Mode;
  socket.emit('dataGenerationModes', m);
}

var interval = setInterval(sendData, 3000);

$('#vehicle1NormalBtn').change(function() {
  vehicle1Mode = "normal";
  sendData();
});
$('#vehicle1AnomalousBtn').change(function() {
  vehicle1Mode = "anomaly";
  sendData();
});

$('#vehicle2NormalBtn').change(function() {
  vehicle2Mode = "normal";
  sendData();
});
$('#vehicle2AnomalousBtn').change(function() {
  vehicle2Mode = "anomaly";
  sendData();
});

$('#vehicle3NormalBtn').change(function() {
  vehicle3Mode = "normal";
  sendData();
});
$('#vehicle3AnomalousBtn').change(function() {
  vehicle3Mode = "anomaly";
  sendData();
});

$('#vehicle4NormalBtn').change(function() {
  vehicle4Mode = "normal";
  sendData();
});
$('#vehicle4AnomalousBtn').change(function() {
  vehicle4Mode = "anomaly";
  sendData();
});
