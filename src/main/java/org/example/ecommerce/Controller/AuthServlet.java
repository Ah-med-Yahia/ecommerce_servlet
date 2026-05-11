package org.example.ecommerce.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.ecommerce.Models.User;
import org.example.ecommerce.Service.Impl.AuthServiceImpl;
import org.example.ecommerce.Service.Interfaces.AuthService;
import org.example.ecommerce.Utils.ResponseUtil;

import java.io.IOException;
import java.util.Map;

@WebServlet("/auth/*")
public class AuthServlet extends HttpServlet {

    private AuthService authService ;

    private ObjectMapper mapper;

    @Override
    public void init() {
        ServletContext ctx = getServletContext();
        mapper = (ObjectMapper) ctx.getAttribute("objectMapper");
        authService = new AuthServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();

        switch (path) {

            case "/register":
                register(request, response);
                break;

            case "/login":
                login(request, response);
                break;

            case "/logout":
                logout(request, response);
                break;

            default:
                ResponseUtil.sendError(request, response,404, "Endpoint not found");
                break;
        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            User user = mapper.readValue(request.getInputStream(), User.class);

            if (user.getName() == null || user.getEmail() == null || user.getPassword() == null) {
                ResponseUtil.sendError(request,response, 400, "Name, email and password are required");
                return;
            }

            User saved = authService.register(user);
            if (saved == null) {
                ResponseUtil.sendError(request,response, 409, "Email already registered");
                return;
            }
            ResponseUtil.sendJson(request,response, 201, saved);

        } catch (Exception e) {
            ResponseUtil.sendError(request,response, 400, "Invalid request body");
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Map<String, String> body = mapper.readValue(request.getInputStream(), Map.class);

            Map<String, Object> result =
                    authService.login(body.get("email"), body.get("password"));

            if (result == null) {
                ResponseUtil.sendError(request,response, 401, "Invalid email or password");
                return;
            }

            ResponseUtil.sendJson(request,response, 200, result);

        } catch (Exception e) {
            ResponseUtil.sendError(request,response, 400, "Invalid request body");
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            ResponseUtil.sendError(request,response, 400, "Missing token");
//            return;
//        }

        String token = authHeader.substring(7);
        boolean revoked = authService.logout(token);

        if (revoked) ResponseUtil.sendSuccess(request,response, "Logged out successfully");
        else         ResponseUtil.sendError(request,response, 400, "Logout failed or token not found");
    }
}