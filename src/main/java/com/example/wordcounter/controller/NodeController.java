package com.example.wordcounter.controller;

import com.example.wordcounter.service.NodeService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/node")
public class NodeController {

    private final NodeService nodeService;

    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        return nodeService.getNodeStatus();
    }

    @PostMapping("/register")
    public String registerNode(@RequestBody Map<String, String> nodeInfo) {
        return nodeService.registerNode(nodeInfo);
    }
}