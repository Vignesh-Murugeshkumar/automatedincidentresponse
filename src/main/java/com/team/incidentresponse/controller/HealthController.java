package com.team.incidentresponse.controller;

import com.team.incidentresponse.repository.IncidentRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    private final IncidentRepository incidentRepository;

    public HealthController(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    @GetMapping("/health/db")
    public String checkDatabase() {
        try {
            long count = incidentRepository.count();
            return "✅ Database connected. Incidents count: " + count;
        } catch (Exception e) {
            return "❌ Database connection failed: " + e.getMessage();
        }
    }
}