package org.example.ecommerce.Service.Interfaces;

import org.example.ecommerce.Models.User;

import java.util.Map;

public interface AuthService {

    User register(User user);

    Map<String, Object> login(String email, String password);

    boolean logout(String token);

}