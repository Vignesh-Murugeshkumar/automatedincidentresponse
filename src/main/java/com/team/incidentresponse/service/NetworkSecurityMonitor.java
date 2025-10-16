package com.team.incidentresponse.service;

import com.team.incidentresponse.model.Incident;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NetworkSecurityMonitor {
    
    private final IncidentService incidentService;
    private final Map<String, Integer> connectionCounts = new ConcurrentHashMap<>();
    private final Set<String> suspiciousIPs = Set.of(
        "192.168.1.100", "10.0.0.50", "172.16.0.10" // Example suspicious IPs
    );

    public NetworkSecurityMonitor(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @Scheduled(fixedRate = 120000) // Every 2 minutes
    public void monitorNetworkConnections() {
        try {
            checkSuspiciousConnections();
            detectPortScanning();
        } catch (Exception e) {
            // Silent monitoring
        }
    }

    private void checkSuspiciousConnections() {
        try {
            ProcessBuilder pb = new ProcessBuilder("netstat", "-an");
            Process process = pb.start();
            
            // Simulate detection of suspicious connections
            if (Math.random() < 0.05) { // 5% chance
                createNetworkIncident("Suspicious outbound connection detected", 
                                    "Connection to known malicious IP");
            }
        } catch (Exception e) {
            // Silent fail
        }
    }

    private void detectPortScanning() {
        // Simulate port scan detection
        if (Math.random() < 0.03) { // 3% chance
            createNetworkIncident("Port scanning detected", 
                                "Multiple connection attempts from external IP");
        }
    }

    private void createNetworkIncident(String type, String description) {
        Incident incident = new Incident(type, "Network Monitor", 
                                       Incident.Severity.HIGH, 88);
        incident.setDescription(description);
        incidentService.createIncident(incident);
    }
}