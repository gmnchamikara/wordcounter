package com.example.wordcounter.service;

import com.example.wordcounter.dto.LetterRange;
import java.util.ArrayList;
import java.util.List;

public class RangeCalculator {
    public static List<LetterRange> calculateRanges(int numProposers) {
        List<LetterRange> ranges = new ArrayList<>();
        if (numProposers <= 0) return ranges;

        char[] letters = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        int totalLetters = letters.length;
        int lettersPerProposer = Math.max(1, totalLetters / numProposers);
        int remaining = totalLetters % numProposers;

        int index = 0;
        for (int i = 0; i < numProposers && index < totalLetters; i++) {
            int endIndex = index + lettersPerProposer + (i < remaining ? 1 : 0) - 1;
            endIndex = Math.min(endIndex, totalLetters - 1);
            ranges.add(new LetterRange(letters[index], letters[endIndex]));
            index = endIndex + 1;
        }
        return ranges;
    }
}