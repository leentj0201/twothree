package com.twothree.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import com.twothree.backend.dto.EmptyRequest;

@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @PostMapping("/health")
    public Map<String, Object> health(@RequestBody(required = false) EmptyRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Server is running");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
    
    @PostMapping("/public")
    public Map<String, String> publicEndpoint(@RequestBody(required = false) EmptyRequest request) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a public endpoint");
        return response;
    }
} 