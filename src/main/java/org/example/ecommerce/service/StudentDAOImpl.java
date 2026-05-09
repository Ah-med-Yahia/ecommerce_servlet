package com.example.demo.service;


import com.example.demo.DAO.DBContextApplication;
import com.example.demo.DAO.StudentDao;
import com.example.demo.models.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl implements StudentDao {

    @Override
    public List<Student> findAll() {
        List<Student> list = new ArrayList<>();
        try {
            Connection conn = DBContextApplication.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM student");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getDouble("GPA"),
                        rs.getDate("birth_date"),
                        rs.getInt("course_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Student findById(int id) {
        try {
            Connection conn = DBContextApplication.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM student WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getDouble("GPA"),
                        rs.getDate("birth_date"),
                        rs.getInt("course_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(Student student) {
        try {

            Connection conn = DBContextApplication.getConnection();

            System.out.println("Save called");

            if (student.getId() == 0) {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO student (name, age, GPA, birth_date, course_id) VALUES (?, ?, ?, ?, ?)"
                );
                ps.setString(1, student.getName());
                ps.setInt(2, student.getAge());
                ps.setDouble(3, student.getGpa());
                ps.setDate(4, student.getBirthDate());
                ps.setInt(5, student.getCourseId());

                int rows = ps.executeUpdate();
                System.out.println("Rows affected = " + rows);
            } else {
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE student SET name=?, age=?, GPA=?, birth_date=?, course_id=? WHERE id=?"
                );
                ps.setString(1, student.getName());
                ps.setInt(2, student.getAge());
                ps.setDouble(3, student.getGpa());
                ps.setDate(4, student.getBirthDate());
                ps.setInt(5, student.getCourseId());
                ps.setInt(6, student.getId());

                int rows = ps.executeUpdate();
                System.out.println("Rows affected = " + rows);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(int id) {
        try {
            Connection conn = DBContextApplication.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM student WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
