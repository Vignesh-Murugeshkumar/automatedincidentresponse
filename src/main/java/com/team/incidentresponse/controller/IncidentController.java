package com.team.incidentresponse.controller;

import com.team.incidentresponse.model.Incident;
import com.team.incidentresponse.service.IncidentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<String> resolveIncident(@PathVariable Long id) {
        try {
            Incident incident = incidentService.findById(id);
            if (incident != null) {
                incident.setStatus(Incident.Status.RESOLVED);
                incidentService.updateIncident(incident);
                return ResponseEntity.ok("Incident resolved");
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to resolve incident");
        }
    }

    @PostMapping("/{id}/dismiss")
    public ResponseEntity<String> dismissIncident(@PathVariable Long id) {
        try {
            Incident incident = incidentService.findById(id);
            if (incident != null) {
                incident.setStatus(Incident.Status.DISMISSED);
                incidentService.updateIncident(incident);
                return ResponseEntity.ok("Incident dismissed");
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to dismiss incident");
        }
    }
}