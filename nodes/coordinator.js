const fs = require("fs");
const Sidecar = require("../lib/sidecar");

class Coordinator {
  constructor() {
    console.log("[Coordinator] Starting coordinator node...");
    this.sidecar = new Sidecar("coordinator");

    this.proposers = new Set();
    this.acceptors = new Set();
    this.learners = new Set();

    this.setupListeners();

    this.sidecar.client.on("error", (err) => {
      console.error("[Coordinator] MQTT error:", err);
    });
  }

  setupListeners() {
    console.log("[Coordinator] Setting up listeners...");
    this.sidecar.client.on("message", (topic, message) => {
      const msg = JSON.parse(message.toString());
      console.log(`[Coordinator] Message received on topic '${topic}':`, msg);

      if (topic === this.sidecar.topics.register) {
        this.handleRegistration(msg);
      }
    });
  }

  handleRegistration({ id, role }) {
    console.log(`[Coordinator] Registering new ${role}: ${id}`);
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
      default:
        console.warn(`[Coordinator] Unknown role received: ${role}`);
    }
    console.log(
      `[Coordinator] Current counts - Proposers: ${this.proposers.size}, Acceptors: ${this.acceptors.size}, Learners: ${this.learners.size}`
    );
  }

  assignRanges() {
    const ranges = this.calculateRanges(this.proposers.size);
    const proposers = Array.from(this.proposers);

    ranges.forEach((range, idx) => {
      const message = {
        proposerId: proposers[idx],
        start: range.start,
        end: range.end,
      };
      this.sidecar.client.publish(
        this.sidecar.topics.assign,
        JSON.stringify(message)
      );
      console.log(
        `[Coordinator] Assigned range to proposer ${proposers[idx]}: ${range.start} - ${range.end}`
      );
    });
  }

  calculateRanges(numProposers) {
    const letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    const rangeSize = Math.ceil(26 / numProposers);
    const ranges = Array.from({ length: numProposers }, (_, i) => ({
      start: letters[i * rangeSize],
      end: letters[Math.min((i + 1) * rangeSize - 1, 25)],
    }));
    console.log(
      `[Coordinator] Calculated ranges for ${numProposers} proposers:`,
      ranges
    );
    return ranges;
  }

  processDocument(path) {
    console.log(`[Coordinator] Processing document: ${path}`);
    const lines = fs.readFileSync(path, "utf-8").split("\n");
    lines.forEach((line) => {
      this.sidecar.client.publish(
        this.sidecar.topics.line,
        JSON.stringify({ line })
      );
      console.log(`[Coordinator] Published line: ${line}`);
    });
    this.sidecar.client.publish(
      this.sidecar.topics.line,
      JSON.stringify({ done: true })
    );
    console.log(`[Coordinator] Published DONE message`);
  }
}

module.exports = Coordinator;
