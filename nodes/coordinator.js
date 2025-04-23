const { TOPICS, ROLES, LETTER_RANGES } = require("../config");
const { readLines } = require("../common/utils");
const log = require("../common/logger");
const sidecar = require("./sidecar");
const path = require("path");

const coordinatorId = `coordinator-${Math.floor(Math.random() * 10000)}`;
log(ROLES.COORDINATOR, `${coordinatorId} started`);

const proposers = [];
const acceptors = [];
const learners = [];

sidecar.subscribe(TOPICS.ANNOUNCE, ({ role, nodeId }) => {
  if (role === ROLES.PROPOSER && !proposers.includes(nodeId)) {
    proposers.push(nodeId);
  } else if (role === ROLES.ACCEPTOR && !acceptors.includes(nodeId)) {
    acceptors.push(nodeId);
  } else if (role === ROLES.LEARNER && !learners.includes(nodeId)) {
    learners.push(nodeId);
  }

  const state = {
    timestamp: Date.now(),
    proposers,
    acceptors,
    learners,
  };

  log(ROLES.COORDINATOR, `📡 Updated cluster state: ${JSON.stringify(state)}`);
  sidecar.publish(TOPICS.CLUSTER_STATE, state, ROLES.COORDINATOR);
});

sidecar.announce(ROLES.COORDINATOR, coordinatorId);

function announceCluster() {
  const info = {
    proposers,
    acceptors,
    learner,
    letterRanges: LETTER_RANGES,
  };
  sidecar.publish(TOPICS.CLUSTER_ANNOUNCE, info, ROLES.COORDINATOR);
}

function assignLetterRanges() {
  const assignments = proposers.map((proposer, index) => ({
    proposerId: proposer.id,
    range: LETTER_RANGES[index % LETTER_RANGES.length],
  }));

  sidecar.publish(TOPICS.LETTER_ASSIGNMENTS, assignments, ROLES.COORDINATOR);
}

function streamDocument(filePath) {
  readLines(filePath, (line) => {
    sidecar.publish(TOPICS.DOCUMENT_LINES, { line }, ROLES.COORDINATOR);
  });
}

function registerNode(type, id) {
  if (type === ROLES.PROPOSER) proposers.push({ id });
  else if (type === ROLES.ACCEPTOR) acceptors.push({ id });
  else if (type === ROLES.LEARNER) learner = { id };

  announceCluster();
}

function init(filePath) {
  // Simulate node registration
  registerNode(ROLES.PROPOSER, "--- Proposer-1");
  registerNode(ROLES.PROPOSER, "--- Proposer-2");
  registerNode(ROLES.ACCEPTOR, "--- Acceptor-1");
  registerNode(ROLES.ACCEPTOR, "--- Acceptor-2");
  registerNode(ROLES.LEARNER, "--- Learner-1");

  assignLetterRanges();

  setTimeout(() => {
    streamDocument(filePath);
  }, 1000); // Give time for all nodes to subscribe
}

// Entry Point
if (require.main === module) {
  const filePath = path.join(__dirname, "../documents/sample.txt");
  log(ROLES.COORDINATOR, "Coordinator starting...");
  init(filePath);
}
