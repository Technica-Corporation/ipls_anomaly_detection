/************************* CREATE GRAPH OBJECTS *************************/
var vehicleSensorData = [
  [
    [
      {
        label: 'v1Sensor1',
        values: []
      },
      {
        label: 'v1Sensor1Avg',
        values: []
      }
    ],
    [
      {
        label: 'v1Sensor2',
        values: []
      },
      {
        label: 'v1Sensor2Avg',
        values: []
      }
    ],
    [
      {
        label: 'v1Sensor3',
        values: []
      },
      {
        label: 'v1Sensor3Avg',
        values: []
      }
    ],
    [
      {
        label: 'v1Sensor4',
        values: []
      },
      {
        label: 'v1Sensor4Avg',
        values: []
      }
/*    ],
    [
      {
        label: 'v1Sensor5',
        values: []
      },
      {
        label: 'v1Sensor5Avg',
        values: []
      }
    ],
    [
      {
        label: 'v1Sensor6',
        values: []
      },
      {
        label: 'v1Sensor6Avg',
        values: []
      }
    ],
    [
      {
        label: 'v1Sensor7',
        values: []
      },
      {
        label: 'v1Sensor7Avg',
        values: []
      }
    ],
    [
      {
        label: 'v1Sensor8',
        values: []
      },
      {
        label: 'v1Sensor8Avg',
        values: []
      }
    ],
    [
      {
        label: 'v1Sensor9',
        values: []
      },
      {
        label: 'v1Sensor9Avg',
        values: []
      }
*/
    ]
  ],
  [
    [
      {
        label: 'v2Sensor1',
        values: []
      },
      {
        label: 'v2Sensor1Avg',
        values: []
      }
    ],
    [
      {
        label: 'v2Sensor2',
        values: []
      },
      {
        label: 'v2Sensor2Avg',
        values: []
      }
    ],
    [
      {
        label: 'v2Sensor3',
        values: []
      },
      {
        label: 'v2Sensor3Avg',
        values: []
      }
    ],
    [
      {
        label: 'v2Sensor4',
        values: []
      },
      {
        label: 'v2Sensor4Avg',
        values: []
      }
/*
    ],
    [
      {
        label: 'v2Sensor5',
        values: []
      },
      {
        label: 'v2Sensor5Avg',
        values: []
      }
    ],
    [
      {
        label: 'v2Sensor6',
        values: []
      },
      {
        label: 'v2Sensor6Avg',
        values: []
      }
    ],
    [
      {
        label: 'v2Sensor7',
        values: []
      },
      {
        label: 'v2Sensor7Avg',
        values: []
      }
    ],
    [
      {
        label: 'v2Sensor8',
        values: []
      },
      {
        label: 'v2Sensor8Avg',
        values: []
      }
    ],
    [
      {
        label: 'v2Sensor9',
        values: []
      },
      {
        label: 'v2Sensor9Avg',
        values: []
      }
*/
    ]
  ],
  [
    [
      {
        label: 'v3Sensor1',
        values: []
      },
      {
        label: 'v3Sensor1Avg',
        values: []
      }
    ],
    [
      {
        label: 'v3Sensor2',
        values: []
      },
      {
        label: 'v3Sensor2Avg',
        values: []
      }
    ],
    [
      {
        label: 'v3Sensor3',
        values: []
      },
      {
        label: 'v3Sensor3Avg',
        values: []
      }
    ],
    [
      {
        label: 'v3Sensor4',
        values: []
      },
      {
        label: 'v3Sensor4Avg',
        values: []
      }
/*    ],
    [
      {
        label: 'v3Sensor5',
        values: []
      },
      {
        label: 'v3Sensor5Avg',
        values: []
      }
    ],
    [
      {
        label: 'v3Sensor6',
        values: []
      },
      {
        label: 'v3Sensor6Avg',
        values: []
      }
    ],
    [
      {
        label: 'v3Sensor7',
        values: []
      },
      {
        label: 'v3Sensor7Avg',
        values: []
      }
    ],
    [
      {
        label: 'v3Sensor8',
        values: []
      },
      {
        label: 'v3Sensor8Avg',
        values: []
      }
    ],
    [
      {
        label: 'v3Sensor9',
        values: []
      },
      {
        label: 'v3Sensor9Avg',
        values: []
      }
*/
    ]
  ],
  [
    [
      {
        label: 'v4Sensor1',
        values: []
      },
      {
        label: 'v4Sensor1Avg',
        values: []
      }
    ],
    [
      {
        label: 'v4Sensor2',
        values: []
      },
      {
        label: 'v4Sensor2Avg',
        values: []
      }
    ],
    [
      {
        label: 'v4Sensor3',
        values: []
      },
      {
        label: 'v4Sensor3Avg',
        values: []
      }
    ],
    [
      {
        label: 'v4Sensor4',
        values: []
      },
      {
        label: 'v4Sensor4Avg',
        values: []
      }
/*
    ],
    [
      {
        label: 'v4Sensor5',
        values: []
      },
      {
        label: 'v4Sensor5Avg',
        values: []
      }
    ],
    [
      {
        label: 'v4Sensor6',
        values: []
      },
      {
        label: 'v4Sensor6Avg',
        values: []
      }
    ],
    [
      {
        label: 'v4Sensor7',
        values: []
      },
      {
        label: 'v4Sensor7Avg',
        values: []
      }
    ],
    [
      {
        label: 'v4Sensor8',
        values: []
      },
      {
        label: 'v4Sensor8Avg',
        values: []
      }
    ],
    [
      {
        label: 'v4Sensor9',
        values: []
      },
      {
        label: 'v4Sensor9Avg',
        values: []
      }
*/
    ]
  ]
];
var anomalyData = [
  [
    {
      label: 'v1AnomalyScores',
      values: []
    },
    {
      label: 'v1AnomalyThreshold',
      values: []
    }
  ],
  [
    {
      label: 'v2AnomalyScores',
      values: []
    },
    {
      label: 'v2AnomalyThreshold',
      values: []
    }
  ],
  [
    {
      label: 'v3AnomalyScores',
      values: []
    },
    {
      label: 'v3AnomalyThreshold',
      values: []
    }
  ],
  [
    {
      label: 'v4AnomalyScores',
      values: []
    },
    {
      label: 'v4AnomalyThreshold',
      values: []
    }
  ]
];

