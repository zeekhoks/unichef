package com.info6250.unichef.configuration;

import com.info6250.unichef.model.User;
import jakarta.servlet.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@Component
@Order(1)
public class AuthorizationFilter implements Filter {

    private static List<String> acceptedURLS = List.of("/","/login","/signup","/logout","/actuator/health");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        User user = (User) request.getSession().getAttribute("user");
        String requestURI = request.getRequestURI();
        if(!acceptedURLS.contains(requestURI)){
            if(user == null){
                response.sendRedirect("/login");
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }
}
