package com.team.incidentresponse.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Scheduled(fixedRateString = "${scheduler.incident-check-interval:30000}")
    public void scanForIncidents() {
        System.out.println("🔄 Scheduled scan: checking for new incidents...");
    }
}