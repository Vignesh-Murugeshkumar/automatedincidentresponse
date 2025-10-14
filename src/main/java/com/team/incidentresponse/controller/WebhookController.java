package com.team.incidentresponse.controller;

import com.team.incidentresponse.model.Incident;
import com.team.incidentresponse.service.IncidentService;
import com.team.incidentresponse.responder.PlaybookExecutor;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/webhook")
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    private final IncidentService incidentService;
    private final PlaybookExecutor playbookExecutor;

    @Autowired
    public WebhookController(IncidentService incidentService, PlaybookExecutor playbookExecutor) {
        this.incidentService = incidentService;
        this.playbookExecutor = playbookExecutor;
    }

    @PostMapping
    public ResponseEntity<String> handleWebhook(@Valid @RequestBody Map<String, Object> payload) {
        logger.info("Received webhook payload: {}", payload.keySet());
        try {
            // Extract fields from payload
            String type = (String) payload.getOrDefault("type", "Unknown");
            String user = (String) payload.getOrDefault("user", "System");
            String description = (String) payload.getOrDefault("description", "No description provided");
            String severityStr = ((String) payload.getOrDefault("severity", "LOW")).toUpperCase();

            // Use nested enum correctly
            Incident.Severity severity;
            try {
                severity = Incident.Severity.valueOf(severityStr);
            } catch (IllegalArgumentException e) {
                severity = Incident.Severity.LOW;
            }

            int threatLevel = ((Number) payload.getOrDefault("threatLevel", 1)).intValue();
            int assetValue = ((Number) payload.getOrDefault("assetValue", 1)).intValue();
            double detectionConfidence = ((Number) payload.getOrDefault("detectionConfidence", 0.8)).doubleValue();

            int score = (int) ((threatLevel * assetValue) * detectionConfidence);

            // Build incident
            Incident incident = new Incident();
            incident.setType(type);
            incident.setUser(user);
            incident.setSeverity(severity);
            incident.setThreatLevel(threatLevel);
            incident.setAssetValue(assetValue);
            incident.setDetectionConfidence(detectionConfidence);
            incident.setScore(score);
            incident.setDescription(description);
            incident.setCreatedAt(LocalDateTime.now());
            incident.setStatus(Incident.Status.ACTIVE);

            // Persist incident
            incidentService.createIncident(incident);

            // Execute playbook
            playbookExecutor.executePlaybook(incident);

            return ResponseEntity.ok("Incident created successfully from webhook.");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Failed to process webhook: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> testWebhook() {
        return ResponseEntity.ok("Webhook endpoint active");
    }
}
