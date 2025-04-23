// nodes/acceptor.js

const { ROLES, TOPICS } = require("../config");
const log = require("../common/logger");
const sidecar = require("./sidecar");

const acceptorId = `acceptor-${Math.floor(Math.random() * 10000)}`;

const proposerId = `proposer-${Math.floor(Math.random() * 10000)}`;

// sidecar.announce(ROLES.PROPOSER, proposerId);
sidecar.announce(ROLES.ACCEPTOR, acceptorId);

sidecar.subscribe(
  TOPICS.PROPOSALS,
  (proposal) => {
    const { proposerId, letter, count, words } = proposal;

    // In a real Paxos implementation, consensus logic goes here.
    // For this prototype, we assume it's always valid.

    log(
      ROLES.ACCEPTOR,
      `${acceptorId} accepted proposal from ${proposerId} for ${letter}: ${count}`
    );

    sidecar.publish(
      TOPICS.VALIDATED_COUNTS,
      {
        acceptorId,
        letter,
        count,
        words,
      },
      ROLES.ACCEPTOR
    );
  },
  ROLES.ACCEPTOR
);

log(ROLES.ACCEPTOR, `${acceptorId} is listening for proposals...`);
