package com.example.wordcounter.controller;

import com.example.wordcounter.service.ProposerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/proposer")
public class ProposerController {

    private final ProposerService proposerService;

    public ProposerController(ProposerService proposerService) {
        this.proposerService = proposerService;
    }

    @PostMapping("/process-text")
    public Map<String, Integer> processText(@RequestBody List<String> lines) {
        return proposerService.countWordsByLetterRange(lines);
    }

    @GetMapping("/assigned-range")
    public String getAssignedRange() {
        return proposerService.getAssignedLetterRange();
    }
}
