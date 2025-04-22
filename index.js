const Coordinator = require("./nodes/coordinator");
const Proposer = require("./nodes/proposer");
const Acceptor = require("./nodes/acceptor");
const Learner = require("./nodes/learner");
require("dotenv").config();

const role = process.env.NODE_ROLE;

switch (role) {
  case "coordinator":
    const coordinator = new Coordinator();
    setTimeout(() => {
      coordinator.assignRanges();
      coordinator.processDocument("./input.txt");
    }, 5000); // Wait 5s for nodes to register
    break;
  case "proposer":
    new Proposer();
    break;
  case "acceptor":
    new Acceptor();
    break;
  case "learner":
    new Learner();
    break;
  default:
    throw new Error("Invalid role");
}
