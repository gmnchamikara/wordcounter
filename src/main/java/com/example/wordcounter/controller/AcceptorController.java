package com.example.wordcounter.controller;

import com.example.wordcounter.service.AcceptorService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/acceptor")
public class AcceptorController {

    private final AcceptorService acceptorService;

    public AcceptorController(AcceptorService acceptorService) {
        this.acceptorService = acceptorService;
    }

    @PostMapping("/validate")
    public String validateData(@RequestBody Map<String, Integer> wordCounts) {
        boolean isValid = acceptorService.validateCounts(wordCounts);
        return isValid ? "Data is valid!" : "Validation failed!";
    }
}
