package org.example.ecommerce.Service.Impl;
import org.example.ecommerce.Models.Role;
import org.example.ecommerce.Models.User;
import org.example.ecommerce.Service.Interfaces.AuthService;
import org.example.ecommerce.DAO.DaoInterfaces.AuthDao;
import org.example.ecommerce.DAO.DaoImpl.AuthDaoImpl;
import org.example.ecommerce.DAO.DaoInterfaces.UserDAO;
import org.example.ecommerce.DAO.DaoImpl.UserDAOImpl;
import org.example.ecommerce.Utils.JwtUtil;
import org.example.ecommerce.Utils.PasswordUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class AuthServiceImpl implements AuthService {

    private final UserDAO userRepository = new UserDAOImpl();
    private final AuthDao authTokenRepository = new AuthDaoImpl();

    public User register(User user) {
        User existing = userRepository.findByEmail(user.getEmail());
        if (existing != null) return null;

        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        if (user.getRole() == null) user.setRole(Role.USER);

        User saved = userRepository.save(user);
        if (saved != null) saved.setPassword(null);
        return saved;
    }

    public Map<String, Object> login(String email, String password) {

        User user = userRepository.findByEmail(email);
        if (user == null) return null;

        if (!PasswordUtil.checkPassword(password, user.getPassword()))
            return null;

        String token = JwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name(),
                user.getId()
        );



        user.setPassword(null);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", user.getRole());
        response.put("email", user.getEmail());
        response.put("userId", user.getId());

        return response;
    }

    public boolean logout(String token) {
        String tokenHash = hashToken(token);
        LocalDateTime expiresAt = JwtUtil.extractExpiry(token);
        return authTokenRepository.blacklistToken(tokenHash, expiresAt);
    }



    private String hashToken(String token) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }
}