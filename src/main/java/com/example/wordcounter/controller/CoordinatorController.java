package com.example.wordcounter.controller;

import com.example.wordcounter.service.CoordinatorService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coordinator")
public class CoordinatorController {

    private final CoordinatorService coordinatorService;

    public CoordinatorController(CoordinatorService coordinatorService) {
        this.coordinatorService = coordinatorService;
    }

    @PostMapping("/assign-tasks")
    public String assignTasks() {
        coordinatorService.assignTasksToProposers();
        return "Tasks assigned successfully!";
    }

    @PostMapping("/broadcast")
    public String broadcastClusterInfo() {
        coordinatorService.broadcastClusterInfo();
        return "Cluster info broadcasted!";
    }
}
