const mqtt = require("mqtt");
const { MQTT_BROKER_URL } = require("../config");

const client = mqtt.connect(MQTT_BROKER_URL);

const connectWithRetry = () => {
  const client = mqtt.connect(MQTT_BROKER_URL);
  client.on("connect", () => {
    console.log("Connected to MQTT broker ...!");
  });
  client.on("error", (err) => {
    console.error("Connection failed, retrying in 5s", err.message);
    setTimeout(connectWithRetry, 5000);
  });
  return client;
};

client.on("error", (err) => {
  console.error(`[MQTT] Error: ${err.message}`);
  client.end();
});

module.exports = connectWithRetry();