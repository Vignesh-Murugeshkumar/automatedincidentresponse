package com.team.incidentresponse.responder;

import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class NotifyUserAction implements ResponderAction {

    @Override
    public void execute(Map<String, Object> context) {
        String userId = (String) context.get("userId");
        String message = (String) context.getOrDefault("message", "Security incident detected");
        
        System.out.println("🚨 SECURITY ALERT NOTIFICATION");
        System.out.println("📧 Email sent to: security-team@company.com");
        System.out.println("📱 SMS sent to: +91-9940194051");
        System.out.println("💬 Message: " + message);
        System.out.println("👤 User: " + userId);
        System.out.println("⏰ Time: " + java.time.LocalDateTime.now());
        System.out.println("🔔 IMMEDIATE ACTION REQUIRED - Check dashboard!");
    }

    @Override
    public String getName() {
        return "notifyUser";
    }
}
