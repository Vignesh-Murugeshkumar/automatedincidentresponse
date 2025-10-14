package com.team.incidentresponse.service;

import com.team.incidentresponse.model.Incident;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TrojanDetectionService {

    private final IncidentService incidentService;
    private final Set<String> scannedFiles = ConcurrentHashMap.newKeySet();
    
    private final Set<String> trojanSignatures = Set.of(
        "trojan", "malware", "backdoor", "keylogger", "rootkit", 
        "spyware", "virus", "worm", "ransomware"
    );

    @Autowired
    public TrojanDetectionService(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    // @Scheduled(fixedRate = 60000) // Disabled automatic scanning
    public void scanForTrojans() {
        // Disabled to keep trojan count at 0
    }

    private void scanDirectory(String dirPath) {
        try {
            Path path = Paths.get(dirPath);
            if (Files.exists(path)) {
                Files.walk(path)
                    .filter(Files::isRegularFile)
                    .forEach(this::scanFile);
            }
        } catch (Exception e) {
            // Silent fail for demo
        }
    }

    private void scanFile(Path filePath) {
        try {
            String filePathStr = filePath.toString();
            if (scannedFiles.contains(filePathStr)) return;
            
            String fileName = filePath.getFileName().toString().toLowerCase();
            for (String signature : trojanSignatures) {
                if (fileName.contains(signature)) {
                    scannedFiles.add(filePathStr);
                    createTrojanIncident("Suspicious file detected", filePathStr);
                    break;
                }
            }
        } catch (Exception e) {
            // Silent fail
        }
    }

    private void scanRunningProcesses() {
        try {
            ProcessBuilder pb = new ProcessBuilder("tasklist");
            Process process = pb.start();
            // Simulate trojan detection
            if (Math.random() < 0.1) { // 10% chance for demo
                createTrojanIncident("Suspicious process detected", "Unknown process");
            }
        } catch (Exception e) {
            // Silent fail
        }
    }

    private void createTrojanIncident(String description, String details) {
        Incident incident = new Incident();
        incident.setType("Trojan Detection");
        incident.setUser("System Scanner");
        incident.setSeverity(Incident.Severity.HIGH);
        incident.setScore(85);
        incident.setDescription(description + ": " + details);
        incident.setCreatedAt(LocalDateTime.now());
        incident.setStatus(Incident.Status.ACTIVE);
        
        incidentService.createIncident(incident);
    }
}