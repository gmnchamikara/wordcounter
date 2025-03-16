package com.example.wordcounter.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LearnerService {

    // Final word count storage
    private final Map<Character, List<String>> finalWordCounts = new HashMap<>();

    /**
     * Listens for validated word counts from acceptors.
     */
    @KafkaListener(topics = "learner-topic", groupId = "learner-group")
    public void receiveFinalWordCount(String message) {
        System.out.println("Received from acceptor: " + message);
        updateFinalResults(message);
        printFinalResults();
    }

    /**
     * Updates the final word count results.
     */
    private void updateFinalResults(String message) {
        String[] parts = message.split(":");
        if (parts.length == 3) {
            char startingLetter = parts[0].charAt(0);
            int count = Integer.parseInt(parts[1]);
            String[] words = parts[2].split(",");

            finalWordCounts.putIfAbsent(startingLetter, new ArrayList<>());
            finalWordCounts.get(startingLetter).addAll(Arrays.asList(words));
        }
    }

    /**
     * Prints the final word count summary.
     */
    private void printFinalResults() {
        System.out.println("\n=== Final Word Count ===");
        finalWordCounts.forEach((letter, words) ->
                System.out.println(letter + " (" + words.size() + "): " + String.join(", ", words))
        );
        System.out.println("========================\n");
    }
}
