package com.example.demo.DAO;

import com.example.demo.models.Student;
import java.util.List;

public interface StudentDao {
    List<Student> findAll();

    Student findById(int id);

    void save(Student student);

    void deleteById(int id);
}
