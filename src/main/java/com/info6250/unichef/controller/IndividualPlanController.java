package com.info6250.unichef.controller;

import com.info6250.unichef.model.*;
import com.info6250.unichef.service.MealService;
import com.info6250.unichef.service.PlanService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@Validated
@RequestMapping(value = "/chef/plan/individual")
public class IndividualPlanController {
    @Autowired
    PlanService planService;

    @Autowired
    MealService mealService;

    @GetMapping("")
    public String individualPlanForm() {
        return "create-individual-plan";
    }

    @PostMapping("/add")
    public ModelAndView createPlan(
            @RequestParam @NotNull(message = "Plan Type cannot be null!") String planType,
            @RequestParam @NotNull(message = "Food preferences cannot be null!") String foodPreferences,
            @RequestParam String deliveryDate,
            @RequestParam @Min(value = 1, message = "Quantity cannot be less than 1!") @NotNull(message = "Quantity cannot be null!") int quantity,
            @RequestParam @NotBlank(message = "Meal Name cannot be empty!") String mealName,
            @RequestParam @NotBlank(message = "Description cannot be empty!") String description,
            @RequestParam @NotBlank(message = "Price cannot be empty!") String price,
            @RequestParam @NotBlank(message = "Allergens cannot be empty!") String allergens,
            HttpServletRequest request
    ) {
        Chef chef = (Chef) request.getSession().getAttribute("user");

        double mealPrice = Double.parseDouble(price);

        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime selectedDayForDelivery = LocalDateTime.parse(deliveryDate, formatter);

        long hoursDifference = ChronoUnit.HOURS.between(today, selectedDayForDelivery);

        if (hoursDifference <= 5) {
            ModelAndView modelAndView = new ModelAndView("create-individual-plan");
            List<String> errors = new ArrayList<>();
            errors.add("Delivery time should be greater than 5 hours from now!");
            modelAndView.addObject("errors", errors);
            return modelAndView;
        } else {
            PlanType planTypeEnum = PlanType.valueOf(planType);
            FoodPreferences foodPreference = FoodPreferences.valueOf(foodPreferences);
            IndividualPlan individualPlan = new IndividualPlan(planTypeEnum, foodPreference,
                    selectedDayForDelivery, quantity);
            Meal meal = new Meal(mealName, description, mealPrice, allergens);

            individualPlan.setMeal(meal);
            meal.setIndividualPlan(individualPlan);
            individualPlan.setChef(chef);
            planService.createPlan(individualPlan);

            System.out.println("Plan ID >>>>>>" + individualPlan.getPlanID());

            ModelAndView modelAndView = new ModelAndView("individual-plan-success");
            modelAndView.addObject("individualPlan", individualPlan);

            return modelAndView;
        }
    }


