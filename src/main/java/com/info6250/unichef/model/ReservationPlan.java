package com.info6250.unichef.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reservations")

public class ReservationPlan extends Plan{

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Meal> meals = new HashSet<>();

    @Column(name = "max_slots")
    private int maxSlots;

    @Column(name = "available-slots")
    private int availableSlots;

    @Column(name = "number-days")
    private int numberOfDays;

    @Column(name = "total_price")
    private Float totalPrice;

    public ReservationPlan() {

    }

    public ReservationPlan(PlanType planType, FoodPreferences foodPreferences, LocalDateTime deliveryDate,
                           int maxSlots, int availableSlots, int numberOfDays, Float totalPrice) {
        super(planType, foodPreferences, deliveryDate);
        this.maxSlots = maxSlots;
        this.availableSlots = availableSlots;
        this.numberOfDays = numberOfDays;
        this.totalPrice = totalPrice;
    }

    public ReservationPlan(int maxSlots, int availableSlots, Float totalPrice) {
        this.maxSlots = maxSlots;
        this.availableSlots = availableSlots;
        this.totalPrice = totalPrice;
    }

    public int getMaxSlots() {
        return maxSlots;
    }

    public void setMaxSlots(int maxSlots) {
        this.maxSlots = maxSlots;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Set<Meal> getMeals() {
        return meals;
    }

    public void setMeals(Set<Meal> meals) {
        this.meals = meals;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }
}
