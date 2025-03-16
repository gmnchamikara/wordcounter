package com.example.wordcounter.service;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class WordProcessorService {

    private final char startLetter;
    private final char endLetter;

    public WordProcessorService(char startLetter, char endLetter) {
        this.startLetter = Character.toLowerCase(startLetter);
        this.endLetter = Character.toLowerCase(endLetter);
    }

    /**
     * Process a given text and count words based on the assigned letter range.
     *
     * @param text The input text to process
     * @return A map of starting letter -> word count
     */
    public Map<Character, List<String>> processText(String text) {
        if (text == null || text.isEmpty()) {
            return Collections.emptyMap();
        }

        // Normalize text: remove punctuation & convert to lowercase
        text = text.replaceAll("[^a-zA-Z\\s]", "").toLowerCase();
        String[] words = text.split("\\s+");

        // Word grouping by first letter within the assigned range
        Map<Character, List<String>> wordGroups = Arrays.stream(words)
                .filter(word -> word.length() > 0)
                .filter(word -> word.charAt(0) >= startLetter && word.charAt(0) <= endLetter)
                .collect(Collectors.groupingBy(word -> word.charAt(0), TreeMap::new, Collectors.toList()));

        return wordGroups;
    }

    /**
     * Aggregates word groups into a count-based summary.
     *
     * @param wordGroups The grouped words
     * @return A map of letter -> word count
     */
    public Map<Character, Integer> countWords(Map<Character, List<String>> wordGroups) {
        return wordGroups.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().size()));
    }
}