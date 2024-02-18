package com.info6250.unichef.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Student extends User{


    @OneToMany(fetch = FetchType.EAGER)
    private Set<Order> orders = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "student_food_preferences", joinColumns = @JoinColumn(name = "student_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "food_preferences")
    private List<FoodPreferences> foodPreferences;

    @Column(name="allergies")
    private String allergies;

    public Student() {
    }

    public Student(List<FoodPreferences> foodPreferences, String allergies) {
        this.foodPreferences = foodPreferences;
        this.allergies = allergies;
    }

    public Student(UserRole role, String firstName, String lastName, String address, String zipcode, String city,
                   String state, String email, String phoneNumber, String password, List<FoodPreferences> foodPreferences, String allergies) {
        super(role, firstName, lastName, address, zipcode, city, state, email, phoneNumber, password);
        this.foodPreferences = foodPreferences;
        this.allergies = allergies;
    }

    public List<FoodPreferences> getFoodPreferences() {
        return foodPreferences;
    }

    public void setFoodPreferences(List<FoodPreferences> foodPreferences) {
        this.foodPreferences = foodPreferences;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
}
