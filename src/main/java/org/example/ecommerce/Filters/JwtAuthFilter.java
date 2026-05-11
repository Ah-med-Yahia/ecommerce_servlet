package org.example.ecommerce.Filters;

import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.ecommerce.DAO.DaoImpl.AuthDaoImpl;
import org.example.ecommerce.DAO.DaoInterfaces.AuthDao;
import org.example.ecommerce.Utils.JwtUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


@WebFilter("/*")
public class JwtAuthFilter implements Filter {

    private final AuthDao authTokenRepository = new AuthDaoImpl();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req = (HttpServletRequest)  request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();


        if (path.contains("/auth/login") || path.contains("/auth/register")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            res.setStatus(401);
            res.getWriter().write("Missing token");
            return;
        }

        String token = authHeader.substring(7);

        if (!JwtUtil.validateToken(token)) {
            res.setStatus(401);
            res.getWriter().write("Invalid or expired token");
            return;
        }



        if (authTokenRepository.isBlacklisted(hashToken(token))) {
            res.setStatus(401);
            res.getWriter().write("Token in black list. Please login again.");
            return;
        }

        Claims claims = JwtUtil.extractClaims(token);

        req.setAttribute("email",  claims.getSubject());
        req.setAttribute("role",   claims.get("role",   String.class));
        req.setAttribute("userId", claims.get("userId", Integer.class));

        chain.doFilter(request, response);
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