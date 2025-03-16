package com.example.wordcounter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProposerService {
    private static final String ACCEPTOR_TOPIC = "acceptor-topic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private char startLetter;
    private char endLetter;

    /**
     * Listens for range assignments from the coordinator.
     */
    @KafkaListener(topics = "proposer-topic", groupId = "proposer-group")
    public void receiveAssignment(String message) {
        if (message.contains(":")) {
            String[] parts = message.split(":");
            if (parts.length == 2) {
                String range = parts[1];
                String[] rangeParts = range.split("-");
                if (rangeParts.length == 2) {
                    startLetter = rangeParts[0].charAt(0);
                    endLetter = rangeParts[1].charAt(0);
                    System.out.println("Proposer assigned range: " + startLetter + " - " + endLetter);
                }
            }
        } else {
            processTextLine(message);
        }
    }

    /**
     * Processes a line of text and counts words within the assigned range.
     */
    public void processTextLine(String line) {
        List<String> words = Arrays.asList(line.split("\\s+"));
        Map<Character, List<String>> filteredWords = words.stream()
                .map(String::toLowerCase)
                .filter(word -> !word.isEmpty())
                .filter(word -> word.charAt(0) >= Character.toLowerCase(startLetter) &&
                        word.charAt(0) <= Character.toLowerCase(endLetter))
                .collect(Collectors.groupingBy(word -> word.charAt(0)));

        sendWordCounts(filteredWords);
    }

    /**
     * Sends word count results to acceptors.
     */
    private void sendWordCounts(Map<Character, List<String>> wordCounts) {
        wordCounts.forEach((character, words) -> {
            String result = character + ":" + words.size() + ":" + String.join(",", words);
            kafkaTemplate.send(ACCEPTOR_TOPIC, result);
            System.out.println("Sent to acceptor: " + result);
        });
    }
}
