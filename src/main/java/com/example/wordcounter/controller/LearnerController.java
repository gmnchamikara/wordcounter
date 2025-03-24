package com.example.wordcounter.controller;

import com.example.wordcounter.dto.ProposerResult;
import com.example.wordcounter.dto.WordCount;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@Profile("learner")
public class LearnerController {
    private final Map<Character, WordCount> finalCounts = new ConcurrentHashMap<>();

    private final Set<String> processedProposers = ConcurrentHashMap.newKeySet();

    @PostMapping("/finalize")
    public synchronized ResponseEntity<?> finalizeResults(@RequestBody ProposerResult result) {
        if (result == null || result.getCounts() == null || result.getProposerAddress() == null) {
            return ResponseEntity.badRequest().body("Invalid result");
        }

        if (!processedProposers.add(result.getProposerAddress())) {
            return ResponseEntity.ok().build(); // Already processed
        }

        result.getCounts().forEach((key, value) ->
                finalCounts.merge(key, value, (oldVal, newVal) -> {
                    oldVal.getWords().addAll(newVal.getWords());
                    oldVal.setCount(oldVal.getCount() + newVal.getCount());
                    return oldVal;
                }));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/results")
    public Map<Character, Map<String, Object>> getResults() {
        return finalCounts.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> Map.of(
                                "count", e.getValue().getCount(),
                                "words", e.getValue().getWords()
                        )
                ));
    }
}