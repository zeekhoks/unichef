package com.info6250.unichef.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "plans")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="db_plan_type",
        discriminatorType = DiscriminatorType.STRING)
public class Plan {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chef_id")
    private Chef chef;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "plans", cascade = CascadeType.ALL)
    private Set<Order> orders;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "plan_id")
    private Long planID;

    @Column(name = "plan_type")
    private PlanType planType;

    @Column(name="preference")
    @Enumerated(EnumType.STRING)
    private FoodPreferences foodPreferences;

    @Column(name="deliveryDate")
    private LocalDateTime deliveryDate;

    public Plan() {
    }

    public Plan(PlanType planType, FoodPreferences foodPreferences, LocalDateTime deliveryDate) {
        this.planType = planType;
        this.foodPreferences = foodPreferences;
        this.deliveryDate = deliveryDate;
    }

    public Long getPlanID() {
        return planID;
    }

    public void setPlanID(Long planID) {
        this.planID = planID;
    }



    public FoodPreferences getFoodPreferences() {
        return foodPreferences;
    }

    public void setFoodPreferences(FoodPreferences foodPreferences) {
        this.foodPreferences = foodPreferences;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }
    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Chef getChef() {
        return chef;
    }

    public void setChef(Chef chef) {
        this.chef = chef;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public PlanType getPlanType() {
        return planType;
    }

    public void setPlanType(PlanType planTypes) {
        this.planType = planTypes;
    }
}
