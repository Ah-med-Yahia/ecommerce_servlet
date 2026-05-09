package org.example.ecommerce.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppConfigContext implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();

        ctx.setAttribute("dbHost",   DBCApplication.HOST);
        ctx.setAttribute("dbName",   DBCApplication.DB_NAME);
        ctx.setAttribute("dbPort",   DBCApplication.PORT);
        ctx.setAttribute("dbUser",   DBCApplication.USERNAME);
        ctx.setAttribute("dbPass",   DBCApplication.PASSWORD);

        ctx.setAttribute("objectMapper", new ObjectMapper());

        System.out.println("App initialized — DB config & ObjectMapper ready.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("App shutting down.");
    }
}
