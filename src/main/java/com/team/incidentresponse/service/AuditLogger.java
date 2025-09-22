package com.team.incidentresponse.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AuditLogger {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogger.class);

    public void log(String user, String action, String entityId) {
        logger.info("User: {}, Action: {}, Entity ID: {}", user, action, entityId);
    }
}
