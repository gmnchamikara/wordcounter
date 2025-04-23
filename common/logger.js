const client = require("./mqttClient");
const { TOPICS } = require("../config");

function log(role, message) {
  const logEntry = {
    role,
    message,
    timestamp: new Date().toISOString(),
  };

  client.publish(TOPICS.LOGGING, JSON.stringify(logEntry));
  console.log(`[${role.toUpperCase()}] ${message}`);
}

module.exports = log;