var v1Sensor1Chart = null;
var v1Sensor2Chart = null;
var v1Sensor3Chart = null;
var v1Sensor4Chart = null;
var v1Sensor5Chart = null;
var v1Sensor6Chart = null;
var v1Sensor7Chart = null;
var v1Sensor8Chart = null;
var v1Sensor9Chart = null;

var v2Sensor1Chart = null;
var v2Sensor2Chart = null;
var v2Sensor3Chart = null;
var v2Sensor4Chart = null;
var v2Sensor5Chart = null;
var v2Sensor6Chart = null;
var v2Sensor7Chart = null;
var v2Sensor8Chart = null;
var v2Sensor9Chart = null;

var v3Sensor1Chart = null;
var v3Sensor2Chart = null;
var v3Sensor3Chart = null;
var v3Sensor4Chart = null;
var v3Sensor5Chart = null;
var v3Sensor6Chart = null;
var v3Sensor7Chart = null;
var v3Sensor8Chart = null;
var v3Sensor9Chart = null;

var v4Sensor1Chart = null;
var v4Sensor2Chart = null;
var v4Sensor3Chart = null;
var v4Sensor4Chart = null;
var v4Sensor5Chart = null;
var v4Sensor6Chart = null;
var v4Sensor7Chart = null;
var v4Sensor8Chart = null;
var v4Sensor9Chart = null;

var v1AnomalyChart = null;
var v2AnomalyChart = null;
var v3AnomalyChart = null;
var v4AnomalyChart = null;

