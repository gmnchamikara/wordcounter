package com.example.wordcounter.dto;

import java.util.List;

public class ClusterState {
    private List<String> proposers;
    private List<String> acceptors;
    private String learner;

    // Required for JSON deserialization
    public ClusterState() {}

    // Add this constructor
    public ClusterState(List<String> proposers, List<String> acceptors, String learner) {
        this.proposers = proposers;
        this.acceptors = acceptors;
        this.learner = learner;
    }

    // Add getters
    public List<String> getProposers() {
        return proposers;
    }

    public List<String> getAcceptors() {
        return acceptors;
    }

    public String getLearner() {
        return learner;
    }

    // In ClusterState.java
    public void setProposers(List<String> proposers) {
        this.proposers = proposers;
    }

    public void setAcceptors(List<String> acceptors) {
        this.acceptors = acceptors;
    }

    public void setLearner(String learner) {
        this.learner = learner;
    }
}