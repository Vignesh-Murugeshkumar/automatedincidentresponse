package com.team.incidentresponse.service;

import com.team.incidentresponse.model.Incident;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.nio.file.*;
import java.time.Instant;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
public class FileMonitorService {

    private final IncidentService incidentService;
    private final ConcurrentLinkedDeque<Long> renameTimestamps = new ConcurrentLinkedDeque<>();

    public FileMonitorService(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @PostConstruct
    public void startWatching() {
        Thread watcher = new Thread(this::watchLoop);
        watcher.setDaemon(true);
        watcher.start();
    }

    private void watchLoop() {
        Path path = Paths.get(System.getProperty("user.home"), "ransom-test"); // test folder
        try {
            Files.createDirectories(path);
            WatchService watchService = path.getFileSystem().newWatchService();
            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);
            System.out.println("🔔 Monitoring folder for suspicious activity: " + path);
            while (true) {
                WatchKey key = watchService.take();
                for (WatchEvent<?> ev : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = ev.kind();
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY || kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        long ts = Instant.now().toEpochMilli();
                        renameTimestamps.add(ts);
                        long cutoff = ts - 60_000L;
                        while (!renameTimestamps.isEmpty() && renameTimestamps.peekFirst() < cutoff) {
                            renameTimestamps.pollFirst();
                        }
                        if (renameTimestamps.size() > 50) { // threshold: >50 file ops in last 60s
                            Incident incident = new Incident();
                            incident.setType("ransomware_behaviour");
                            incident.setSeverity(Incident.Severity.CRITICAL);
                            incident.setDescription("High file activity in monitored test folder");
                            incidentService.handleIncident(incident);
                            renameTimestamps.clear();
                        }
                    }
                }
                key.reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
