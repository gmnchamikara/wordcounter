const { ROLES, TOPICS } = require("../config");
const { isInRange, getWordsFromLine } = require("../common/utils");
const log = require("../common/logger");
const sidecar = require("./sidecar");

const proposerId = `proposer-${Math.floor(Math.random() * 10000)}`;
let myRange = null;

sidecar.announce(ROLES.PROPOSER, proposerId);

sidecar.subscribe(
  TOPICS.LETTER_ASSIGNMENTS,
  (assignments) => {
    const assignment = assignments.find((a) => a.proposerId === proposerId);
    if (assignment) {
      myRange = assignment.range;
      log(ROLES.PROPOSER, `${proposerId} assigned range ${myRange}`);
    }
  },
  ROLES.PROPOSER
);

sidecar.subscribe(
  TOPICS.DOCUMENT_LINES,
  ({ line }) => {
    if (!myRange) return;

    const words = getWordsFromLine(line);
    const result = {};

    for (const word of words) {
      if (isInRange(word, myRange)) {
        const char = word[0].toUpperCase();
        if (!result[char]) result[char] = { count: 0, words: [] };
        result[char].count++;
        result[char].words.push(word);
      }
    }

    for (const [letter, data] of Object.entries(result)) {
      sidecar.publish(
        TOPICS.PROPOSALS,
        {
          proposerId,
          letter,
          ...data,
        },
        ROLES.PROPOSER
      );
    }
  },
  ROLES.PROPOSER
);

log(ROLES.PROPOSER, `${proposerId} is listening...`);
