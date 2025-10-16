package com.team.incidentresponse.controller;

import com.team.incidentresponse.repository.IncidentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class DashboardController {

    private final IncidentRepository incidentRepository;

    public DashboardController(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        long totalIncidents = incidentRepository.count();
        model.addAttribute("totalIncidents", totalIncidents);
        model.addAttribute("activeCount", incidentRepository.countByStatus(com.team.incidentresponse.model.Incident.Status.ACTIVE));
        model.addAttribute("resolvedCount", incidentRepository.countByStatus(com.team.incidentresponse.model.Incident.Status.RESOLVED));
        model.addAttribute("criticalCount", incidentRepository.countBySeverity(com.team.incidentresponse.model.Incident.Severity.CRITICAL));
        model.addAttribute("recentIncidents", incidentRepository.findAll());
        model.addAttribute("title", "Dashboard");
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "redirect:/";
    }

    @GetMapping("/incidents")
    public String incidents(Model model) {
        model.addAttribute("incidents", incidentRepository.findAll());
        model.addAttribute("title", "Incidents");
        return "incidents";
    }

    @GetMapping("/analytics")
    public String analytics(Model model) {
        model.addAttribute("title", "Analytics");
        return "analytics";
    }

    @GetMapping("/api/dashboard/stats")
    @ResponseBody
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalIncidents", incidentRepository.count());
        stats.put("activeCount", incidentRepository.countByStatus(com.team.incidentresponse.model.Incident.Status.ACTIVE));
        stats.put("resolvedCount", incidentRepository.countByStatus(com.team.incidentresponse.model.Incident.Status.RESOLVED));
        stats.put("criticalCount", incidentRepository.countBySeverity(com.team.incidentresponse.model.Incident.Severity.CRITICAL));
        return stats;
    }
}