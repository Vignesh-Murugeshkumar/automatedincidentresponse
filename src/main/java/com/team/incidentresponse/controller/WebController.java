package com.team.incidentresponse.controller;

import com.team.incidentresponse.model.Incident;
import com.team.incidentresponse.service.IncidentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WebController {

    private final IncidentService incidentService;

    public WebController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping("/incidents/{id}")
    public String incidentDetails(@PathVariable Long id, Model model) {
        Incident incident = incidentService.findById(id);
        if (incident == null) {
            return "redirect:/incidents";
        }
        model.addAttribute("incident", incident);
        model.addAttribute("title", "Incident Details");
        return "incident-details";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}