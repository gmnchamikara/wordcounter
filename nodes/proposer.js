const Sidecar = require("../lib/sidecar");

class Proposer {
  constructor() {
    this.sidecar = new Sidecar("proposer");
    this.range = null;
    this.counts = new Map();
    this.setupListeners();
  }

  setupListeners() {
    this.sidecar.client.subscribe(this.sidecar.topics.assign);
    this.sidecar.client.subscribe(this.sidecar.topics.line);

    this.sidecar.client.on("message", (topic, message) => {
      const msg = JSON.parse(message.toString());

      if (
        topic === this.sidecar.topics.assign &&
        msg.proposerId === this.sidecar.id
      ) {
        this.range = msg;
      } else if (topic === this.sidecar.topics.line) {
        msg.done ? this.sendResults() : this.processLine(msg.line);
      }
    });
  }

  processLine(line) {
    line.split(/\s+/).forEach((word) => {
      const firstChar = word[0].toUpperCase();
      if (this.inRange(firstChar)) {
        const entry = this.counts.get(firstChar) || {
          count: 0,
          words: new Set(),
        };
        entry.count++;
        entry.words.add(word.toLowerCase());
        this.counts.set(firstChar, entry);
      }
    });
  }

  inRange(char) {
    return char >= this.range.start && char <= this.range.end;
  }

  sendResults() {
    const results = Array.from(this.counts.entries()).map(([char, data]) => ({
      char,
      count: data.count,
      words: Array.from(data.words),
    }));

    this.sidecar.client.publish(
      this.sidecar.topics.propose,
      JSON.stringify({ proposerId: this.sidecar.id, results })
    );
  }
}

module.exports = Proposer;
