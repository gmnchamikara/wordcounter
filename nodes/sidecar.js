const client = require("../common/mqttClient");
const log = require("../common/logger");
const { TOPICS } = require("../config");

function publish(topic, message, role = "sidecar") {
  const msgStr = JSON.stringify(message);
  client.publish(topic, msgStr);
  log(role, `Published to ${topic}: ${msgStr}`);
}

function subscribe(topic, callback, role) {
  mqttClient.subscribe(topic, (message) => {
    if (typeof message === "string") {
      try {
        message = JSON.parse(message);
      } catch (_) {}
    }
    callback(message);
  });
}

function publish(topic, payload, role) {
  mqttClient.publish(topic, JSON.stringify(payload));
}

function announce(role, nodeId) {
  publish(TOPICS.ANNOUNCE, { role, nodeId }, role);
}

module.exports = { subscribe, publish, announce };