package com.demo.rbc.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    private String status = "UP";

    @GetMapping
    public String healthCheck() {
        return status;
    }

    @PostMapping("/set-status")
    public String setStatus() {
        this.status = "DOWN";
        return "Status set to DOWN";
    }
}