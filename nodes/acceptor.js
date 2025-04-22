const Sidecar = require("../lib/sidecar");

class Acceptor {
  constructor() {
    this.sidecar = new Sidecar("acceptor");
    this.setupListeners();
  }

  setupListeners() {
    this.sidecar.client.subscribe(this.sidecar.topics.propose);

    this.sidecar.client.on("message", (topic, message) => {
      if (topic === this.sidecar.topics.propose) {
        const proposal = JSON.parse(message.toString());
        if (this.validate(proposal)) {
          this.sidecar.client.publish(
            this.sidecar.topics.result,
            JSON.stringify(proposal)
          );
        }
      }
    });
  }

  validate({ results }) {
    return results.every(({ char, words }) =>
      words.every((word) => word[0].toUpperCase() === char)
    );
  }
}

module.exports = Acceptor;
