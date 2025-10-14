package com.team.incidentresponse.controller;

import com.team.incidentresponse.repository.IncidentRepository;
import com.team.incidentresponse.service.AnalyticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class DashboardController {

    private final IncidentRepository incidentRepository;
    private final AnalyticsService analyticsService;

    public DashboardController(IncidentRepository incidentRepository, AnalyticsService analyticsService) {
        this.incidentRepository = incidentRepository;
        this.analyticsService = analyticsService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Map<String, Object> metrics = analyticsService.getDashboardMetrics();
        model.addAllAttributes(metrics);
        model.addAttribute("incidents", incidentRepository.findTop10ByOrderByCreatedAtDesc());
        model.addAttribute("incidentsByType", analyticsService.getIncidentsByType());

        return "dashboard";
    }

    @GetMapping("/api/dashboard/metrics")
    @ResponseBody
    public Map<String, Object> getDashboardMetrics() {
        return analyticsService.getDashboardMetrics();
    }
}