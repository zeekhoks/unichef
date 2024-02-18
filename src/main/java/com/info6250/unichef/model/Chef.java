package com.info6250.unichef.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Chef extends User{

    @OneToMany
    private Set<Plan> plans = new HashSet<>();

    @Column(name = "cuisines")
    private String cuisines;

    public Chef() {
    }

    public Chef(String cuisines) {
        this.cuisines = cuisines;
    }

    public Chef(UserRole role, String firstName, String lastName, String address, String zipcode, String city, String state,
                String email, String phoneNumber, String password, String cuisines) {
        super(role, firstName, lastName, address, zipcode, city, state, email, phoneNumber, password);
        this.cuisines = cuisines;
    }

    public String getIngredients() {
        return cuisines;
    }

    public void setIngredients(String ingredients) {
        this.cuisines = ingredients;
    }

    public Set<Plan> getPlans() {
        return plans;
    }

    public void setPlans(Set<Plan> plans) {
        this.plans = plans;
    }
}
