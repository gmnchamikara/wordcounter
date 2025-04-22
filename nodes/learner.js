const Sidecar = require("../lib/sidecar");

class Learner {
  constructor() {
    this.sidecar = new Sidecar("learner");
    this.results = new Map();
    this.setupListeners();
  }

  setupListeners() {
    this.sidecar.client.subscribe(this.sidecar.topics.result);

    this.sidecar.client.on("message", (topic, message) => {
      if (topic === this.sidecar.topics.result) {
        const { results } = JSON.parse(message.toString());
        results.forEach(({ char, count, words }) => {
          this.results.set(char, { count, words });
        });
        this.display();
      }
    });
  }

  display() {
    console.log("Final Results:");
    Array.from(this.results.entries()).forEach(([char, data]) => {
      console.log(`${char} ${data.count} ${data.words.join(", ")}`);
    });
  }
}

module.exports = Learner;
