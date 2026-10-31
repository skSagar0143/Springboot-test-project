package com.learning.springboottest.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public Map<String, String> hello() {
        return Map.of(
                "message", "Hello! Your Spring Boot CI/CD learning project is running.",
                "application", "springboot-test-project",
                "version", "1.0.0"
        );
    }
}