package org.example.ecommerce.models;

import java.sql.Timestamp;

public class Auth {
    private int id;
    private int userId;
    private String password;
    private String role;
    private boolean isActive;
    private Timestamp createdAt;

    public Auth() {}

    public Auth(int id, int userId, String password, String role, boolean isActive, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public boolean isActive() {
        return isActive;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
