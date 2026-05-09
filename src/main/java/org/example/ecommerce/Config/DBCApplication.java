package org.example.ecommerce.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBCApplication {

    static final String HOST = "127.0.0.1";
    static final String DB_NAME = "ecommerce_system";
    static final int PORT = 3306;
    static final String USERNAME = "root";
    static final String PASSWORD = "";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = String.format(
                    "jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC",
                    HOST, PORT, DB_NAME
            );

            return DriverManager.getConnection(url, USERNAME, PASSWORD);

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver not found");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database connection failed");
            e.printStackTrace();
        }
        return null;
    }
}