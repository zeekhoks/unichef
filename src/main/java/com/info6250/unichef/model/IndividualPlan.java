package com.info6250.unichef.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "individuals")
public class IndividualPlan extends Plan{

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "meal_id")
    private Meal meal;

    @Column(name="quantity")
    private int quantity;

    public IndividualPlan(){

    }

    public IndividualPlan(int quantity) {
        this.quantity = quantity;
    }

    public IndividualPlan(PlanType planTypes, FoodPreferences foodPreferences,
                          LocalDateTime deliveryDate, int quantity) {
        super(planTypes, foodPreferences, deliveryDate);
        this.quantity = quantity;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
