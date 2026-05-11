package org.example.ecommerce.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {

    private static ObjectMapper getMapper(HttpServletRequest request) {

        ServletContext context = request.getServletContext();

        return (ObjectMapper) context.getAttribute("objectMapper");
    }

    public static void sendJson(
            HttpServletRequest request,
            HttpServletResponse response,
            int status,
            Object data
    ) throws IOException {

        ObjectMapper mapper = getMapper(request);

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        mapper.writeValue(response.getOutputStream(), data);
    }

    public static void sendSuccess( HttpServletRequest request,HttpServletResponse response, String message)
            throws IOException {

        Map<String, String> result = new HashMap<>();
        result.put("message", message);

        sendJson(request,response, 200, result);
    }

    public static void sendError(
            HttpServletRequest request,
            HttpServletResponse response,
            int status,
            String message
    ) throws IOException {

        Map<String, String> error = new HashMap<>();
        error.put("error", message);

        sendJson(request, response, status, error);
    }
}