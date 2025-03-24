package com.example.wordcounter.controller;

import com.example.wordcounter.dto.NodeRegistration;
import com.example.wordcounter.service.CoordinatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Profile("coordinator")
public class CoordinatorController {
    @Autowired
    private CoordinatorService coordinatorService;

    @PostMapping("/register")
    public void registerNode(@RequestBody NodeRegistration registration) {
        coordinatorService.registerNode(registration);
    }

    @PostMapping("/start-processing")
    public void startProcessing() throws IOException {
        coordinatorService.startProcessing();
    }
}