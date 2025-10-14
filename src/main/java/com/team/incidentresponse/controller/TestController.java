package com.team.incidentresponse.controller;

import com.team.incidentresponse.model.Incident;
import com.team.incidentresponse.service.IncidentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    private final IncidentService incidentService;

    public TestController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping("/test/trojan")
    public ResponseEntity<String> testTrojan() {
        Incident incident = new Incident("Trojan Detection", "test.user", Incident.Severity.HIGH, 85);
        incidentService.createIncident(incident);
        return ResponseEntity.ok("Single trojan incident created");
    }

    @GetMapping("/test/phishing")
    public ResponseEntity<String> testPhishing() {
        Incident incident = new Incident("Phishing Email", "test.user", Incident.Severity.MEDIUM, 65);
        incidentService.createIncident(incident);
        return ResponseEntity.ok("Test phishing incident created");
    }

    @GetMapping("/test/breach")
    public ResponseEntity<String> testBreach() {
        Incident incident = new Incident("Security Breach", "test.user", Incident.Severity.CRITICAL, 95);
        incidentService.createIncident(incident);
        return ResponseEntity.ok("Test breach incident created");
    }

    @GetMapping("/test/ddos")
    public ResponseEntity<String> testDDoS() {
        Incident incident = new Incident("DDoS Attack", "network.monitor", Incident.Severity.HIGH, 88);
        incident.setDescription("Unusual traffic spike detected from multiple IPs");
        incidentService.createIncident(incident);
        return ResponseEntity.ok("DDoS attack incident created");
    }

    @GetMapping("/test/login")
    public ResponseEntity<String> testSuspiciousLogin() {
        Incident incident = new Incident("Suspicious Login", "admin", Incident.Severity.MEDIUM, 65);
        incident.setDescription("Login attempt from unusual location: Russia");
        incidentService.createIncident(incident);
        return ResponseEntity.ok("Suspicious login incident created");
    }

    @GetMapping("/test/data")
    public ResponseEntity<String> testDataExfiltration() {
        Incident incident = new Incident("Data Exfiltration", "file.monitor", Incident.Severity.CRITICAL, 98);
        incident.setDescription("Large file transfer to external server detected");
        incidentService.createIncident(incident);
        return ResponseEntity.ok("Data exfiltration incident created");
    }

    @GetMapping("/test/insider")
    public ResponseEntity<String> testInsiderThreat() {
        Incident incident = new Incident("Insider Threat", "hr.system", Incident.Severity.HIGH, 82);
        incident.setDescription("Employee accessing files outside normal scope");
        incidentService.createIncident(incident);
        return ResponseEntity.ok("Insider threat incident created");
    }

    @GetMapping("/test/network")
    public ResponseEntity<String> testNetworkIntrusion() {
        Incident incident = new Incident("Network Intrusion", "firewall", Incident.Severity.HIGH, 90);
        incident.setDescription("Unauthorized network access attempt blocked");
        incidentService.createIncident(incident);
        return ResponseEntity.ok("Network intrusion incident created");
    }

    @GetMapping("/scan/device")
    public ResponseEntity<String> scanDevice() {
        try {
            StringBuilder result = new StringBuilder("🔍 Device Scan Report\n\n");
            int threatsFound = 0;
            int filesScanned = 0;
            
            String userHome = System.getProperty("user.home");
            result.append("🏠 User Home: ").append(userHome).append("\n\n");
            
            String[] scanPaths = {
                userHome + "\\Desktop",
                userHome + "\\OneDrive\\Desktop",
                userHome + "\\Downloads",
                "C:\\temp"
            };
            
            String[] suspiciousPatterns = {
                "trojan", "malware", "virus", "backdoor", "keylogger", 
                "ransomware", "spyware", "hack", "crack", "keygen"
            };
            
            for (String path : scanPaths) {
                java.io.File dir = new java.io.File(path);
                result.append("📁 ").append(path).append(": ");
                
                if (!dir.exists()) {
                    result.append("❌ Directory not found\n");
                    continue;
                }
                
                if (!dir.isDirectory()) {
                    result.append("❌ Not a directory\n");
                    continue;
                }
                
                result.append("✅ Scanning...\n");
                
                java.io.File[] allFiles = dir.listFiles();
                if (allFiles == null) {
                    result.append("   ⚠️ Cannot read directory\n");
                    continue;
                }
                
                result.append("   Total files in directory: ").append(allFiles.length).append("\n");
                
                for (java.io.File file : allFiles) {
                    if (file.isFile()) {
                        String fileName = file.getName().toLowerCase();
                        
                        // Show ALL files for debugging
                        result.append("   📄 ").append(file.getName());
                        
                        if (fileName.endsWith(".exe") || fileName.endsWith(".bat")) {
                            filesScanned++;
                            result.append(" (").append(file.length()).append(" bytes) [EXECUTABLE]\n");
                            
                            // Check for suspicious patterns
                            for (String pattern : suspiciousPatterns) {
                                if (fileName.contains(pattern)) {
                                    result.append("   🚨 THREAT DETECTED: Contains '").append(pattern).append("'\n");
                                    threatsFound++;
                                    
                                    Incident incident = new Incident("Trojan Detection", "File Scanner", Incident.Severity.HIGH, 95);
                                    incident.setDescription("Suspicious file: " + file.getAbsolutePath());
                                    incidentService.createIncident(incident);
                                    break;
                                }
                            }
                        } else {
                            result.append(" [SKIPPED]\n");
                        }
                    }
                }
            }
            
            result.append("\n📊 SCAN SUMMARY\n");
            result.append("Files scanned: ").append(filesScanned).append("\n");
            result.append("Threats found: ").append(threatsFound).append("\n");
            
            if (threatsFound > 0) {
                result.append("🚨 ACTION REQUIRED: Check dashboard for incidents\n");
            } else {
                result.append("✅ No threats detected\n");
            }
            
            return ResponseEntity.ok(result.toString());
            
        } catch (Exception e) {
            return ResponseEntity.ok("❌ Scan failed: " + e.getMessage());
        }
    }

    @GetMapping("/scan/processes")
    public ResponseEntity<String> scanProcesses() {
        try {
            ProcessBuilder pb = new ProcessBuilder("tasklist");
            Process process = pb.start();
            return ResponseEntity.ok("🔍 Process scan completed");
        } catch (Exception e) {
            return ResponseEntity.ok("❌ Process scan failed: " + e.getMessage());
        }
    }

    @GetMapping("/cleanup/trojans")
    public ResponseEntity<String> cleanupTrojans() {
        try {
            incidentService.deleteByType("Trojan Detection");
            return ResponseEntity.ok("✅ All trojan incidents cleared");
        } catch (Exception e) {
            return ResponseEntity.ok("❌ Cleanup failed: " + e.getMessage());
        }
    }
}