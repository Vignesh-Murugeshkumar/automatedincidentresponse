package com.team.incidentresponse.service;

import com.team.incidentresponse.model.Incident;
import com.team.incidentresponse.repository.IncidentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private final IncidentRepository incidentRepository;

    public AnalyticsService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    public Map<String, Object> getDashboardMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        LocalDateTime last24Hours = LocalDateTime.now().minusHours(24);
        LocalDateTime lastWeek = LocalDateTime.now().minusWeeks(1);
        
        metrics.put("totalIncidents", incidentRepository.count());
        metrics.put("highPriorityCount", incidentRepository.countBySeverity(Incident.Severity.HIGH));
        metrics.put("criticalCount", incidentRepository.countBySeverity(Incident.Severity.CRITICAL));
        metrics.put("resolvedCount", incidentRepository.countByStatus(Incident.Status.RESOLVED));
        metrics.put("activeCount", incidentRepository.countByStatus(Incident.Status.ACTIVE));
        metrics.put("trojanCount", incidentRepository.countByType("Trojan Detection"));
        metrics.put("last24Hours", incidentRepository.countByCreatedAtAfter(last24Hours));
        metrics.put("lastWeek", incidentRepository.countByCreatedAtAfter(lastWeek));
        metrics.put("avgResolutionTime", calculateAvgResolutionTime());
        
        return metrics;
    }

    public Map<String, Long> getIncidentsByType() {
        List<Object[]> results = incidentRepository.countIncidentsByType();
        Map<String, Long> typeStats = new HashMap<>();
        
        for (Object[] result : results) {
            typeStats.put((String) result[0], (Long) result[1]);
        }
        
        return typeStats;
    }

    private double calculateAvgResolutionTime() {
        List<Incident> resolved = incidentRepository.findByStatus(Incident.Status.RESOLVED);
        if (resolved.isEmpty()) return 0.0;
        
        return resolved.stream()
            .filter(i -> i.getUpdatedAt() != null)
            .mapToLong(i -> java.time.Duration.between(i.getCreatedAt(), i.getUpdatedAt()).toMinutes())
            .average()
            .orElse(0.0);
    }
}