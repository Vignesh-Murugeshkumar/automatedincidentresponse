package com.team.incidentresponse.service;

import com.team.incidentresponse.model.Incident;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.Set;

@Service
public class RealTimeVirusScanner {
    
    private final IncidentService incidentService;
    private final Set<String> knownMalwareHashes = Set.of(
        "d41d8cd98f00b204e9800998ecf8427e", // Empty file hash (example)
        "5d41402abc4b2a76b9719d911017c592"  // "hello" hash (example)
    );

    public RealTimeVirusScanner(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    public boolean scanFile(Path filePath) {
        try {
            // Hash-based detection
            String fileHash = calculateMD5(filePath);
            if (knownMalwareHashes.contains(fileHash)) {
                createMalwareIncident("Known malware hash detected", filePath.toString());
                return true;
            }

            // Behavioral analysis
            if (hasSuspiciousBehavior(filePath)) {
                createMalwareIncident("Suspicious file behavior", filePath.toString());
                return true;
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private String calculateMD5(Path filePath) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] fileBytes = Files.readAllBytes(filePath);
        byte[] hashBytes = md.digest(fileBytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private boolean hasSuspiciousBehavior(Path filePath) {
        String fileName = filePath.getFileName().toString().toLowerCase();
        return fileName.matches(".*\\.(exe|bat|scr|pif|com|cmd)$") && 
               (fileName.contains("crack") || fileName.contains("keygen") || 
                fileName.length() > 50 || fileName.matches(".*[0-9]{8,}.*"));
    }

    private void createMalwareIncident(String description, String filePath) {
        Incident incident = new Incident("Malware Detection", "Virus Scanner", 
                                       Incident.Severity.CRITICAL, 95);
        incident.setDescription(description + ": " + filePath);
        incidentService.createIncident(incident);
    }
}