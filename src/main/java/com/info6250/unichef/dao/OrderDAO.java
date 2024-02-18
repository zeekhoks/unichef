package com.info6250.unichef.dao;


import com.info6250.unichef.model.Order;

import java.util.List;

public interface OrderDAO {
    public void save (Order order);

    public List<Order> getAllOrders ();
}
