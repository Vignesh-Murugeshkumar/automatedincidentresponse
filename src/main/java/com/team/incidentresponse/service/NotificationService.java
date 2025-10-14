package com.team.incidentresponse.service;

import com.team.incidentresponse.model.Incident;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(@Autowired(required = false) SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyNewIncident(Incident incident) {
        if (messagingTemplate != null) {
            messagingTemplate.convertAndSend("/topic/incidents", incident);
        }
    }

    public void notifyIncidentUpdate(Incident incident) {
        if (messagingTemplate != null) {
            messagingTemplate.convertAndSend("/topic/incidents/update", incident);
        }
    }
}