const mqttService = require("./service/mqttService");
const bleService = require('./service/bleService');

const MQTT_HOST_NAME = "mqtt://127.0.0.1:1883";

var mqttClient = new mqttService(MQTT_HOST_NAME);
mqttClient.connect();
// mqttClient.subscribe("ci_bluetooth");


var bleClient = new bleService();
bleClient.discoverAndPublish(mqttClient);