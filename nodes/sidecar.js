const client = require("../common/mqttClient");
const log = require("../common/logger");
const { TOPICS } = require("../config");

function publish(topic, message, role = "sidecar") {
  const msgStr = JSON.stringify(message);
  client.publish(topic, msgStr);
  log(role, `Published to ${topic}: ${msgStr}`);
}

function subscribe(topic, callback, role) {
  client.subscribe(topic);
  client.on("message", (receivedTopic, message) => {
    console.log(`!!!! [DEBUG] MQTT Message on ${topic}:`, message.toString());
    if (receivedTopic === topic) {
      try {
        const parsed = JSON.parse(message.toString());
        callback(parsed);
      } catch (err) {
        callback(message.toString());
      }
    }
  });
}

function announce(role, nodeId) {
  publish(TOPICS.ANNOUNCE, { role, nodeId }, role);
}

module.exports = { subscribe, publish, announce };