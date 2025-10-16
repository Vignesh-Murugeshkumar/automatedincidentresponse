package com.team.incidentresponse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {



    @GetMapping("/favicon.ico")
    public String favicon() {
        return "redirect:/css/cybershield.css";
    }
}