package com.team.incidentresponse.scoring;

import org.springframework.stereotype.Component;
import com.team.incidentresponse.model.Incident;

@Component
public class IncidentScoringEngine {

    private final double threatWeight = 0.5;
    private final double assetWeight = 0.3;
    private final double confidenceWeight = 0.2;

    public double calculateScore(Incident incident) {
        int threatLevel = incident.getThreatLevel();           
        int assetValue = incident.getAssetValue();             
        double confidence = incident.getDetectionConfidence(); 

        return (threatWeight * threatLevel) +
               (assetWeight * assetValue) +
               (confidenceWeight * confidence);
    }

    public String classifySeverity(double score) {
        if (score >= 8.0) return "CRITICAL";
        if (score >= 5.0) return "HIGH";
        if (score >= 3.0) return "MEDIUM";
        return "LOW";
    }
}
