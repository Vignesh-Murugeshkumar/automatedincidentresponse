package com.team.incidentresponse.controller;

import com.team.incidentresponse.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @Autowired
    private IncidentService incidentService;

    @GetMapping({"/","/dashboard"})
    public String dashboard(Model model) {
    var incidents = incidentService.getAllIncidents();
    model.addAttribute("incidents", incidents);
    model.addAttribute("totalIncidents", incidents.size());

    long high = incidents.stream()
        .filter(i -> i.getSeverity() != null && i.getSeverity().name().equalsIgnoreCase("HIGH"))
        .count();
    long active = incidents.stream()
        .filter(i -> i.getStatus() != null && i.getStatus().name().equalsIgnoreCase("ACTIVE"))
        .count();
    long resolved = incidents.stream()
        .filter(i -> i.getStatus() != null && i.getStatus().name().equalsIgnoreCase("RESOLVED"))
        .count();

    model.addAttribute("highPriorityCount", high);
    model.addAttribute("activeCount", active);
    model.addAttribute("resolvedCount", resolved);

    return "dashboard";
    }
}