var vehicleSensorCharts = null;
var anomalyCharts = null;
/************************* END GRAPH OBJECTS *************************/

var socket = io();
var modes = [
  "Sensor Data Feed",
  "Anomaly Detection",
  "Federated Learning"
];
var vehicles = [
  "v1",
  "v2",
  "v3",
  "v4"
];
var selectedMode = 0;
var selectedVehicle = 0;

var configured = false;
var graphConfig = null;

var anomalyThreshold = 0.08;

//Callback when a vehicle tab is selected -- uses string interpolation
function clickVehicleTabLink(vehicleNum) {
  if(vehicleNum >= 0 && vehicleNum <= vehicles.length) {
    if(selectedVehicle !== vehicleNum) {
      selectedVehicle = vehicleNum;
      $(`.vehicle-tab-link:not(#${vehicles[vehicleNum]}TabLink)`).removeClass("active font-weight-bold");
      $(`#${vehicles[vehicleNum]}TabLink`).addClass("active font-weight-bold");
      if(selectedMode === 0) {
        $(`.vehicle-sensor-data:not(#${vehicles[vehicleNum]}SensorDataDisplay)`).removeClass("active-display");
        $(`.vehicle-sensor-data:not(#${vehicles[vehicleNum]}SensorDataDisplay)`).addClass("inactive-display");
        $(`#${vehicles[vehicleNum]}SensorDataDisplay`).removeClass("inactive-display");
        $(`#${vehicles[vehicleNum]}SensorDataDisplay`).addClass("active-display");
      }
      else {

      }
    }
  }
}

//Callback when a mode type is selected -- use string interpolation
function clickModeLink(modeNum) {
  if(modeNum >= 0 && modeNum <= modes.length) {
    if(selectedMode !== modeNum) {
      selectedMode = modeNum;
      $(`.mode-link:not(#modeLink${modeNum})`).removeClass("active");
      $(`#modeLink${modeNum}`).addClass("active");
      $(`.mode-display:not(#mode${modeNum}Display)`).removeClass("active-display");
      $(`.mode-display:not(#mode${modeNum}Display)`).addClass("inactive-display");
      $(`#mode${modeNum}Display`).removeClass("inactive-display");
      $(`#mode${modeNum}Display`).addClass("active-display");
    }
  }
}

$("#modeLink0").click(function() {
  clickModeLink(0);
});

$("#modeLink1").click(function() {
  clickModeLink(1);
});
$("#modeLink2").click(function() {
  clickModeLink(2);
});

$("#v1TabLink").click(function () {
  clickVehicleTabLink(0);
});

$("#v2TabLink").click(function () {
  clickVehicleTabLink(1);
});

$("#v3TabLink").click(function () {
  clickVehicleTabLink(2);
});

$("#v4TabLink").click(function () {
  clickVehicleTabLink(3);
});

socket.on('configuration', function(configData) {
  if(!configured) {
    configured = true;
    graphConfig = configData.graphConfig;
    console.log(graphConfig);

    v1Sensor1Chart = $('#v1Sensor1Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[0][0],
      range: graphConfig.sensor1.range,
      ticks: graphConfig.sensor1.ticks,
      axes: graphConfig.sensor1.axes,
      windowSize: 75
    });
    v1Sensor2Chart = $('#v1Sensor2Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[0][1],
      range: graphConfig.sensor2.range,
      ticks: graphConfig.sensor2.ticks,
      axes: graphConfig.sensor2.axes,
      windowSize: 75
    });
    v1Sensor3Chart = $('#v1Sensor3Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[0][2],
      range: graphConfig.sensor3.range,
      ticks: graphConfig.sensor3.ticks,
      axes: graphConfig.sensor3.axes,
      windowSize: 75
    });
    v1Sensor4Chart = $('#v1Sensor4Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[0][3],
      range: graphConfig.sensor4.range,
      ticks: graphConfig.sensor4.ticks,
      axes: graphConfig.sensor4.axes,
      windowSize: 75
    });
