package com.info6250.unichef.controller;


import com.info6250.unichef.model.Chef;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChefDashboardController {

    @GetMapping("/chef/dashboard")
    public String getDashboard(){
            return "chef-dashboard";
    }



}
