package org.example.ecommerce.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

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


        String redisHost = context.getInitParameter("redisHost");
        int    redisPort = Integer.parseInt(context.getInitParameter("redisPort"));
        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), redisHost, redisPort);
        context.setAttribute("jedisPool", jedisPool);

        context.setAttribute("objectMapper", new ObjectMapper());

        System.out.println("App initialized ? DB config & ObjectMapper & Redis ready.");


    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JedisPool pool = (JedisPool) sce.getServletContext().getAttribute("jedisPool");
        if (pool != null) pool.close();

        System.out.println("App shutting down.");
    }
}
