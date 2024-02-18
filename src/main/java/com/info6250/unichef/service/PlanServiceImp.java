package com.info6250.unichef.service;

import com.info6250.unichef.dao.PlanDAO;
import com.info6250.unichef.model.Chef;
import com.info6250.unichef.model.Plan;
import com.info6250.unichef.model.PlanType;
import com.info6250.unichef.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanServiceImp implements PlanService{

    @Autowired
    PlanDAO planDAO;

    @Override
    public void createPlan(Plan plan) {
        planDAO.save(plan);
    }

    @Override
    public Plan getPlanFromId (Long planID){
        return planDAO.getPlanById(planID);
    }

    @Override
    public void updatePlan (Plan plan) { planDAO.update(plan); }

    @Override
    public List<Plan> getAllPlans(PlanType planType, Chef chef){return planDAO.getAllPlans(planType, chef); }

    @Override
    public List<Plan> getAllPlansByType(PlanType planType){return planDAO.getAllPlansByType(planType); }

    @Override
    public void deletePlanById (Long planID) { planDAO.deletePlanById(planID);}
}
