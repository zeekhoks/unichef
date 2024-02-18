package com.info6250.unichef.dao;

import com.info6250.unichef.model.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderDAOImp implements OrderDAO{

    @Autowired
    private SessionFactory sessionFactory;

    private Session session;

    public OrderDAOImp() {
    }

    private Session getSession() {
        if(session == null){
            session = sessionFactory.openSession();
        }
        return session;
    }

    @Override
    public void save (Order order){
        Session session = getSession();
        try{
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.persist(order);
            transaction.commit();

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public List<Order> getAllOrders(){
        Session session = getSession();
        try{
            List orderList = session.createQuery("FROM Order ").stream().toList();
            return orderList;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
