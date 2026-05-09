package com.example.demo.DAO;

import com.example.demo.models.Course;
import java.util.List;

public interface CourseDao {
    List<Course> findAll();

    Course findById(int id);

    void save(Course course);

    void deleteById(int id);
}