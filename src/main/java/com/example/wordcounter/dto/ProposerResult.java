package com.example.wordcounter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProposerResult {
    private String proposerAddress;
    private Map<Character, WordCount> counts;

    // Required for deserialization
    public ProposerResult() {}

    public ProposerResult(String proposerAddress, Map<Character, WordCount> counts) {
        this.proposerAddress = proposerAddress;
        this.counts = counts;
    }

    // Getters and setters
    public String getProposerAddress() { return proposerAddress; }
    public void setProposerAddress(String proposerAddress) { this.proposerAddress = proposerAddress; }

    public Map<Character, WordCount> getCounts() { return counts; }
    public void setCounts(Map<Character, WordCount> counts) { this.counts = counts; }
}