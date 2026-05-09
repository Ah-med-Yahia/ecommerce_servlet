package com.example.demo.service;


import com.example.demo.DAO.CourseDao;
import com.example.demo.DAO.DBContextApplication;
import com.example.demo.models.Course;

import java.sql.*;
        import java.util.ArrayList;
import java.util.List;

public class CourseDAOImpl implements CourseDao{

    @Override
    public List<Course> findAll() {
        List<Course> list = new ArrayList<>();
        try {
            Connection conn = DBContextApplication.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM course");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Course(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("hours")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Course findById(int id) {
        try {
            Connection conn = DBContextApplication.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM course WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Course(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("hours")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(Course c) {
        try {
            Connection conn = DBContextApplication.getConnection();

            if (c.getId() == 0) {
                
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO course (name, hours) VALUES (?, ?)"
                );
                ps.setString(1, c.getName());
                ps.setInt(2, c.getHours());
                ps.executeUpdate();
            } else {
                // UPDATE
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE course SET name=?, hours=? WHERE id=?"
                );
                ps.setString(1, c.getName());
                ps.setInt(2, c.getHours());
                ps.setInt(3, c.getId());
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(int id) {
        try {
            Connection conn = DBContextApplication.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM course WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
