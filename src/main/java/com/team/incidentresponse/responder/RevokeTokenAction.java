package com.team.incidentresponse.responder;

import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class RevokeTokenAction implements ResponderAction {

    @Override
    public void execute(Map<String, Object> context) {
        String userId = (String) context.get("userId");
        System.out.println("Revoking token for user: " + userId);
    }

    @Override
    public String getName() {
        return "revokeToken";
    }
}
