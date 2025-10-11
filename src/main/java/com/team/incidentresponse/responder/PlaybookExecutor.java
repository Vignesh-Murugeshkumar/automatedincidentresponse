package com.team.incidentresponse.responder;

import com.team.incidentresponse.model.Incident;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PlaybookExecutor {

    // Core method to execute a playbook for an incident
    public void execute(String playbookPath, Incident incident) {
        Map<String, Object> context = new HashMap<>();
        context.put("userId", incident.getUser());
        context.put("incidentId", incident.getId());

        System.out.println("Executing playbook: " + playbookPath + " for incident " + incident.getId());
        // Here, you can implement actual automation logic
    }

    // Convenience method for controller (added missing method)
    public void executePlaybook(Incident incident) {
        // Example: choose playbook based on severity
        String playbookPath;
        switch (incident.getSeverity()) {
            case HIGH, CRITICAL -> playbookPath = "playbooks/high_severity.yml";
            case MEDIUM -> playbookPath = "playbooks/medium_severity.yml";
            default -> playbookPath = "playbooks/low_severity.yml";
        }

        execute(playbookPath, incident);
    }
}
