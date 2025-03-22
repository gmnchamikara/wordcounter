package com.example.wordcounter.controller;

import com.example.wordcounter.dto.ClusterState;
import com.example.wordcounter.dto.LetterRange;
import com.example.wordcounter.dto.ProposerResult;
import com.example.wordcounter.dto.WordCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Profile("proposer")
public class ProposerController {
    private final Map<Character, WordCount> counts = new ConcurrentHashMap<>();
    private LetterRange range;
    private List<String> acceptors;
    private String proposerAddress;

    @Autowired
    @Qualifier("simpleRestTemplate")
    private RestTemplate restTemplate;

    @Value("${node.address}")
    private String selfAddress;

    @PostMapping("/cluster-state")
    public ResponseEntity<?> updateClusterState(@RequestBody ClusterState state) {
        if (state == null || state.getAcceptors() == null || state.getProposers() == null) {
            return ResponseEntity.badRequest().body("Invalid cluster state");
        }
        this.acceptors = state.getAcceptors();
        this.proposerAddress = determineProposerAddress(state.getProposers());
        return ResponseEntity.ok().build();
    }

    private String determineProposerAddress(List<String> proposers) {
        return proposers.stream()
                .filter(address -> address.equals(selfAddress))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Current node not in proposers list"));
    }

    @PostMapping("/assign-range")
    public ResponseEntity<?> assignRange(@RequestBody LetterRange range) {
        if (range == null || !range.validate()) {
            return ResponseEntity.badRequest().body("Invalid letter range");
        }
        this.range = range;
        return ResponseEntity.ok().build();
    }

    @PostMapping("/process-line")
    public ResponseEntity<?> processLine(@RequestBody String line) {
        if (range == null) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Letter range not assigned");
        }

        Arrays.stream(line.split("[^\\p{L}]+"))  // Better Unicode support
                .filter(word -> !word.isEmpty())
                .forEach(word -> {
                    char firstChar = Character.toLowerCase(word.charAt(0));
                    if (range.contains(firstChar)) {
                        counts.computeIfAbsent(firstChar, k -> new WordCount())
                                .addWord(word);
                    }
                });

        return ResponseEntity.ok().build();
    }

    @PostMapping("/end-processing")
    public ResponseEntity<?> endProcessing() {
        if (counts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        ProposerResult result = new ProposerResult();
        result.setProposerAddress(this.proposerAddress); // Ensure proposerAddress is set (from cluster state)
        result.setCounts(counts);

        acceptors.forEach(a ->
                restTemplate.postForEntity(a + "/accept", result, Void.class));
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/end-processing")
//    public ResponseEntity<?> endProcessing() {
//        if (counts.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//
//        ProposerResult result = new ProposerResult(proposerAddress, counts);
//        acceptors.forEach(a ->
//                restTemplate.postForEntity(a + "/accept", result, Void.class));
//
//        return ResponseEntity.ok().build();
//    }

}