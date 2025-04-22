const mqtt = require("mqtt");
const { v4: uuidv4 } = require("uuid");

class Sidecar {
  constructor(role) {
    this.role = role;
    this.id = uuidv4();
    this.client = mqtt.connect("mqtt://localhost");
    this.topics = {
      register: "nodes/register",
      assign: "coordinator/assign",
      line: "proposers/line",
      propose: "acceptors/propose",
      result: "learners/result",
      done: "proposers/done",
    };
    this.setup();
  }

  setup() {
    this.client.on("connect", () => {
      console.log("Connected to MQTT broker");
      this.client.subscribe(this.topics.register);
      console.log(`Sidecar connected for ${this.role} ${this.id}`);
      this.register();
    });
  }

  register() {
    this.client.publish(
      this.topics.register,
      JSON.stringify({
        id: this.id,
        role: this.role,
      })
    );
  }
}

module.exports = Sidecar;
