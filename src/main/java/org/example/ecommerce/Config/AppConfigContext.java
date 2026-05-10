package org.example.ecommerce.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppConfigContext implements ServletContextListener {

    private static ServletContext context;

    public static ServletContext getContext() {
        return context;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        context = sce.getServletContext();

        String host   = context.getInitParameter("dbHost");
        String dbName = context.getInitParameter("dbName");
        String port   = context.getInitParameter("dbPort");
        String user   = context.getInitParameter("dbUser");
        String pass   = context.getInitParameter("dbPass");

        String url = String.format(
                "jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=UTC",
                host, port, dbName
        );

        context.setAttribute("dbUrl",  url);
        context.setAttribute("dbUser", user);
        context.setAttribute("dbPass", pass);

        context.setAttribute("objectMapper", new ObjectMapper());

        System.out.println("App initialized — DB config & ObjectMapper ready.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("App shutting down.");
    }
}
