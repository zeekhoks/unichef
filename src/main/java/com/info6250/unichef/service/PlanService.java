package com.info6250.unichef.service;

import com.info6250.unichef.model.Chef;
import com.info6250.unichef.model.Plan;
import com.info6250.unichef.model.PlanType;
import com.info6250.unichef.model.User;

import java.util.List;

public interface PlanService {
    public void createPlan(Plan plan);

    public Plan getPlanFromId (Long planID);

    public void updatePlan (Plan plan);

    public List<Plan> getAllPlans(PlanType planType, Chef chef);

    public List<Plan> getAllPlansByType(PlanType planType);

    public void deletePlanById (Long planID);
}
