package com.info6250.unichef.controller;

import com.info6250.unichef.model.*;
import com.info6250.unichef.service.MealService;
import com.info6250.unichef.service.PlanService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@RequestMapping(value = "/student/order/reservation")
public class ReservationOrderController {

    @Autowired
    PlanService planService;

    @Autowired
    MealService mealService;

    @GetMapping("/view/all")
    public ModelAndView viewAllReservationOrders(HttpServletRequest request,
                                                 @RequestParam(required = false) String query,
                                                 @RequestParam(required = false) String filter) {

        if (filter == null) {
            filter = "ALLERGENS";
        }

        if (query == null) {
            query = "";
        }


        ModelAndView modelAndView = new ModelAndView();
        List<Plan> allPlans = planService.getAllPlansByType(PlanType.RESERVATION);
        Set<Plan> selectedPlans = (Set<Plan>) request.getSession().getAttribute("orderItems");

        if (query.isEmpty()) {
            modelAndView.addObject("allPlans", allPlans);
            modelAndView.addObject("selectedPlans", selectedPlans);
        } else {
            List<Plan> planList = new ArrayList<>();
            query = query.trim();

            switch (filter) {
                case "MEALNAME":
                    System.out.println("In mealname switch >>>>>>>>>>");
                    final String tempQuery = query;
                    planList = allPlans.stream().filter(plan -> {
                        ReservationPlan temp = (ReservationPlan) plan;
                        return temp.getMeals().stream().anyMatch(meal -> meal.getMealName().toLowerCase().contains(tempQuery.toLowerCase()));
                    }).toList();
                    planList.forEach(plan -> System.out.println(plan));
                    break;
                case "ALLERGENS":
                    System.out.println("In allergen switch >>>>>>>>>>");
                    final String temp1Query = query;
                    planList = allPlans.stream().filter(plan -> {
                        ReservationPlan temp = (ReservationPlan) plan;
                        return temp.getMeals().stream().anyMatch(meal -> !meal.getAllergens().toLowerCase().contains(temp1Query.toLowerCase()));
                    }).toList();
                    planList.forEach(plan -> System.out.println(plan));
                    break;
            }
            modelAndView.addObject("allPlans", planList);
        }

        modelAndView.addObject("filter", filter);
        modelAndView.addObject("query", query);
        modelAndView.addObject("selectedPlans", selectedPlans);
        modelAndView.setViewName("view-all-reservation-plans-student");
        return modelAndView;

    }

    @GetMapping("/view/{planID}")
    public ModelAndView getSingleReservationPlan(@PathVariable("planID") Long planID) {

        ModelAndView modelAndView = new ModelAndView();
        ReservationPlan reservationPlan = (ReservationPlan) planService.getPlanFromId(planID);
        modelAndView.setViewName("view-single-reservation-plan-student");
        modelAndView.addObject("reservationPlan", reservationPlan);
        return modelAndView;
    }

    @GetMapping("/plan/book/{planID}")
    public ModelAndView addToOrder(@PathVariable("planID") Long planID, HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();

        ReservationPlan reservationPlan = (ReservationPlan) planService.getPlanFromId(planID);

        Set<Plan> selectedPlans = (Set<Plan>) request.getSession().getAttribute("orderItems");
        if (selectedPlans == null) {
            selectedPlans = new HashSet<>();
        }
        reservationPlan.setAvailableSlots(reservationPlan.getAvailableSlots()-1);
        selectedPlans.add(reservationPlan);

        modelAndView.setViewName("view-reservation-orders-page");
        request.getSession().setAttribute("orderItems", selectedPlans);
        return modelAndView;
    }

    @GetMapping("/checkout")
    public String checkoutView (){
        return "view-reservation-orders-page";
    }

    @GetMapping("/delete/{planID}")
    public ModelAndView removeReservationFromOrder(@PathVariable("planID") Long planID, HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        ReservationPlan reservationPlan = (ReservationPlan) planService.getPlanFromId(planID);
        Set<Plan> selectedPlans = (Set<Plan>) request.getSession().getAttribute("orderItems");
        if(selectedPlans != null && selectedPlans.contains(reservationPlan)){
            selectedPlans.remove(reservationPlan);
        }
        reservationPlan.setAvailableSlots(reservationPlan.getAvailableSlots()+1);
        modelAndView.setViewName("redirect:/student/order/reservation/view/all");
        request.getSession().setAttribute("orderItems", selectedPlans);
        return modelAndView;
    }


}