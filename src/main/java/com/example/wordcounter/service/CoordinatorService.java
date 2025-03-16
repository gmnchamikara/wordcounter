package com.example.wordcounter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CoordinatorService {


    private static final String PROPOSER_TOPIC = "proposer-topic";
    private static final char[][] LETTER_RANGES = { {'A', 'C'}, {'D', 'F'}, {'G', 'I'}, {'J', 'L'}, {'M', 'O'}, {'P', 'R'}, {'S', 'U'}, {'V', 'Z'} };

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private List<String> proposerNodes;

    /**
     * Register proposer nodes in the system.
     * @param proposers List of proposer node IDs.
     */
    public void registerProposers(List<String> proposers) {
        this.proposerNodes = proposers;
        assignLetterRanges();
    }

    /**
     * Assigns letter ranges to available proposer nodes.
     */
    private void assignLetterRanges() {
        if (proposerNodes == null || proposerNodes.isEmpty()) {
            throw new IllegalStateException("No proposer nodes available.");
        }

        int numProposers = proposerNodes.size();
        Map<String, String> proposerAssignments = IntStream.range(0, numProposers)
                .boxed()
                .collect(Collectors.toMap(
                        proposerIndex -> proposerNodes.get(proposerIndex),
                        proposerIndex -> {
                            char start = LETTER_RANGES[proposerIndex % LETTER_RANGES.length][0];
                            char end = LETTER_RANGES[proposerIndex % LETTER_RANGES.length][1];
                            return start + "-" + end;
                        }
                ));

        // Notify proposers of their assigned range
        proposerAssignments.forEach((nodeId, range) -> {
            String message = nodeId + ":" + range;
            kafkaTemplate.send(PROPOSER_TOPIC, message);
            System.out.println("Assigned range " + range + " to proposer " + nodeId);
        });
    }

    /**
     * Sends a document line to all proposer nodes for processing.
     * @param line The line of text to process.
     */
    public void distributeLineToProposers(String line) {
        kafkaTemplate.send(PROPOSER_TOPIC, line);
    }

}
