const mqtt = require("mqtt");

class MQTTService {
    constructor(host) {
        this.mqttClient = null;
        this.host = host;
    }

    connect() {
        this.mqttClient = mqtt.connect(this.host);

        this.mqttClient.on("connect", () => {
            console.log("MQTT client connected");
        });

        // Call the message callback function when message arrived
        this.mqttClient.on("message", function (topic, message) {
            console.log("Receive: " + message.toString());
        });
    
        this.mqttClient.on("close", () => {
            console.log(`MQTT client disconnected`);
        });
    }

    publish(topic, message) {
        console.log("Publish: " + message);
        this.mqttClient.publish(topic, message);
    }

    subscribe(topic) {
        this.mqttClient.subscribe(topic);
    }
}

module.exports = MQTTService;