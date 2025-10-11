package com.team.incidentresponse.service;

import com.team.incidentresponse.model.Incident;
import com.team.incidentresponse.repository.IncidentRepository;
import com.team.incidentresponse.responder.PlaybookExecutor;
import com.team.incidentresponse.scoring.IncidentScoringEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final IncidentScoringEngine scoringEngine;
    private final PlaybookExecutor playbookExecutor;
    private final AuditLogger auditLogger;

    @Autowired
    public IncidentService(IncidentRepository incidentRepository,
                           IncidentScoringEngine scoringEngine,
                           PlaybookExecutor playbookExecutor,
                           AuditLogger auditLogger) {
        this.incidentRepository = incidentRepository;
        this.scoringEngine = scoringEngine;
        this.playbookExecutor = playbookExecutor;
        this.auditLogger = auditLogger;
    }

    // Handle new incident: score, save, and execute playbook
    public void handleIncident(Incident incident) {
        double score = scoringEngine.calculateScore(incident);
        incident.setScore((int) score);

        // classifySeverity returns string like "HIGH"
        String sev = scoringEngine.classifySeverity(score);
        try {
            incident.setSeverity(Incident.Severity.valueOf(sev));
        } catch (Exception e) {
            incident.setSeverity(Incident.Severity.MEDIUM);
        }

        incidentRepository.save(incident);
        if (auditLogger != null) auditLogger.log("system", "Incident created", incident.getId() == null ? "-" : incident.getId().toString());

        // execute playbook (async behavior could be added)
        playbookExecutor.executePlaybook(incident);
    }

    // Save incident directly
    public Incident createIncident(Incident incident) {
        return incidentRepository.save(incident);
    }

    // List all incidents
    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    // Find by ID
    public Incident getIncidentById(Long id) {
        return incidentRepository.findById(id).orElse(null);
    }

    // Trigger playbook for an existing incident
    public void executePlaybookForIncident(Long id) {
        Incident incident = getIncidentById(id);
        if (incident != null) {
            playbookExecutor.executePlaybook(incident);
            if (auditLogger != null) auditLogger.log("system", "Manual playbook execution", incident.getId().toString());
        }
    }
}
