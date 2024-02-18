package com.info6250.unichef.dao;

import com.info6250.unichef.model.Chef;
import com.info6250.unichef.model.Plan;
import com.info6250.unichef.model.PlanType;
import com.info6250.unichef.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class PlanDAOImp implements PlanDAO {

    @Autowired
    private SessionFactory sessionFactory;

    private Session session;

    public PlanDAOImp() {
    }


    private Session getSession() {
        if (session == null) {
            session = sessionFactory.openSession();
        }
        return session;
    }

    @Override
    public void save(Plan plan) {

        Session session = getSession();

        try {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.persist(plan);
            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Plan getPlanById(Long planID) {

        Session session = getSession();
        try {
            String idQuery = "FROM Plan WHERE planID = :planID";
            Query<Plan> query = session.createQuery(idQuery, Plan.class);
            query.setParameter("planID", planID);
            return query.uniqueResult();

        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(Plan plan) {
        Session session = getSession();

        try {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.merge(plan);
            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Plan> getAllPlans(PlanType planType, Chef chef) {
        Session session = getSession();
        try {
            Query<Plan> query = session.createQuery("FROM Plan WHERE planType = :planType AND chef = :chef", Plan.class);
            query.setParameter("planType", planType);
            query.setParameter("chef", chef);

            return query.list();

        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<Plan> getAllPlansByType(PlanType planType) {
        Session session = getSession();
        try {
            Query<Plan> query = session.createQuery("FROM Plan WHERE planType = :planType", Plan.class);
            query.setParameter("planType", planType);

            return query.list();

        } catch (HibernateException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void deletePlanById (Long planID) {
        Session session = getSession();
        try{
            String idQuery = "FROM Plan WHERE planID = :planID";
            Query<Plan> query = session.createQuery(idQuery, Plan.class);
            query.setParameter("planID", planID);

            Plan planToDelete = query.uniqueResult();

            if(planToDelete != null){
                Transaction transaction = session.getTransaction();
                transaction.begin();
                session.remove(planToDelete);
                transaction.commit();
            }

        } catch (HibernateException e){
            e.printStackTrace();
        }
    }

}
