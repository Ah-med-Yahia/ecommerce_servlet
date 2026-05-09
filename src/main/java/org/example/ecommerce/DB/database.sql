CREATE DATABASE IF NOT EXISTS manageStudentAndCourse;

USE manageStudentAndCourse;

CREATE TABLE IF NOT EXISTS course (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    hours INT CHECK (hours > 0)
    );

CREATE TABLE IF NOT EXISTS student (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    age INT CHECK (age > 0),
    GPA DOUBLE CHECK (GPA BETWEEN 0 AND 4),
    birth_date DATE,
    course_id INT,
    FOREIGN KEY (course_id) REFERENCES course(id)
    );