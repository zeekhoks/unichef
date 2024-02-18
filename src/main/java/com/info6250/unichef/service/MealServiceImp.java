package com.info6250.unichef.service;

import com.info6250.unichef.dao.MealDAO;
import com.info6250.unichef.model.Meal;
import com.info6250.unichef.model.Plan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MealServiceImp implements MealService{

    @Autowired
    MealDAO mealDAO;

    @Override
    public void createMeal(Meal meal) {
        mealDAO.save(meal);
    }

}