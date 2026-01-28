package com.team.incidentresponse.service;

import com.team.incidentresponse.model.Incident;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import java.util.Properties;

@Service
public class ImapEmailPoller {

    private final IncidentService incidentService;

    public ImapEmailPoller(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @Scheduled(fixedDelayString = "${email.poll.interval:30000}")
    public void pollMailbox() {
        System.out.println("📬 Polling mailbox for new messages...");
        try {
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");
            Session session = Session.getInstance(props, null);
            // configure via application.properties: mail.username & mail.password
            String host = "imap.gmail.com";
            Store store = session.getStore();
            store.connect(host, "your mail", "your app password");
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);
            Message[] messages = inbox.search(new javax.mail.search.FlagTerm(new Flags(Flags.Flag.SEEN), false));
            for (Message m : messages) {
                handleMessage(m);
                m.setFlag(Flags.Flag.SEEN, true);
            }
            inbox.close(false);
            store.close();
        } catch (Exception ex) {
            System.err.println("Email polling failed: " + ex.getMessage());
            System.err.println("Set TEST_MAIL_USER and TEST_MAIL_PASSWORD environment variables for Gmail access");
        }
    }

    private void handleMessage(Message m) {
        try {
            String from = ((InternetAddress) m.getFrom()[0]).getAddress();
            String subject = m.getSubject() != null ? m.getSubject().toLowerCase() : "";
            String body = m.getContent().toString().toLowerCase();

            String[] phishingKeywords = {"urgent", "verify account", "click here", "suspended", 
                "confirm identity", "update payment", "security alert", "act now"};
            
            String[] suspiciousDomains = {".tk", ".ml", ".ga", ".cf", "tempmail", "guerrillamail"};
            
            boolean isPhishing = false;
            String reason = "";
            
            // Check for phishing keywords
            for (String keyword : phishingKeywords) {
                if (subject.contains(keyword) || body.contains(keyword)) {
                    isPhishing = true;
                    reason = "Phishing keyword detected: " + keyword;
                    break;
                }
            }
            
            // Check suspicious domains
            for (String domain : suspiciousDomains) {
                if (from.contains(domain)) {
                    isPhishing = true;
                    reason = "Suspicious domain: " + domain;
                    break;
                }
            }

            if (isPhishing) {
                Incident incident = new Incident();
                incident.setType("Phishing Email");
                incident.setUser(from);
                incident.setSeverity(Incident.Severity.HIGH);
                incident.setScore(85);
                incident.setDescription(reason + " - Subject: " + m.getSubject());
                incidentService.createIncident(incident);
                System.out.println("🚨 Phishing email detected from: " + from);
            }
        } catch (Exception e) {
            System.err.println("Error processing email: " + e.getMessage());
        }
    }
}
