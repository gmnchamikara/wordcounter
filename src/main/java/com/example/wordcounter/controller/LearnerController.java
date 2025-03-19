package com.example.wordcounter.controller;

import com.example.wordcounter.service.LearnerService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/learner")
public class LearnerController {

    private final LearnerService learnerService;

    public LearnerController(LearnerService learnerService) {
        this.learnerService = learnerService;
    }

    @PostMapping("/finalize-results")
    public String finalizeResults(@RequestBody Map<String, Integer> validatedCounts) {
        learnerService.processFinalResults(validatedCounts);
        return "Final results processed successfully!";
    }

    @GetMapping("/results")
    public Map<String, Integer> getResults() {
        return learnerService.getFinalWordCounts();
    }
}
