package net.javaguides.springboot.service;

import org.springframework.stereotype.Service;

@Service
public class LoginService {
    public boolean authenticateUser(String email, String password) {
        // Implement your authentication logic here
        // For example, check if the email and password match a user in the database
        // Return true if authenticated, false otherwise
        return false;
    }

    public String authenticateAndGetUserName(String email, String password) {
        return email;
    }
}