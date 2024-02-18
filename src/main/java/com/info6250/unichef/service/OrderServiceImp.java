package com.info6250.unichef.service;

import com.info6250.unichef.dao.MealDAO;
import com.info6250.unichef.dao.OrderDAO;
import com.info6250.unichef.model.Meal;
import com.info6250.unichef.model.Order;
import com.info6250.unichef.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class OrderServiceImp implements OrderService{

    @Autowired
    OrderDAO orderDAO;

    @Autowired
    EmailService emailService;

    @Override
    public void createOrder(Order order, Student student, Locale locale) {
        orderDAO.save(order);
        try {
            emailService.sendEmail(student.getEmail(), "Order Confirmation", order, locale);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public List<Order> getAllOrders (){ return orderDAO.getAllOrders(); }

}