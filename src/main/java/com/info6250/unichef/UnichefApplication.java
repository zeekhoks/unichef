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

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class UnichefApplication {

    public static void main(String[] args) {

        SpringApplication.run(UnichefApplication.class, args);


    }

    @Bean
    public CommandLineRunner demoData(UserService userService) {
        return args -> {

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//            User student1 = new Student(UserRole.STUDENT, "Timothee", "Chalamet", "231 Park Drive",  "02215", "Boston", "Massachussets", "timothee.chalamet@example.com", "1234567891", bCryptPasswordEncoder.encode("abc123"), new ArrayList<FoodPreferences>(List.of(FoodPreferences.NONVEGETARIAN, FoodPreferences.EGGITARIAN)), "Nuts");
//            userService.addUser(student1);
//            User chef1 = new Chef(UserRole.CHEF, "Tommy", "Hillfiger", "231 Park Drive",  "02215", "Boston", "Massachussets", "tommy.hilfiger@example.com", "1234567892", bCryptPasswordEncoder.encode("abc123"), "Thai, Indo-Chinese, Pan-Asian");
//            userService.addUser(chef1);

        };
    }

}
