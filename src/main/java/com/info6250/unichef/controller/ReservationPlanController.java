package com.info6250.unichef.controller;


import com.info6250.unichef.model.*;
import com.info6250.unichef.service.EmailService;
import com.info6250.unichef.service.MealService;
import com.info6250.unichef.service.PlanService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Validated
@RequestMapping(value = "/chef/plan/reservation")
public class ReservationPlanController {

    @Autowired
    PlanService planService;

    @Autowired
    MealService mealService;

    @GetMapping("")
    public String reservationPlanForm() {
        return "create-reservation-plan";
    }

    @PostMapping("/add")
    public ModelAndView createReservationPlan(
            @RequestParam @NotNull(message = "Plan Type cannot be null!") String planType,
            @RequestParam @NotNull(message = "Food preferences cannot be null!") String foodPreferences,
            @RequestParam String deliveryDate,
            @RequestParam @Min(value = 1, message = "Slots cannot be less than 1!") @Max(value = 10, message = "Slots cannot be more than 10!") @NotNull(message = "Slots cannot be null!") int maxSlots,
            @RequestParam @Min(value = 1, message = "Days cannot be less than 1!") @Max(value = 5, message = "Days cannot be more than 5!") @NotNull(message = "Days cannot be null!") int numberOfDays,
            @RequestParam String totalPrice,
            HttpServletRequest request
    ) {
        Chef chef = (Chef) request.getSession().getAttribute("user");

        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime selectedDayForDelivery = LocalDateTime.parse(deliveryDate, formatter);

        long daysDifference = ChronoUnit.DAYS.between(today, selectedDayForDelivery);

        if (daysDifference <= 1) {
            ModelAndView modelAndView = new ModelAndView("create-reservation-plan");
            List<String> errors = new ArrayList<>();
            System.out.println("Today: " + today);
            System.out.println("Selected date: " + selectedDayForDelivery);
            System.out.println("Days difference: " + daysDifference);
            errors.add("Delivery time should be greater than 1 day from now!");
            modelAndView.addObject("errors", errors);
            return modelAndView;
        } else {

            PlanType planTypeEnum = PlanType.valueOf(planType);
            FoodPreferences foodPreference = FoodPreferences.valueOf(foodPreferences);
            Float totalPlanPrice = Float.parseFloat(totalPrice);
            Set<Meal> meals = new HashSet<>();
            int availableSlots = maxSlots;

            for (int i = 1; i <= numberOfDays; i++) {
                String mealName = request.getParameter("mealName" + i);
                String description = request.getParameter("description" + i);
                String tempPrice = request.getParameter("price" + i);
                double price = Double.parseDouble(tempPrice);
                String allergens = request.getParameter("allergens" + i);

                Meal meal = new Meal(mealName, description, price, allergens);

                meals.add(meal);
            }

            ReservationPlan reservationPlan = new ReservationPlan(planTypeEnum, foodPreference, selectedDayForDelivery,
                    maxSlots, availableSlots, numberOfDays, totalPlanPrice);

            reservationPlan.setMeals(meals);
            reservationPlan.setChef(chef);
            planService.createPlan(reservationPlan);

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("view-reservation-plan-chef");
            modelAndView.addObject("reservationPlan", reservationPlan);
            return modelAndView;

        }

    }


