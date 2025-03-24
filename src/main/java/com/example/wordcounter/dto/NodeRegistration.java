package com.example.wordcounter.dto;

public class NodeRegistration {
    private NodeType type;
    private String address;

    // Add constructors, getters, and setters
    public NodeRegistration() {}

    public NodeRegistration(NodeType type, String address) {
        this.type = type;
        this.address = address;
    }

    // Getters and setters
    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
