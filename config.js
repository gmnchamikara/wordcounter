// config.js

module.exports = {
  MQTT_BROKER_URL: "mqtt://localhost:1883", // or your remote broker

  // MQTT Topics
  TOPICS: {
    CLUSTER_ANNOUNCE: "cluster/announce", // Coordinator announces active nodes
    LETTER_ASSIGNMENTS: "cluster/assignments", // Coordinator sends letter ranges to proposers
    DOCUMENT_LINES: "document/lines", // Document lines streamed to proposers
    PROPOSALS: "proposer/proposals", // Proposers send word counts to acceptors
    VALIDATED_COUNTS: "acceptor/validated", // Acceptors send validated data to learner
    FINAL_RESULT: "learner/final", // Learner broadcasts final results
    LOGGING: "sidecar/logs", // Sidecar logs
  },

  // Roles
  ROLES: {
    COORDINATOR: "coordinator",
    PROPOSER: "proposer",
    ACCEPTOR: "acceptor",
    LEARNER: "learner",
  },

  // Character ranges for proposers (can be dynamic too)
  LETTER_RANGES: [
    ["a", "c"],
    ["d", "f"],
    ["g", "i"],
    ["j", "l"],
    ["m", "o"],
    ["p", "r"],
    ["s", "u"],
    ["v", "z"],
  ],
};


const ROLES = {
  COORDINATOR: "coordinator",
  PROPOSER: "proposer",
  ACCEPTOR: "acceptor",
  LEARNER: "learner",
};

const TOPICS = {
  ANNOUNCE: "cluster/announce",
  CLUSTER_STATE: "cluster/state",
  ASSIGNMENTS: "proposer/assignments",
  LINES: "proposer/lines",
  PROPOSALS: "acceptor/proposals",
  VALIDATED_COUNTS: "learner/validated_counts",
  FINAL_RESULT: "learner/final_result",
};

module.exports = { ROLES, TOPICS };
