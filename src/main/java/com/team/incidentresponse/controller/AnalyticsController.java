package com.team.incidentresponse.controller;

import com.team.incidentresponse.model.Incident;
import com.team.incidentresponse.repository.IncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.util.*;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private IncidentRepository incidentRepository;

    @GetMapping("/incidents")
    public Map<String, Object> getAnalyticsData() {
        List<Incident> incidents = incidentRepository.findAll();
        
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("dailyIncidents", getDailyIncidents(incidents));
        analytics.put("severityCounts", getSeverityCounts(incidents));
        analytics.put("weeklyTrend", getWeeklyTrend(incidents));
        analytics.put("weeklyResolved", getWeeklyResolved(incidents));
        analytics.put("typeCounts", getTypeCounts(incidents));
        analytics.put("responseTimeCounts", getResponseTimeCounts(incidents));
        
        return analytics;
    }

    private int[] getDailyIncidents(List<Incident> incidents) {
        int[] dailyCounts = new int[7]; // Mon-Sun
        LocalDateTime weekStart = LocalDateTime.now().minusDays(7);
        
        for (Incident incident : incidents) {
            if (incident.getCreatedAt().isAfter(weekStart)) {
                DayOfWeek day = incident.getCreatedAt().getDayOfWeek();
                int index = (day.getValue() - 1) % 7; // Mon=0, Sun=6
                dailyCounts[index]++;
            }
        }
        return dailyCounts;
    }

    private Map<String, Integer> getSeverityCounts(List<Incident> incidents) {
        Map<String, Integer> counts = new HashMap<>();
        counts.put("CRITICAL", 0);
        counts.put("HIGH", 0);
        counts.put("MEDIUM", 0);
        counts.put("LOW", 0);
        
        for (Incident incident : incidents) {
            String severity = incident.getSeverity().name();
            counts.put(severity, counts.get(severity) + 1);
        }
        return counts;
    }

    private int[] getWeeklyTrend(List<Incident> incidents) {
        int[] weeklyCounts = new int[4];
        LocalDateTime now = LocalDateTime.now();
        
        for (int i = 0; i < 4; i++) {
            LocalDateTime weekStart = now.minusWeeks(i + 1);
            LocalDateTime weekEnd = now.minusWeeks(i);
            
            int count = 0;
            for (Incident incident : incidents) {
                if (incident.getCreatedAt().isAfter(weekStart) && incident.getCreatedAt().isBefore(weekEnd)) {
                    count++;
                }
            }
            weeklyCounts[3 - i] = count; // Reverse order for chart
        }
        return weeklyCounts;
    }

    private int[] getWeeklyResolved(List<Incident> incidents) {
        int[] resolvedCounts = new int[4];
        LocalDateTime now = LocalDateTime.now();
        
        for (int i = 0; i < 4; i++) {
            LocalDateTime weekStart = now.minusWeeks(i + 1);
            LocalDateTime weekEnd = now.minusWeeks(i);
            
            int count = 0;
            for (Incident incident : incidents) {
                if (incident.getStatus() == Incident.Status.RESOLVED &&
                    incident.getCreatedAt().isAfter(weekStart) && incident.getCreatedAt().isBefore(weekEnd)) {
                    count++;
                }
            }
            resolvedCounts[3 - i] = count;
        }
        return resolvedCounts;
    }

    private Map<String, Integer> getTypeCounts(List<Incident> incidents) {
        Map<String, Integer> counts = new HashMap<>();
        counts.put("Malware", 0);
        counts.put("Phishing", 0);
        counts.put("Suspicious Login", 0);
        counts.put("Data Breach", 0);
        
        for (Incident incident : incidents) {
            String type = incident.getType();
            if (type.toLowerCase().contains("malware") || type.toLowerCase().contains("trojan")) {
                counts.put("Malware", counts.get("Malware") + 1);
            } else if (type.toLowerCase().contains("phishing")) {
                counts.put("Phishing", counts.get("Phishing") + 1);
            } else if (type.toLowerCase().contains("login")) {
                counts.put("Suspicious Login", counts.get("Suspicious Login") + 1);
            } else {
                counts.put("Data Breach", counts.get("Data Breach") + 1);
            }
        }
        return counts;
    }

    private int[] getResponseTimeCounts(List<Incident> incidents) {
        int[] timeCounts = new int[4]; // <1min, 1-5min, 5-15min, >15min
        
        for (Incident incident : incidents) {
            if (incident.getStatus() == Incident.Status.RESOLVED) {
                // Simulate response time based on severity for now
                switch (incident.getSeverity()) {
                    case CRITICAL -> timeCounts[0]++; // <1min
                    case HIGH -> timeCounts[1]++;     // 1-5min
                    case MEDIUM -> timeCounts[2]++;   // 5-15min
                    case LOW -> timeCounts[3]++;      // >15min
                }
            }
        }
        return timeCounts;
    }
}