    @GetMapping("/view/all")
    public ModelAndView viewAllReservationPlans(HttpServletRequest request,
                                                @RequestParam(required = false) String query,
                                                @RequestParam(required = false) String filter) {

        if (filter == null) {
            filter = "ALLERGENS";
        }

        if (query == null) {
            query = "";
        }

        Chef chef = (Chef) request.getSession().getAttribute("user");
        ModelAndView modelAndView = new ModelAndView();
        List<Plan> allPlans = planService.getAllPlans(PlanType.RESERVATION, chef);

        if (query.isEmpty()) {
            modelAndView.addObject("allPlans", allPlans);
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
        modelAndView.setViewName("view-all-reservation-plans-chef");
        return modelAndView;

    }

    @GetMapping("/view/{planID}")
    public ModelAndView getSingleReservationPlan(@PathVariable("planID") Long planID) {

        ModelAndView modelAndView = new ModelAndView();
        ReservationPlan planById = (ReservationPlan) planService.getPlanFromId(planID);
        modelAndView.setViewName("view-single-reservation-plan-chef");
        modelAndView.addObject("planById", planById);
        return modelAndView;
    }

    @GetMapping("/delete/{planID}")
    public ModelAndView deleteReservationPlan(HttpServletResponse response, @PathVariable("planID") Long planID) throws IOException {
        ReservationPlan reservationPlan = (ReservationPlan) planService.getPlanFromId(planID);

        LocalDateTime deliveryDate = reservationPlan.getDeliveryDate();
        LocalDateTime today = LocalDateTime.now();

        long daysDifference = ChronoUnit.DAYS.between(today, deliveryDate);
        if (daysDifference <= 1) {
            ModelAndView modelAndView = new ModelAndView("view-all-reservation-plans-chef");
            List<String> errors = new ArrayList<>();
            errors.add("Cannot delete a plan whose delivery date is one day from now!");
            modelAndView.addObject("errors", errors);
            return modelAndView;
        } else {
            planService.deletePlanById(planID);
            response.sendRedirect("/chef/plan/reservation/view/all");
            return new ModelAndView("view-all-reservation-plans-chef");
        }

    }

    @GetMapping("/update/{planID}")
    public ModelAndView updatePlan(@PathVariable("planID") Long planID) {

        ModelAndView modelAndView = new ModelAndView();
        ReservationPlan reservationPlan = (ReservationPlan) planService.getPlanFromId(planID);
        modelAndView.addObject("reservationPlan", reservationPlan);
        modelAndView.setViewName("view-update-reservation-plan-chef");
        return modelAndView;

    }

    @PostMapping("/update/{planID}")
    public ModelAndView updateReservationPlan(
            @RequestParam @NotNull(message = "Plan Type cannot be null!") String planType,
            @RequestParam @NotNull(message = "Food preferences cannot be null!") String foodPreferences,
            @RequestParam String deliveryDate,
            @RequestParam @Min(value = 1, message = "Slots cannot be less than 1!")
            @NotNull(message = "Slots cannot be null!") @Max(value = 10, message = "Slots cannot be more than 10!") int maxSlots,
            @RequestParam @Min(value = 1, message = "Days cannot be less than 1!")
            @NotNull(message = "Days cannot be null!") @Max(value = 5, message = "Days cannot be more than 5!") int numberOfDays,
            HttpServletRequest request,
            @PathVariable("planID") Long planID) {

        ModelAndView modelAndView = new ModelAndView();
        ReservationPlan reservationPlan = (ReservationPlan) planService.getPlanFromId(planID);

        PlanType planTypeEnum = PlanType.valueOf(planType);
        FoodPreferences foodPreference = FoodPreferences.valueOf(foodPreferences);

        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime selectedDayForDelivery = LocalDateTime.parse(deliveryDate, formatter);


        long daysDifference = ChronoUnit.DAYS.between(today, selectedDayForDelivery);

        if (daysDifference <= 1) {
            modelAndView.setViewName("view-update-reservation-plan-chef");
            List<String> errors = new ArrayList<>();
            errors.add("Delivery time should be greater than 1 day from now!");
            modelAndView.addObject("errors", errors);
            return modelAndView;
        } else {
            reservationPlan.setPlanType(planTypeEnum);
            reservationPlan.setFoodPreferences(foodPreference);
            reservationPlan.setDeliveryDate(selectedDayForDelivery);
            reservationPlan.setNumberOfDays(numberOfDays);
            reservationPlan.setMaxSlots(maxSlots);
            reservationPlan.setAvailableSlots(maxSlots);

            Map<Long, Meal> mealMap = new HashMap<>();

            for (Meal meal : reservationPlan.getMeals()) {
                mealMap.put(meal.getMealID(), meal);
            }

            float totalPrice = 0.0f;

            for (Map.Entry<Long, Meal> entry : mealMap.entrySet()) {
                long mealId = entry.getKey();
                String mealName = request.getParameter("mealName" + mealId);
                String description = request.getParameter("description" + mealId);
                String tempPrice = request.getParameter("price" + mealId);
                double price = Double.parseDouble(tempPrice);
                String allergens = request.getParameter("allergens" + mealId);

                totalPrice += Float.parseFloat(tempPrice);

                entry.getValue().setMealName(mealName);
                entry.getValue().setDescription(description);
                entry.getValue().setPrice(price);
                entry.getValue().setAllergens(allergens);
            }

            reservationPlan.setTotalPrice(totalPrice);

            planService.updatePlan(reservationPlan);

            modelAndView.setViewName("reservation-plan-success");

            modelAndView.addObject("reservationPlan", reservationPlan);

            return modelAndView;

        }

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ModelAndView constraintViolationException(ConstraintViolationException ex) {
        Set<String> errors = new HashSet<>();
        if (ex.getConstraintViolations() == null) {
            errors.add(ex.getMessage());
        } else {
            errors = ex.getConstraintViolations().stream().map(v -> v.getMessage()).collect(Collectors.toSet());
        }
        return new ModelAndView("errors", "errors", errors);
    }


}
