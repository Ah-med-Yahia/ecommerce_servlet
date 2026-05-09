package com.example.demo.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContextApplication {

    private static final String HOST = "127.0.0.1";
    private static final String DB_NAME = "manageStudentAndCourse";
    private static final int PORT = 3306;
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

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
