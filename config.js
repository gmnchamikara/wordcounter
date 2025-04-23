const ROLES = {
  COORDINATOR: "coordinator",
  PROPOSER: "proposer",
  ACCEPTOR: "acceptor",
  LEARNER: "learner",
};

const TOPICS = {
  ANNOUNCE: "cluster/announce",
  CLUSTER_STATE: "cluster/state",
  CLUSTER_ANNOUNCE: "cluster/announce",
  LETTER_ASSIGNMENTS: "cluster/assignments",
  DOCUMENT_LINES: "document/lines",
  PROPOSALS: "proposer/proposals",
  VALIDATED_COUNTS: "acceptor/validated",
  FINAL_RESULT: "learner/final",
  LOGGING: "sidecar/logs",
};

const LETTER_RANGES = [
  ["a", "c"],
  ["d", "f"],
  ["g", "i"],
  ["j", "l"],
  ["m", "o"],
  ["p", "r"],
  ["s", "u"],
  ["v", "z"],
];

const MQTT_BROKER_URL = "mqtt://mqtt-broker:1883";

module.exports = {
  ROLES,
  TOPICS,
  LETTER_RANGES,
  MQTT_BROKER_URL,
};
