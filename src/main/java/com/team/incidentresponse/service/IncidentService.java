package com.team.incidentresponse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team.incidentresponse.model.Incident;
import com.team.incidentresponse.repository.IncidentRepository;
import com.team.incidentresponse.responder.PlaybookExecutor;
import com.team.incidentresponse.scoring.IncidentScoringEngine;
import java.util.List;

@Service
public class IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private IncidentScoringEngine scoringEngine;

    @Autowired
    private PlaybookExecutor playbookExecutor;

    @Autowired
    private AuditLogger auditLogger;

    // Handle new incident
    public void handleIncident(Incident incident) {
        // Calculate score
        double score = scoringEngine.calculateScore(incident);
        incident.setScore((int) score);

        // Convert string severity to enum
        incident.setSeverity(
            Incident.Severity.valueOf(scoringEngine.classifySeverity(score).toUpperCase())
        );

        // Save incident
        incidentRepository.save(incident);
        auditLogger.log("System", "Incident scored and saved", incident.getId().toString());

        // Execute playbook
        String playbookPath = "playbooks/" + incident.getType() + ".yml";
        playbookExecutor.execute(playbookPath, incident);
        auditLogger.log("System", "Playbook executed", incident.getId().toString());
    }

    // Retrieve incident by ID
    public Incident getIncidentById(Long id) {
        return incidentRepository.findById(id).orElse(null);
    }

    // List all incidents
    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    // Manually trigger playbook
    public void executePlaybookForIncident(Long id) {
        Incident incident = getIncidentById(id);
        if (incident != null) {
            String playbookPath = "playbooks/" + incident.getType() + ".yml";
            playbookExecutor.execute(playbookPath, incident);
            auditLogger.log("System", "Manual playbook execution", incident.getId().toString());
        }
    }
}
