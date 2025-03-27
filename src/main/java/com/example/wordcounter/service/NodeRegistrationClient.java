//package com.example.wordcounter.service;
//
//import com.example.wordcounter.dto.NodeRegistration;
//import com.example.wordcounter.dto.NodeType;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.annotation.Profile;
//import org.springframework.context.event.EventListener;
//import org.springframework.core.env.Environment;
//import org.springframework.retry.annotation.Backoff;
//import org.springframework.retry.annotation.Retryable;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//@Component
//@Profile("!coordinator")
//public class NodeRegistrationClient {
//    @Value("${node.address}")
//    private String nodeAddress;
//
//    @Value("${coordinator.url}")  // Use the correct property key
//    private String coordinatorUrl;
//
//    @Qualifier("")
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @Autowired
//    private Environment environment;
//
//    @EventListener(ApplicationReadyEvent.class)
//    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 1000, multiplier = 2))
//    public void registerOnStartup() {
//        NodeType type = determineNodeType();
//        NodeRegistration registration = new NodeRegistration(type, nodeAddress);
//        restTemplate.postForEntity(coordinatorUrl + "/register", registration, Void.class);
//    }
//
//    private NodeType determineNodeType() {
//        if (environment.acceptsProfiles("proposer")) {
//            return NodeType.PROPOSER;
//        } else if (environment.acceptsProfiles("acceptor")) {
//            return NodeType.ACCEPTOR;
//        } else if (environment.acceptsProfiles("learner")) {
//            return NodeType.LEARNER;
//        }
//        throw new IllegalStateException("Unknown node type - check active profiles");
//    }
//
//}

package com.example.wordcounter.service;

import com.example.wordcounter.dto.NodeRegistration;
import com.example.wordcounter.dto.NodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Profile("!coordinator")
public class NodeRegistrationClient {
    @Value("${node.address}")
    private String nodeAddress;

    @Value("${coordinator.url}")
    private String coordinatorUrl;

    @Autowired
    @Qualifier("simpleRestTemplate")  // Explicitly specify which bean to use
    private RestTemplate restTemplate;

    @Autowired
    private Environment environment;

    @EventListener(ApplicationReadyEvent.class)
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 1000, multiplier = 2))
    public void registerOnStartup() {
        NodeType type = determineNodeType();
        NodeRegistration registration = new NodeRegistration(type, nodeAddress);
        restTemplate.postForEntity(coordinatorUrl + "/register", registration, Void.class);
    }

    private NodeType determineNodeType() {
        if (environment.acceptsProfiles("proposer")) {
            return NodeType.PROPOSER;
        }
        if (environment.acceptsProfiles("acceptor")) {
            return NodeType.ACCEPTOR;
        }
        if (environment.acceptsProfiles("learner")) {
            return NodeType.LEARNER;
        }
        throw new IllegalStateException("""
            Unknown node type. Must activate one of these profiles:
            - 'proposer'
            - 'acceptor'
            - 'learner'
            Active profiles: %s
            """.formatted(String.join(", ", environment.getActiveProfiles())));
    }
}