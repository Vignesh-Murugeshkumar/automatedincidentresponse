package com.team.incidentresponse.service;

import com.team.incidentresponse.model.Incident;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SMSNotificationService {

    @Autowired(required = false)
    private TwilioSMSService twilioSMSService;

    public void sendSMSAlert(Incident incident) {
        String smsMessage = String.format(
            "SECURITY ALERT: %s detected. Severity: %s. Check dashboard immediately.",
            incident.getType(), incident.getSeverity()
        );
        
        String phoneNumber = "+919940194051";
        
        System.out.println("📱 Sending SMS Alert...");
        
        if (twilioSMSService != null) {
            boolean sent = twilioSMSService.sendSMS(phoneNumber, smsMessage);
            if (sent) {
                System.out.println("✅ Real SMS sent to: " + phoneNumber);
            } else {
                System.out.println("⚠️ SMS simulation - Twilio not configured");
            }
        } else {
            System.out.println("📱 SMS SIMULATION:");
            System.out.println("📱 To: " + phoneNumber);
            System.out.println("📱 Message: " + smsMessage);
        }
        
        System.out.println("📱 Time: " + java.time.LocalDateTime.now());
    }
}