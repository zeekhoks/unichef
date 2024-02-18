package com.info6250.unichef.controller;

import com.info6250.unichef.model.User;
import com.info6250.unichef.model.UserRole;
import com.info6250.unichef.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class LogoutController {

    @Autowired
    UserService userService;

    @GetMapping("/logout")
    public String handleLogout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/login";
    }
}
