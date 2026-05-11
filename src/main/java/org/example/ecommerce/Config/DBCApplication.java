package org.example.ecommerce.Config;

import jakarta.servlet.ServletContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBCApplication {


    public static Connection getConnection() {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            ServletContext ctx = AppConfigContext.getContext();

            String url  = (String) ctx.getAttribute("dbUrl");
            String user = (String) ctx.getAttribute("dbUser");
            String pass = (String) ctx.getAttribute("dbPass");

            if (url == null) {
                throw new IllegalStateException("DB config not initialized in ServletContext");
            }

            return DriverManager.getConnection(url, user, pass);

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver not found");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database connection failed" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}