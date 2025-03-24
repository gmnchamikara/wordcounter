package com.example.wordcounter.controller;

import com.example.wordcounter.dto.ClusterState;
import com.example.wordcounter.dto.WordCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@Profile("acceptor")
public class AcceptorController {
    @Autowired
    private RestTemplate restTemplate;
    private String learner;

    @PostMapping("/cluster-state")
    public void updateClusterState(@RequestBody ClusterState state) {
        this.learner = state.getLearner();
    }

    @PostMapping("/accept")
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 100))
    public void acceptResults(@RequestBody Map<Character, WordCount> results) {
        if (validate(results)) {
            restTemplate.postForEntity(learner + "/finalize", results, Void.class);
        } else {
            throw new IllegalArgumentException("Invalid results received");
        }
    }

    private boolean validate(Map<Character, WordCount> results) {
        return results != null && results.entrySet().stream()
                .allMatch(e -> Character.isLetter(e.getKey()) &&
                        e.getValue() != null &&
                        e.getValue().getCount() > 0 &&
                        e.getValue().getCount() == e.getValue().getWords().size());
    }
}