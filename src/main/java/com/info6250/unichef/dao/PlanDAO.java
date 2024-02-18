package com.info6250.unichef.dao;

import com.info6250.unichef.model.Chef;
import com.info6250.unichef.model.Plan;
import com.info6250.unichef.model.PlanType;
import com.info6250.unichef.model.User;

import java.util.List;


public interface PlanDAO {

    public void save (Plan plan);

    public Plan getPlanById (Long planID);

    public void update (Plan plan);

    public List<Plan> getAllPlans (PlanType planType, Chef chef);

    public List<Plan> getAllPlansByType (PlanType planType);

    public void deletePlanById (Long planID);


}
