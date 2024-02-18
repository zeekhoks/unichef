package com.info6250.unichef.controller;

import com.info6250.unichef.model.*;
import com.info6250.unichef.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@Validated
public class AuthController {

    @Autowired
    UserService userService;

    @GetMapping("/index")
    public String home() {
        return "index";
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    @GetMapping("/login")
    public String loginForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");

        if (user != null) {
            if (user.getRole().equals(UserRole.CHEF)) {
                response.sendRedirect("/chef/dashboard");
            } else if (user.getRole().equals(UserRole.STUDENT)) {
                response.sendRedirect("/student/dashboard");
            } else {
                return "login";
            }
        }

        return "login";
    }

    @PostMapping("/signup")
    public ModelAndView signupUser(

            @RequestParam @NotNull(message = "Role cannot be null!") String role,
            @RequestParam @Size(min = 3, message = "First Name should have size grater than 3!") @NotBlank(message = "Name cannot be null!") String firstName,
            @RequestParam @Size(min = 3, message = "Last Name should have size grater than 3!") @NotBlank(message = "Name cannot be null!") String lastName,
            @RequestParam @NotBlank(message = "Address cannot be empty!") String address,
            @RequestParam @NotBlank(message = "Zipcode cannot be empty!") String zipcode,
            @RequestParam @NotBlank(message = "City cannot be empty!") String city,
            @RequestParam @NotBlank(message = "State cannot be empty!") String state,
            @RequestParam @Email(message = "Email is not valid!") String email,
            @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Invalid phone number!") String phoneNumber,
            @RequestParam @Size(min = 4, message = "Field should have size grater than 4!") String password,
            @RequestParam(required = false) String[] foodPreferences,
            @RequestParam(required = false) String allergies,
            @RequestParam(required = false) String cuisines

    ) throws IOException {

        User presentUser = userService.getUserByEmail(email);

        ModelAndView modelAndView = new ModelAndView();

        if (presentUser != null) {
            modelAndView.setViewName("redirect:/signup");
            return modelAndView;
        }

        UserRole userRole = UserRole.valueOf(role);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        if (role.equals("STUDENT")) {
            for (String foodPreference : foodPreferences) {
                System.out.println(foodPreference);
            }
            List<FoodPreferences> foodPreferencesList = Arrays.stream(foodPreferences)
                    .map(FoodPreferences::valueOf)
                    .toList();


            Student student = new Student(userRole, firstName, lastName, address, zipcode, city,
                    state, email, phoneNumber, bCryptPasswordEncoder.encode(password), foodPreferencesList, allergies);
            userService.addUser(student);
            System.out.println("Redirecting to login after adding student");
            return new ModelAndView("redirect:/login");
        } else if (role.equals("CHEF")) {
            Chef chef = new Chef(userRole, firstName, lastName, address, zipcode, city,
                    state, email, phoneNumber, bCryptPasswordEncoder.encode(password), cuisines);
            userService.addUser(chef);
            System.out.println("Redirecting to login after adding chef");
            return new ModelAndView("redirect:/login");
        } else {
            System.out.println("Redirecting to signup as I'm in else");
            modelAndView.setViewName("signup");
            return modelAndView;
        }


    }

    @PostMapping("/login")
    public ModelAndView loginUser(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletRequest request
    ) {

        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getUserByEmail(email);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        if (user == null) {
//            Email is wrong
            modelAndView.setViewName("redirect:/login");
        } else {
            if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
                if (user.getRole().equals(UserRole.CHEF)) {
                    modelAndView.setViewName("redirect:/chef/dashboard");
                    request.getSession().setAttribute("user", user);
                } else if (user.getRole().equals(UserRole.STUDENT)) {
                    modelAndView.setViewName("redirect:/student/dashboard");
                    request.getSession().setAttribute("user", user);
                } else {
//                    Password is wrong
                    modelAndView.setViewName("redirect:/login");
                }
            }
        }

        return modelAndView;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ModelAndView constraintViolationException(ConstraintViolationException ex) {
        Set<String> errors = new HashSet<>();
        if (ex.getConstraintViolations() == null) {
            errors.add(ex.getMessage());
        } else {
            errors = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
        }
        return new ModelAndView("errors", "errors", errors);
    }

}
