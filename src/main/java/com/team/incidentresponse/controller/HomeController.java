package com.team.incidentresponse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/favicon.ico")
    public void favicon() {
        // Return empty response to prevent favicon errors
    }
}