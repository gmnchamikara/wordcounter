const mqtt = require("mqtt");
const { MQTT_BROKER_URL } = require("../config");

const client = mqtt.connect(MQTT_BROKER_URL);

client.on("connect", () => {
  console.log(`[MQTT] Connected to broker at ${MQTT_BROKER_URL}`);
});

client.on("error", (err) => {
  console.error(`[MQTT] Error: ${err.message}`);
  client.end();
});

module.exports = client;