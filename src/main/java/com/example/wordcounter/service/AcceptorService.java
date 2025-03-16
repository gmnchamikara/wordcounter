package com.example.wordcounter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AcceptorService {

    private static final String LEARNER_TOPIC = "learner-topic";
    private static final int ACCEPTANCE_THRESHOLD = 2; // Require at least 2 identical results

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // Cache to track received results
    private final Map<String, Integer> validationCache = new HashMap<>();

    /**
     * Listens for word count results from proposers and validates them.
     */
    @KafkaListener(topics = "acceptor-topic", groupId = "acceptor-group")
    public void receiveWordCount(String message) {
        System.out.println("Received from proposer: " + message);

        validationCache.put(message, validationCache.getOrDefault(message, 0) + 1);

        if (validationCache.get(message) >= ACCEPTANCE_THRESHOLD) {
            sendToLearner(message);
            validationCache.remove(message);
        }
    }

    /**
     * Sends validated word counts to the learner node.
     */
    private void sendToLearner(String message) {
        kafkaTemplate.send(LEARNER_TOPIC, message);
        System.out.println("Sent to learner: " + message);
    }
}