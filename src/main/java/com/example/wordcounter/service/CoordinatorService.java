package com.example.wordcounter.service;

import com.example.wordcounter.dto.ClusterState;
import com.example.wordcounter.dto.LetterRange;
import com.example.wordcounter.dto.NodeRegistration;
import com.example.wordcounter.dto.NodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@Profile("coordinator")
public class CoordinatorService {
    @Value("${document.path}")
    private String docPath;

    private final List<String> proposers = new ArrayList<>();
    private final List<String> acceptors = new ArrayList<>();
    private String learner;

    @Autowired
    private RestTemplate restTemplate;

    public void registerNode(NodeRegistration registration) {
        switch (registration.getType()) {
            case PROPOSER:
                proposers.add(registration.getAddress());
                break;
            case ACCEPTOR:
                acceptors.add(registration.getAddress());
                break;
            case LEARNER:
                learner = registration.getAddress();
                break;
        }
        broadcastClusterState();
    }

    private void broadcastClusterState() {
        ClusterState state = new ClusterState(proposers, acceptors, learner);
        if (learner != null) {
            restTemplate.postForEntity(learner + "/cluster-state", state, Void.class);
        }
        proposers.forEach(p -> restTemplate.postForEntity(p + "/cluster-state", state, Void.class));
        acceptors.forEach(a -> restTemplate.postForEntity(a + "/cluster-state", state, Void.class));
    }

    public void startProcessing() throws IOException {
        assignRanges();
        processDocument();
    }

    private void assignRanges() {
        List<LetterRange> ranges = RangeCalculator.calculateRanges(proposers.size());
        IntStream.range(0, proposers.size()).forEach(i ->
                restTemplate.postForEntity(proposers.get(i) + "/assign-range", ranges.get(i), Void.class));
    }

    private void processDocument() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(docPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                final String currentLine = line; // Final copy for lambda
                proposers.forEach(p ->
                        restTemplate.postForEntity(
                                p + "/process-line",
                                currentLine,
                                Void.class
                        )
                );
            }
            proposers.forEach(p ->
                    restTemplate.postForEntity(
                            p + "/end-processing",
                            null,
                            Void.class
                    )
            );
        }
    }
}