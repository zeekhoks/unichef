package com.info6250.unichef.model;

import jakarta.persistence.*;

@Entity
@Table(name = "meals")
public class Meal {


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reservation_id")
    private ReservationPlan reservationPlan;

    @OneToOne(mappedBy = "meal", fetch = FetchType.EAGER)
    private IndividualPlan individualPlan;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mealID;

    @Column(name="meal-name")
    private String mealName;

    @Column(name="description")
    private String description;

    @Column(name="price")
    private double price;

    @Column(name="allergens")
    private String allergens;


    public Meal() {
    }

    public Meal(String mealName, String description, double price, String allergens) {
        this.mealName = mealName;
        this.description = description;
        this.price = price;
        this.allergens = allergens;
    }

    public Long getMealID() {
        return mealID;
    }

    public void setMealID(Long mealID) {
        this.mealID = mealID;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAllergens() {
        return allergens;
    }

    public void setAllergens(String allergens) {
        this.allergens = allergens;
    }

    public ReservationPlan getReservationPlan() {
        return reservationPlan;
    }

    public void setReservationPlan(ReservationPlan reservationPlan) {
        this.reservationPlan = reservationPlan;
    }

    public IndividualPlan getIndividualPlan() {
        return individualPlan;
    }

    public void setIndividualPlan(IndividualPlan individualPlan) {
        this.individualPlan = individualPlan;
    }
}
