package com.info6250.unichef.service;

import com.info6250.unichef.model.User;

public interface UserService {

    public void addUser(User user);

    public User getUserByEmail(String email);
}
