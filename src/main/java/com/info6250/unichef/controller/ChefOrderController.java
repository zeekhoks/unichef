package com.info6250.unichef.controller;


import com.info6250.unichef.model.*;
import com.info6250.unichef.service.OrderService;
import com.info6250.unichef.service.PlanService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/chef/order")
public class ChefOrderController {
    @Autowired
    OrderService orderService;

    @Autowired
    PlanService planService;

    @GetMapping("/view/current")
    public ModelAndView getAllCurrentOrders(HttpServletRequest request){
        Chef chef = (Chef) request.getSession().getAttribute("user");
        ModelAndView modelAndView = new ModelAndView();
        List<Order> allOrders = orderService.getAllOrders();

        List<Order> currentOrders = allOrders.stream()
                .filter(order -> order.getPlans().stream()
                        .anyMatch(plan -> plan.getChef().getUser_id().equals(chef.getUser_id()))).toList();

        if (currentOrders.isEmpty()) {
            modelAndView.setViewName("errors");
        } else {
            modelAndView.addObject("orders", currentOrders);
            modelAndView.addObject("chef", chef);
            modelAndView.setViewName("view-all-current-orders-page-chef");
        }
        return modelAndView;
    }


    @GetMapping("/view/history")
    public ModelAndView getAllHistoryOrders(HttpServletRequest request){
        Chef chef = (Chef) request.getSession().getAttribute("user");
        ModelAndView modelAndView = new ModelAndView();
        List<Order> allOrders = orderService.getAllOrders();
        LocalDateTime today = LocalDateTime.now();

        List<Order> currentOrders = allOrders.stream()
                .filter(order -> order.getPlans().stream()
                        .anyMatch(plan -> plan.getChef().getUser_id().equals(chef.getUser_id())))
                .filter(order -> order.getPlans().stream()
                        .anyMatch(plan -> ChronoUnit.DAYS.between(today, plan.getDeliveryDate()) <= 0))
                .collect(Collectors.toList());

        modelAndView.addObject("orders", currentOrders);
        modelAndView.addObject("chef", chef);
        modelAndView.setViewName("view-all-history-orders-page-chef");
        return modelAndView;
    }

}
