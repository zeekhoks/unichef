package com.info6250.unichef.dao;

import com.info6250.unichef.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDAOImp implements UserDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(User user) {
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.persist(user);
            transaction.commit();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public User findUserByEmail(String email){
        try(Session session = sessionFactory.openSession()){
            String emailQuery = "FROM User WHERE email= :email";
            Query<User> query = session.createQuery(emailQuery, User.class);
            query.setParameter("email", email);
            return query.uniqueResult();

        } catch (HibernateException e){
            e.printStackTrace();
            return null;
        }
    }
}
