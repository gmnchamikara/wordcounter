// index.js

const { ROLES } = require("./config");

const role = process.argv[2];

if (!role) {
  console.log("Usage: node index.js <role>");
  console.log("Available roles: coordinator, proposer, acceptor, learner");
  process.exit(1);
}

switch (role) {
  case ROLES.COORDINATOR:
    require("./nodes/coordinator");
    break;
  case ROLES.PROPOSER:
    require("./nodes/proposer");
    break;
  case ROLES.ACCEPTOR:
    require("./nodes/acceptor");
    break;
  case ROLES.LEARNER:
    require("./nodes/learner");
    break;
  default:
    console.error(`Invalid role "${role}"`);
    process.exit(1);
}
