package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1.0")
public class HomeController {

    @GetMapping("/")
    public String home(){
        return "index";
    }

    
// DONE............
}
