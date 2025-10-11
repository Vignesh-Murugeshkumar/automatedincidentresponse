package com.team.incidentresponse.controller;

import com.team.incidentresponse.model.Incident;
import com.team.incidentresponse.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    @Autowired
    private IncidentService incidentService;

    // Report a new incident
    @PostMapping("/report")
    public ResponseEntity<String> reportIncident(@RequestBody Incident incident) {
        incidentService.handleIncident(incident);
        return ResponseEntity.ok("Incident received and processed.");
    }

    // Get incident by ID
    @GetMapping("/{id}")
    public ResponseEntity<Incident> getIncident(@PathVariable Long id) {
        Incident incident = incidentService.getIncidentById(id);
        return ResponseEntity.ok(incident);
    }

    // List all incidents
    @GetMapping("/all")
    public ResponseEntity<List<Incident>> getAllIncidents() {
        return ResponseEntity.ok(incidentService.getAllIncidents());
    }
    @PostMapping("/{id}/execute")
    public ResponseEntity<String> executePlaybook(@PathVariable Long id) {
        incidentService.executePlaybookForIncident(id);
        return ResponseEntity.ok("Playbook manually triggered.");
    }
}
