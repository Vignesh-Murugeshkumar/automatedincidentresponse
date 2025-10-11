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
            store.connect(host, System.getenv("TEST_MAIL_USER"), System.getenv("TEST_MAIL_PASSWORD"));
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
            ex.printStackTrace();
        }
    }

    private void handleMessage(Message m) {
        try {
            String from = ((InternetAddress) m.getFrom()[0]).getAddress();
            String subject = m.getSubject();
            String body = m.getContent().toString();

            boolean suspect = false;
            if (subject != null && subject.toLowerCase().contains("reset password")) suspect = true;
            if (from.endsWith(".ru") || from.endsWith(".cn")) suspect = true;

            if (suspect) {
                Incident incident = new Incident();
                incident.setType("phishing_email");
                incident.setSeverity(Incident.Severity.MEDIUM);
                incident.setDescription("Potential phishing: " + subject + " from " + from);
                incidentService.handleIncident(incident);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
