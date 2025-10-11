package com.team.incidentresponse.responder;

import java.util.Map;

public interface ResponderAction {
    void execute(Map<String, Object> context);
    String getName(); 
}
