// nodes/learner.js

const { ROLES, TOPICS } = require("../config");
const log = require("../common/logger");
const sidecar = require("./sidecar");

const learnerId = `learner-${Math.floor(Math.random() * 10000)}`;
const finalCounts = {};

const proposerId = `proposer-${Math.floor(Math.random() * 10000)}`;

// sidecar.announce(ROLES.PROPOSER, proposerId);
sidecar.announce(ROLES.LEARNER, learnerId); // ✅ Correct

sidecar.subscribe(
  
  TOPICS.VALIDATED_COUNTS,
  (msg) => {
    const { letter, count, words } = msg;

    const normalizedLetter = letter.toLowerCase();
    
    console.log("...[Learner] Subscribing to validated counts...");


    if (!finalCounts[normalizedLetter]) {
      finalCounts[normalizedLetter] = { count: 0, words: [] };
    }

    finalCounts[normalizedLetter].count += count;
    finalCounts[normalizedLetter].words.push(...words);

    log("***",ROLES.LEARNER, `Received count for ${letter}: ${count}`,"***");
  },
  ROLES.LEARNER
);

// Optionally broadcast final result every X seconds
setInterval(() => {
  sidecar.publish(TOPICS.FINAL_RESULT, finalCounts, ROLES.LEARNER);

  console.clear();
  console.log("\n +++++ 📘 Final Aggregated Word Counts (Learner):");
  Object.keys(finalCounts)
    .sort()
    .forEach((letter) => {
      const entry = finalCounts[letter];
      console.log("@@@@",`${letter} ➜ ${entry.count} [${entry.words.join(", ")}]`,"@@@");
    });
}, 5000);
