package com.example.wordcounter.service;

import com.example.wordcounter.dto.LetterRange;

import java.util.ArrayList;
import java.util.List;

public class RangeCalculator {
    public static List<LetterRange> calculateRanges(int numProposers) {
        List<LetterRange> ranges = new ArrayList<>();
        int chunkSize = 26 / numProposers;
        int remainder = 26 % numProposers;
        char current = 'a';

        for (int i = 0; i < numProposers; i++) {
            char end = (char) (current + chunkSize - 1 + (i < remainder ? 1 : 0));
            ranges.add(new LetterRange(current, end));
            current = (char) (end + 1);
        }
        return ranges;
    }
}