/*
    v1Sensor5Chart = $('#v1Sensor5Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[0][4],
      range: graphConfig.sensor5.range,
      ticks: graphConfig.sensor5.ticks,
      axes: graphConfig.sensor5.axes
    });
    v1Sensor6Chart = $('#v1Sensor6Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[0][5],
      range: graphConfig.sensor6.range,
      ticks: graphConfig.sensor6.ticks,
      axes: graphConfig.sensor6.axes
    });
    v1Sensor7Chart = $('#v1Sensor7Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[0][6],
      range: graphConfig.sensor7.range,
      ticks: graphConfig.sensor7.ticks,
      axes: graphConfig.sensor7.axes
    });
    v1Sensor8Chart = $('#v1Sensor8Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[0][7],
      range: graphConfig.sensor8.range,
      ticks: graphConfig.sensor8.ticks,
      axes: graphConfig.sensor8.axes
    });
    v1Sensor9Chart = $('#v1Sensor9Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[0][8],
      range: graphConfig.sensor9.range,
      ticks: graphConfig.sensor9.ticks,
      axes: graphConfig.sensor9.axes
    });
*/

    v2Sensor1Chart = $('#v2Sensor1Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[1][0],
      range: graphConfig.sensor1.range,
      ticks: graphConfig.sensor1.ticks,
      axes: graphConfig.sensor1.axes,
      windowSize: 75
    });
    v2Sensor2Chart = $('#v2Sensor2Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[1][1],
      range: graphConfig.sensor2.range,
      ticks: graphConfig.sensor2.ticks,
      axes: graphConfig.sensor2.axes,
      windowSize: 75
    });
    v2Sensor3Chart = $('#v2Sensor3Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[1][2],
      range: graphConfig.sensor3.range,
      ticks: graphConfig.sensor3.ticks,
      axes: graphConfig.sensor3.axes,
      windowSize: 75
    });
    v2Sensor4Chart = $('#v2Sensor4Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[1][3],
      range: graphConfig.sensor4.range,
      ticks: graphConfig.sensor4.ticks,
      axes: graphConfig.sensor4.axes,
      windowSize: 75
    });
/*
    v2Sensor5Chart = $('#v2Sensor5Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[1][4],
      range: graphConfig.sensor5.range,
      ticks: graphConfig.sensor5.ticks,
      axes: graphConfig.sensor5.axes
    });
    v2Sensor6Chart = $('#v2Sensor6Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[1][5],
      range: graphConfig.sensor6.range,
      ticks: graphConfig.sensor6.ticks,
      axes: graphConfig.sensor6.axes
    });
    v2Sensor7Chart = $('#v2Sensor7Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[1][6],
      range: graphConfig.sensor7.range,
      ticks: graphConfig.sensor7.ticks,
      axes: graphConfig.sensor7.axes
    });
    v2Sensor8Chart = $('#v2Sensor8Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[1][7],
      range: graphConfig.sensor8.range,
      ticks: graphConfig.sensor8.ticks,
      axes: graphConfig.sensor8.axes
    });
    v2Sensor9Chart = $('#v2Sensor9Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[1][8],
      range: graphConfig.sensor9.range,
      ticks: graphConfig.sensor9.ticks,
      axes: graphConfig.sensor9.axes
    });
*/

    v3Sensor1Chart = $('#v3Sensor1Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[2][0],
      range: graphConfig.sensor1.range,
      ticks: graphConfig.sensor1.ticks,
      axes: graphConfig.sensor1.axes,
      windowSize: 75
    });
    v3Sensor2Chart = $('#v3Sensor2Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[2][1],
      range: graphConfig.sensor2.range,
      ticks: graphConfig.sensor2.ticks,
      axes: graphConfig.sensor2.axes,
      windowSize: 75
    });
    v3Sensor3Chart = $('#v3Sensor3Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[2][2],
      range: graphConfig.sensor3.range,
      ticks: graphConfig.sensor3.ticks,
      axes: graphConfig.sensor3.axes,
      windowSize: 75
    });
    v3Sensor4Chart = $('#v3Sensor4Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[2][3],
      range: graphConfig.sensor4.range,
      ticks: graphConfig.sensor4.ticks,
      axes: graphConfig.sensor4.axes,
      windowSize: 75
    });
