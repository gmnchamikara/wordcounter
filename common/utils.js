const fs = require("fs");
const readline = require("readline");

// Check if a word starts with a letter within a specific range
function isInRange(word, range) {
  if (!word || !word.length) return false;
  const char = word[0].toLowerCase();
  return char >= range[0] && char <= range[1];
}

// Parse a line of text into words
function getWordsFromLine(line) {
  return line
    .split(/\s+/)
    .map((w) => w.toLowerCase().replace(/[^a-z]/gi, ""))
    .filter(Boolean);
}

// Read a file line by line
async function readLines(filePath, onLine) {
  const rl = readline.createInterface({
    input: fs.createReadStream(filePath),
    crlfDelay: Infinity,
  });

  for await (const line of rl) {
    onLine(line);
  }
}

module.exports = {
  isInRange,
  getWordsFromLine,
  readLines,
};
