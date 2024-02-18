package com.info6250.unichef.controller;


import com.info6250.unichef.model.*;
import com.info6250.unichef.service.EmailService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student/order")
public class StudentOrderController {
    @Autowired
    OrderService orderService;

    @Autowired
    PlanService planService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/create")
    public ModelAndView createOrder(HttpServletRequest request, Locale locale){
        Student student = (Student) request.getSession().getAttribute("user");
        Order order = new Order();
        order.setStudent(student);
        LocalDateTime today = LocalDateTime.now();
        order.setOrderDate(today);
        Set<Plan> orderItems = (Set<Plan>)request.getSession().getAttribute("orderItems");
        if(order.getPlans() == null){
            order.setPlans(new HashSet<>());
        }
        for (Plan orderItem : orderItems) {
            Plan temp = planService.getPlanFromId(orderItem.getPlanID());
            order.getPlans().add(temp);
        }
        orderService.createOrder(order, student, locale);

        request.getSession().removeAttribute("orderItems");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("order", order);
//        emailService.sendEmail(student.getEmail(), "Order Confirmation", "Thank you for your order!");
        modelAndView.setViewName("order-successful");
        return modelAndView;
    }

    @GetMapping("/view/current")
    public ModelAndView getAllCurrentOrders(HttpServletRequest request){
        Student student = (Student) request.getSession().getAttribute("user");
        ModelAndView modelAndView = new ModelAndView();
        List<Order> allOrders = orderService.getAllOrders();
        LocalDateTime today = LocalDateTime.now();

        List<Order> currentOrders = allOrders.stream()
                .filter(order -> order.getPlans().stream()
                        .anyMatch(plan -> ChronoUnit.DAYS.between(today, plan.getDeliveryDate()) >= 1))
                .filter(order -> order.getStudent().getUser_id().equals(student.getUser_id()))
                .collect(Collectors.toList());

        if (currentOrders.isEmpty()) {
            modelAndView.setViewName("errors");
        } else {
            modelAndView.addObject("orders", currentOrders);
            modelAndView.setViewName("view-all-current-orders-page-student");
        }
        return modelAndView;
    }


    @GetMapping("/view/history")
    public ModelAndView getAllHistoryOrders(HttpServletRequest request){
        Student student = (Student) request.getSession().getAttribute("user");
        ModelAndView modelAndView = new ModelAndView();
        List<Order> allOrders = orderService.getAllOrders();
        LocalDateTime today = LocalDateTime.now();

        List<Order> pastOrders = allOrders.stream()
                .filter(order -> order.getPlans().stream()
                        .anyMatch(plan -> ChronoUnit.DAYS.between(today, plan.getDeliveryDate()) <= 0))
                .filter(order -> order.getStudent().getUser_id().equals(student.getUser_id()))
                .collect(Collectors.toList());

        modelAndView.addObject("orders", pastOrders);
        modelAndView.setViewName("view-all-history-orders-page-student");
        return modelAndView;
    }

}