/*
    v3Sensor5Chart = $('#v3Sensor5Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[2][4],
      range: graphConfig.sensor5.range,
      ticks: graphConfig.sensor5.ticks,
      axes: graphConfig.sensor5.axes
    });
    v3Sensor6Chart = $('#v3Sensor6Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[2][5],
      range: graphConfig.sensor6.range,
      ticks: graphConfig.sensor6.ticks,
      axes: graphConfig.sensor6.axes
    });
    v3Sensor7Chart = $('#v3Sensor7Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[2][6],
      range: graphConfig.sensor7.range,
      ticks: graphConfig.sensor7.ticks,
      axes: graphConfig.sensor7.axes
    });
    v3Sensor8Chart = $('#v3Sensor8Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[2][7],
      range: graphConfig.sensor8.range,
      ticks: graphConfig.sensor8.ticks,
      axes: graphConfig.sensor8.axes
    });
    v3Sensor9Chart = $('#v3Sensor9Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[2][8],
      range: graphConfig.sensor9.range,
      ticks: graphConfig.sensor9.ticks,
      axes: graphConfig.sensor9.axes
    });
*/

    v4Sensor1Chart = $('#v4Sensor1Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[3][0],
      range: graphConfig.sensor1.range,
      ticks: graphConfig.sensor1.ticks,
      axes: graphConfig.sensor1.axes,
      windowSize: 75
    });
    v4Sensor2Chart = $('#v4Sensor2Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[3][1],
      range: graphConfig.sensor2.range,
      ticks: graphConfig.sensor2.ticks,
      axes: graphConfig.sensor2.axes,
      windowSize: 75
    });
    v4Sensor3Chart = $('#v4Sensor3Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[3][2],
      range: graphConfig.sensor3.range,
      ticks: graphConfig.sensor3.ticks,
      axes: graphConfig.sensor3.axes,
      windowSize: 75
    });
    v4Sensor4Chart = $('#v4Sensor4Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[3][3],
      range: graphConfig.sensor4.range,
      ticks: graphConfig.sensor4.ticks,
      axes: graphConfig.sensor4.axes,
      windowSize: 75
    });
