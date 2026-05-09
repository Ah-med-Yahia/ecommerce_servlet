package com.example.demo.models;
import java.sql.Date;


public class Student {
    private int id;
    private String name;
    private int age;
    private double gpa;
    private Date birthDate;
    private int courseId;

    public Student() {
    }

    public Student(int id, String name, int age, double gpa, Date birthDate, int courseId) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gpa = gpa;
        this.birthDate = birthDate;
        this.courseId = courseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
