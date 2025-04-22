const fs = require("fs");
const Sidecar = require("../lib/sidecar");

class Coordinator {
  constructor() {
    debug("Initializing coordinator");
    this.sidecar = new Sidecar("coordinator");
    this.proposers = new Set();
    this.acceptors = new Set();
    this.learners = new Set();
    this.setupListeners();
    this.sidecar.client.on("error", (err) => {
      debug("MQTT error:", err);
    });
  }

  setupListeners() {
    this.sidecar.client.on("message", (topic, message) => {
      const msg = JSON.parse(message.toString());
      if (topic === this.sidecar.topics.register) {
        this.handleRegistration(msg);
      }
    });
  }

  handleRegistration({ id, role }) {
    switch (role) {
      case "proposer":
        this.proposers.add(id);
        break;
      case "acceptor":
        this.acceptors.add(id);
        break;
      case "learner":
        this.learners.add(id);
        break;
    }
    console.log(`Registered ${role}: ${id}`);
  }

  assignRanges() {
    const ranges = this.calculateRanges(this.proposers.size);
    const proposers = Array.from(this.proposers);

    ranges.forEach((range, idx) => {
      this.sidecar.client.publish(
        this.sidecar.topics.assign,
        JSON.stringify({
          proposerId: proposers[idx],
          start: range.start,
          end: range.end,
        })
      );
    });
  }

  calculateRanges(numProposers) {
    const letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    const rangeSize = Math.ceil(26 / numProposers);
    return Array.from({ length: numProposers }, (_, i) => ({
      start: letters[i * rangeSize],
      end: letters[Math.min((i + 1) * rangeSize - 1, 25)],
    }));
  }

  processDocument(path) {
    const lines = fs.readFileSync(path, "utf-8").split("\n");
    lines.forEach((line) => {
      this.sidecar.client.publish(
        this.sidecar.topics.line,
        JSON.stringify({ line })
      );
    });
    this.sidecar.client.publish(
      this.sidecar.topics.line,
      JSON.stringify({ done: true })
    );
  }
}

// Usage: new Coordinator().processDocument('input.txt');
module.exports = Coordinator;