/*
    v4Sensor5Chart = $('#v4Sensor5Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[3][4],
      range: graphConfig.sensor5.range,
      ticks: graphConfig.sensor5.ticks,
      axes: graphConfig.sensor5.axes
    });
    v4Sensor6Chart = $('#v4Sensor6Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[3][5],
      range: graphConfig.sensor6.range,
      ticks: graphConfig.sensor6.ticks,
      axes: graphConfig.sensor6.axes
    });
    v4Sensor7Chart = $('#v4Sensor7Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[3][6],
      range: graphConfig.sensor7.range,
      ticks: graphConfig.sensor7.ticks,
      axes: graphConfig.sensor7.axes
    });
    v4Sensor8Chart = $('#v4Sensor8Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[3][7],
      range: graphConfig.sensor8.range,
      ticks: graphConfig.sensor8.ticks,
      axes: graphConfig.sensor8.axes
    });
    v4Sensor9Chart = $('#v4Sensor9Chart').epoch({
      type: 'time.line',
      data: vehicleSensorData[3][8],
      range: graphConfig.sensor9.range,
      ticks: graphConfig.sensor9.ticks,
      axes: graphConfig.sensor9.axes
    });
*/

    v1AnomalyChart = $('#v1AnomalyChart').epoch({
      type: 'time.line',
      data: anomalyData[0],
      range: [0, 0.5],
      ticks:{  right: 5, left: 5 },
      axes: ['bottom','left'],
      windowSize: 150
    });
    v2AnomalyChart = $('#v2AnomalyChart').epoch({
      type: 'time.line',
      data: anomalyData[1],
      range: [0, 0.5],
      ticks:{  right: 5, left: 5 },
      axes: ['bottom','left'],
      windowSize: 150
    });
    v3AnomalyChart = $('#v3AnomalyChart').epoch({
      type: 'time.line',
      data: anomalyData[2],
      range: [0, 0.5],
      ticks:{  right: 5, left: 5 },
      axes: ['bottom','left'],
      windowSize: 150
    });
    v4AnomalyChart = $('#v4AnomalyChart').epoch({
      type: 'time.line',
      data: anomalyData[3],
      range: [0, 0.5],
      ticks:{  right: 5, left: 5 },
      axes: ['bottom','left'],
      windowSize: 150 
    });

    vehicleSensorCharts = [
      [
        v1Sensor1Chart,
        v1Sensor2Chart,
        v1Sensor3Chart,
        v1Sensor4Chart
/*        v1Sensor5Chart,
        v1Sensor6Chart,
        v1Sensor7Chart,
        v1Sensor8Chart,
        v1Sensor9Chart
*/
      ],
      [
        v2Sensor1Chart,
        v2Sensor2Chart,
        v2Sensor3Chart,
        v2Sensor4Chart
/*
        v2Sensor5Chart,
        v2Sensor6Chart,
        v2Sensor7Chart,
        v2Sensor8Chart,
        v2Sensor9Chart
*/
      ],
      [
        v3Sensor1Chart,
        v3Sensor2Chart,
        v3Sensor3Chart,
        v3Sensor4Chart
/*
        v3Sensor5Chart,
        v3Sensor6Chart,
        v3Sensor7Chart,
        v3Sensor8Chart,
        v3Sensor9Chart
*/
      ],
      [
        v4Sensor1Chart,
        v4Sensor2Chart,
        v4Sensor3Chart,
        v4Sensor4Chart
/*
        v4Sensor5Chart,
        v4Sensor6Chart,
        v4Sensor7Chart,
        v4Sensor8Chart,
        v4Sensor9Chart
*/
      ]
    ];

    anomalyCharts = [
	v1AnomalyChart,
	v2AnomalyChart,
	v3AnomalyChart,
	v4AnomalyChart
    ]

    // TODO change the value of the data.average object
    socket.on('sensorData', function(data) {
      vehicleSensorCharts[data.vehicleId][data.sensorId].push([
        {
          time: data.timestamp,
          y: data.value
        },
        {
          time: data.timestamp,
          y: data.average.value
        }
      ]);
    });

    socket.on('anomalyData', function(data) {
	anomalyCharts[data.vehicleId].push([
	  {
	    time: data.timestamp,
	    y: data.anomaly_score
	  },
	  {
	    time: data.timestamp,
	    y: anomalyThreshold
	  }
	]);
     });

    socket.on('fedlearning', function(data) {
	if(data.topic == 'model/live') {
    		$('#fedlearningprogress').addClass("bg-success");
		$('#fedlearningprogress').css("width", "100%");
		$('#fedlearningprogress').data("modelCount", 0);
	} else {
		currCount = $('#fedlearningprogress').data('modelCount');
		if(currCount == undefined) {
			currCount = 0;
		}

		currCount = currCount + 1;
		if(currCount >= 10) { 
			currCount = 10;
		}

		//get parent width and separate into segments
		$('#fedlearningprogress').css("width", currCount + "0%");
    		$('#fedlearningprogress').removeClass("bg-success");

		$('#fedlearningprogress').data("modelCount", currCount);
		$('#fedlearningprogress').text(currCount + " of 10");

		console.log("CurrCount=" + currCount);
	}		
     });
     
    // This must be done to load all the graph widths correctly
    $(".vehicle-sensor-data:not(#v1SensorDataDisplay)").addClass("inactive-display");
    $("#mode1Display").addClass("inactive-display");
    $("#mode2Display").addClass("inactive-display");
    $("#mode0Display").addClass("active-display");
  }
});
