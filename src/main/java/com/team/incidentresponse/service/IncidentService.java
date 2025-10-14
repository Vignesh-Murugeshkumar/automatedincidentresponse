package com.team.incidentresponse.service;

import com.team.incidentresponse.model.Incident;
import com.team.incidentresponse.repository.IncidentRepository;
import com.team.incidentresponse.responder.PlaybookExecutor;
import com.team.incidentresponse.scoring.IncidentScoringEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentService {

    private static final Logger logger = LoggerFactory.getLogger(IncidentService.class);

    private final IncidentRepository incidentRepository;
    private final IncidentScoringEngine scoringEngine;
    private final PlaybookExecutor playbookExecutor;
    private final AuditLogger auditLogger;
    private final NotificationService notificationService;
    private final SMSNotificationService smsNotificationService;

    @Autowired
    public IncidentService(IncidentRepository incidentRepository,
                           IncidentScoringEngine scoringEngine,
                           PlaybookExecutor playbookExecutor,
                           AuditLogger auditLogger,
                           @Autowired(required = false) NotificationService notificationService,
                           @Autowired(required = false) SMSNotificationService smsNotificationService) {
        this.incidentRepository = incidentRepository;
        this.scoringEngine = scoringEngine;
        this.playbookExecutor = playbookExecutor;
        this.auditLogger = auditLogger;
        this.notificationService = notificationService;
        this.smsNotificationService = smsNotificationService;
    }

    // Handle new incident: score, save, and execute playbook
    public void handleIncident(Incident incident) {
        double score = scoringEngine.calculateScore(incident);
        incident.setScore((int) score);

        String sev = scoringEngine.classifySeverity(score);
        try {
            incident.setSeverity(Incident.Severity.valueOf(sev));
        } catch (Exception e) {
            incident.setSeverity(Incident.Severity.MEDIUM);
        }

        Incident saved = incidentRepository.save(incident);
        if (auditLogger != null) auditLogger.log("system", "Incident created", saved.getId().toString());

        // Execute playbook asynchronously
        executePlaybookAsync(saved);
    }

    @Async
    public void executePlaybookAsync(Incident incident) {
        playbookExecutor.executePlaybook(incident);
    }

    // Save incident directly
    public Incident createIncident(Incident incident) {
        logger.info("Creating incident: type={}, user={}, severity={}", 
                   incident.getType(), incident.getUser(), incident.getSeverity());
        
        Incident saved = incidentRepository.save(incident);
        logger.debug("Incident saved with ID: {}", saved.getId());
        
        // Send notifications for ALL incidents
        sendIncidentNotifications(saved);
        
        if (notificationService != null) {
            notificationService.notifyNewIncident(saved);
        }
        
        logger.info("Incident created successfully: ID={}", saved.getId());
        return saved;
    }
    
    private void sendIncidentNotifications(Incident incident) {
        try {
            if (smsNotificationService != null) {
                // Send SMS for ALL incidents regardless of severity
                smsNotificationService.sendSMSAlert(incident);
                System.out.println("✅ SMS notification sent for " + incident.getType() + " incident (Severity: " + incident.getSeverity() + ")");
            }
        } catch (Exception e) {
            System.err.println("❌ SMS notification error: " + e.getMessage());
        }
    }

    // List all incidents
    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    // Find by ID
    public Incident getIncidentById(Long id) {
        return incidentRepository.findById(id).orElse(null);
    }

    public Incident findById(Long id) {
        return incidentRepository.findById(id).orElse(null);
    }

    public Incident updateIncident(Incident incident) {
        return incidentRepository.save(incident);
    }

    @org.springframework.transaction.annotation.Transactional
    public void deleteByType(String type) {
        incidentRepository.deleteByType(type);
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
