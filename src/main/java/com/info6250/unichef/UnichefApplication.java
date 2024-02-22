package com.info6250.unichef;

import com.info6250.unichef.dao.UserDAO;
import com.info6250.unichef.dao.UserDAOImp;
import com.info6250.unichef.model.*;
import com.info6250.unichef.service.UserService;
import com.info6250.unichef.service.UserServiceImp;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class})
public class UnichefApplication {

    public static void main(String[] args) {

        SpringApplication.run(UnichefApplication.class, args);
    }

}
