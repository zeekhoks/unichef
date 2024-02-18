package com.info6250.unichef.service;

import com.info6250.unichef.model.Order;
import com.info6250.unichef.model.Student;

import java.util.List;
import java.util.Locale;

public interface OrderService {
    public void createOrder (Order order, Student student, Locale locale);

    public List<Order> getAllOrders ();
}
