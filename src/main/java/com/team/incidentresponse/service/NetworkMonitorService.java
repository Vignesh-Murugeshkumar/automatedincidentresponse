package com.team.incidentresponse.service;

import com.team.incidentresponse.model.Incident;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class NetworkMonitorService {

    private final IncidentService incidentService;
    private final Map<String, Integer> connectionCounts = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> lastChecked = new ConcurrentHashMap<>();
    
    // DDoS thresholds
    private static final int DDOS_THRESHOLD = 50; // connections per minute
    private static final int SUSPICIOUS_THRESHOLD = 20;

    public NetworkMonitorService(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    // @Scheduled(fixedRate = 60000) // Disabled automatic monitoring
    public void monitorNetworkTraffic() {
        // Disabled to prevent automatic DDoS/intrusion detection
    }

    private void checkNetworkConnections() {
        try {
            ProcessBuilder pb = new ProcessBuilder("netstat", "-an");
            Process process = pb.start();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int activeConnections = 0;
            int suspiciousConnections = 0;
            
            while ((line = reader.readLine()) != null) {
                if (line.contains("ESTABLISHED") || line.contains("SYN_SENT")) {
                    activeConnections++;
                    
                    // Check for suspicious patterns
                    if (line.contains(":80 ") || line.contains(":443 ") || line.contains(":22 ")) {
                        suspiciousConnections++;
                    }
                }
            }
            
            // DDoS Detection Logic
            if (activeConnections > DDOS_THRESHOLD) {
                createNetworkIncident("DDoS Attack", 
                    "High number of connections detected: " + activeConnections, 
                    Incident.Severity.CRITICAL, 95);
            } else if (suspiciousConnections > SUSPICIOUS_THRESHOLD) {
                createNetworkIncident("Suspicious Network Activity", 
                    "Unusual connection patterns detected: " + suspiciousConnections, 
                    Incident.Severity.HIGH, 80);
            }
            
        } catch (Exception e) {
            // Silent fail for demo
        }
    }

    private void checkPortScanning() {
        try {
            // Simulate port scan detection
            ProcessBuilder pb = new ProcessBuilder("netstat", "-an");
            Process process = pb.start();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int listenPorts = 0;
            
            while ((line = reader.readLine()) != null) {
                if (line.contains("LISTENING")) {
                    listenPorts++;
                }
            }
            
            // Check for unusual port activity (simplified detection)
            if (listenPorts > 30) {
                createNetworkIncident("Port Scanning Detected", 
                    "Unusual number of listening ports: " + listenPorts, 
                    Incident.Severity.MEDIUM, 65);
            }
            
        } catch (Exception e) {
            // Silent fail
        }
    }

    public void simulateDDoSAttack() {
        createNetworkIncident("DDoS Attack", 
            "Simulated attack: 1000+ requests per second from 50+ IPs", 
            Incident.Severity.CRITICAL, 98);
    }

    public void simulateNetworkIntrusion() {
        createNetworkIncident("Network Intrusion", 
            "Unauthorized access attempt from IP: 192.168.1.100", 
            Incident.Severity.HIGH, 88);
    }

    private void createNetworkIncident(String type, String description, Incident.Severity severity, int score) {
        Incident incident = new Incident();
        incident.setType(type);
        incident.setUser("Network Monitor");
        incident.setSeverity(severity);
        incident.setScore(score);
        incident.setDescription(description);
        incident.setCreatedAt(LocalDateTime.now());
        incident.setStatus(Incident.Status.ACTIVE);
        
        // This automatically sends SMS notification to +919940194051
        incidentService.createIncident(incident);
        
        System.out.println("🚨 Network incident created: " + type);
        System.out.println("📱 SMS notification sent for network security alert");
    }
}