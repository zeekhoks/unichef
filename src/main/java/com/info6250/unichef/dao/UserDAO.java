package com.info6250.unichef.dao;

import com.info6250.unichef.model.User;

public interface UserDAO {

    public void save(User user);

    User findUserByEmail(String email);


}
