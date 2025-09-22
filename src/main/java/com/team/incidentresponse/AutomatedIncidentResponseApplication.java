package com.vignesh.incidentresponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AutomatedIncidentResponseApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutomatedIncidentResponseApplication.class, args);
    }
}