    @GetMapping("/view/all")
    public ModelAndView viewAllIndividualPlans(HttpServletRequest request,
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
        List<Plan> allPlans = planService.getAllPlans(PlanType.INDIVIDUAL, chef);

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
                        IndividualPlan temp = (IndividualPlan) plan;
                        return temp.getMeal().getMealName().toLowerCase().contains(tempQuery.toLowerCase());
                    }).toList();
                    planList.forEach(plan -> System.out.println(plan));
                    break;
                case "ALLERGENS":
                    System.out.println("In allergen switch >>>>>>>>>>");
                    final String temp1Query = query;
                    planList = allPlans.stream().filter(plan -> {
                        IndividualPlan temp = (IndividualPlan) plan;
                        return !temp.getMeal().getAllergens().toLowerCase().contains(temp1Query.toLowerCase());
                    }).toList();
                    planList.forEach(plan -> System.out.println(plan));
                    break;
            }
            modelAndView.addObject("allPlans", planList);
        }
        modelAndView.addObject("filter", filter);
        modelAndView.addObject("query", query);
        modelAndView.setViewName("view-all-individual-plans-chef");
        return modelAndView;

    }

    @GetMapping("/view/{planID}")
    public ModelAndView getSingleIndividualPlan(@PathVariable("planID") Long planID) {

        ModelAndView modelAndView = new ModelAndView();
        IndividualPlan planById = (IndividualPlan) planService.getPlanFromId(planID);
        modelAndView.setViewName("view-single-individual-plan-chef");
        modelAndView.addObject("planById", planById);
        return modelAndView;

    }

    @GetMapping("/delete/{planID}")
    public ModelAndView deletePlan(HttpServletResponse response, @PathVariable("planID") Long planID) throws IOException {
        IndividualPlan individualPlan = (IndividualPlan) planService.getPlanFromId(planID);

        LocalDateTime deliveryDate = individualPlan.getDeliveryDate();
        LocalDateTime today = LocalDateTime.now();

        long hoursDifference = ChronoUnit.HOURS.between(today, deliveryDate);
        if (hoursDifference <= 5) {
            ModelAndView modelAndView = new ModelAndView("view-all-individual-plans-chef");
            List<String> errors = new ArrayList<>();
            errors.add("Cannot delete a plan with delivery time less than 5 hours");
            modelAndView.addObject("errors", errors);
            return modelAndView;

        } else {
            planService.deletePlanById(planID);
            response.sendRedirect("/chef/plan/individual/view/all");
            return new ModelAndView("view-all-individual-plans-chef");
        }

    }


    @GetMapping("/update/{planID}")
    public ModelAndView updatePlan(@PathVariable("planID") Long planID) {

        ModelAndView modelAndView = new ModelAndView();
        IndividualPlan individualPlan = (IndividualPlan) planService.getPlanFromId(planID);
        modelAndView.addObject("individualPlan", individualPlan);
        modelAndView.setViewName("view-update-individual-plan-chef");
        return modelAndView;

    }

    @PostMapping("/update/{planID}")
    public ModelAndView updateIndividualPlan(
            @RequestParam @NotNull(message = "Plan Type cannot be null!") String planType,
            @RequestParam @NotNull(message = "Food preferences cannot be null!") String foodPreferences,
            @RequestParam String deliveryDate,
            @RequestParam @Min(value = 1, message = "Quantity cannot be less than 1!") @NotNull(message = "Quantity cannot be null!") int quantity,
            @RequestParam @NotBlank(message = "Meal Name cannot be empty!") String mealName,
            @RequestParam @NotBlank(message = "Description cannot be empty!") String description,
            @RequestParam @NotBlank(message = "Price cannot be empty!") String price,
            @RequestParam @NotBlank(message = "Allergens cannot be empty!") String allergens,
            @PathVariable("planID") Long planID) {

        ModelAndView modelAndView = new ModelAndView();
        IndividualPlan individualPlan = (IndividualPlan) planService.getPlanFromId(planID);

        PlanType planTypeEnum = PlanType.valueOf(planType);
        FoodPreferences foodPreference = FoodPreferences.valueOf(foodPreferences);
        double mealPrice = Double.parseDouble(price);

        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime selectedDayForDelivery = LocalDateTime.parse(deliveryDate, formatter);

        System.out.println("Getting the meal before update >>>>>>>" + individualPlan.getMeal().getMealID());

        long daysDifference = ChronoUnit.HOURS.between(today, selectedDayForDelivery);

        if (daysDifference <= 5) {
            modelAndView.setViewName("view-update-individual-plan-chef");
            List<String> errors = new ArrayList<>();
            errors.add("Delivery time should be greater than 5 hours from now!");
            modelAndView.addObject("errors", errors);
            return modelAndView;
        } else {
            individualPlan.setPlanID(planID);
            individualPlan.setPlanType(planTypeEnum);
            individualPlan.setFoodPreferences(foodPreference);
            individualPlan.setDeliveryDate(selectedDayForDelivery);
            individualPlan.setQuantity(quantity);
            individualPlan.getMeal().setMealName(mealName);
            individualPlan.getMeal().setDescription(description);
            individualPlan.getMeal().setPrice(mealPrice);
            individualPlan.getMeal().setAllergens(allergens);

            planService.updatePlan(individualPlan);

            modelAndView.setViewName("individual-plan-success");
            modelAndView.addObject("individualPlan", individualPlan);

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
