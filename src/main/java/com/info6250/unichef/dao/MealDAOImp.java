package com.info6250.unichef.dao;

import com.info6250.unichef.model.Chef;
import com.info6250.unichef.model.Meal;
import com.info6250.unichef.model.Plan;
import com.info6250.unichef.model.PlanType;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MealDAOImp implements MealDAO {

    @Autowired
    private SessionFactory sessionFactory;

    private Session session;

    public MealDAOImp(){

    }

    private Session getSession() {
        if(session == null){
            session = sessionFactory.openSession();
        }
        return  session;
    }


    @Override
    public void save(Meal meal){

        Session session = getSession();
        try{
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.persist(meal);
            transaction.commit();

        } catch (Exception e){
            e.printStackTrace();
        }

    }



}