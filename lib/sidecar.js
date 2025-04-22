const mqtt = require("mqtt");
const { v4: uuidv4 } = require("uuid");

class Sidecar {
  constructor(role) {
    this.role = role;
    this.id = uuidv4();
    console.log(`[Sidecar] Creating sidecar for role: ${role}, ID: ${this.id}`);

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
      console.log(`[Sidecar] Connected to MQTT broker`);
      this.client.subscribe(this.topics.register, () => {
        console.log(
          `[Sidecar] Subscribed to register topic: ${this.topics.register}`
        );
      });
      console.log(`[Sidecar] Subscribed as ${this.role} with ID ${this.id}`);
      this.register();
    });

    this.client.on("error", (err) => {
      console.error("[Sidecar] MQTT connection error:", err);
    });
  }

  register() {
    const payload = {
      id: this.id,
      role: this.role,
    };
    console.log(`[Sidecar] Registering node: ${JSON.stringify(payload)}`);
    this.client.publish(this.topics.register, JSON.stringify(payload));
  }
}

module.exports = Sidecar;
