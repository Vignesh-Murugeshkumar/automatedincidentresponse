package com.team.incidentresponse.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioSMSService {

    @Value("${twilio.account.sid:}")
    private String accountSid;

    @Value("${twilio.auth.token:}")
    private String authToken;

    @Value("${twilio.phone.number:}")
    private String fromPhoneNumber;

    private boolean initialized = false;

    private void initializeTwilio() {
        if (!initialized && !accountSid.isEmpty() && !authToken.isEmpty()) {
            try {
                Twilio.init(accountSid, authToken);
                initialized = true;
                System.out.println("✅ Twilio SMS service initialized");
            } catch (Exception e) {
                System.err.println("❌ Twilio initialization failed: " + e.getMessage());
            }
        }
    }

    public boolean sendSMS(String toPhoneNumber, String messageBody) {
        initializeTwilio();
        
        if (!initialized) {
            System.out.println("⚠️ Twilio not configured - SMS simulation mode");
            System.out.println("📱 Would send SMS to: " + toPhoneNumber);
            System.out.println("📱 Message: " + messageBody);
            return false;
        }

        try {
            Message message = Message.creator(
                new PhoneNumber(toPhoneNumber),
                new PhoneNumber(fromPhoneNumber),
                messageBody
            ).create();

            System.out.println("✅ SMS sent successfully!");
            System.out.println("📱 To: " + toPhoneNumber);
            System.out.println("📱 SID: " + message.getSid());
            return true;

        } catch (Exception e) {
            System.err.println("❌ SMS sending failed: " + e.getMessage());
            return false;
        }
    }
}