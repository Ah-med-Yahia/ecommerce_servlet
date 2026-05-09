package com.example.demo.controller;

import com.example.demo.models.Course;
import com.example.demo.models.Student;
import com.example.demo.service.CourseDAOImpl;
import com.example.demo.service.StudentDAOImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/students/*")
public class StudentControllerServlet extends HttpServlet {

    private StudentDAOImpl studentService;
    private CourseDAOImpl  courseService;
    private ObjectMapper mapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        studentService = new StudentDAOImpl();
        courseService=new CourseDAOImpl();
        mapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            List<Student> students = studentService.findAll();
            mapper.writeValue(resp.getWriter(), students);
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                if(id<=0){
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
                else {
                    Student student = studentService.findById(id);

                    if (student != null) {
                        mapper.writeValue(resp.getWriter(), student);
                        resp.setStatus(HttpServletResponse.SC_OK);
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    }
                }
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        System.out.println("POST called");

        try {
            Student student = mapper.readValue(req.getInputStream(), Student.class);

            Course course = courseService.findById(student.getCourseId());


            if (course == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"error\":\"Course not found\"}");
                resp.getWriter().flush();
                return;
            }

            studentService.save(student);

            resp.setStatus(HttpServletResponse.SC_CREATED);


        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String pathInfo = req.getPathInfo();

        try {
            int id = Integer.parseInt(pathInfo.substring(1));

            Student oldStudent = studentService.findById(id);

            if (oldStudent == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            Student newStudent = mapper.readValue(req.getInputStream(), Student.class);

            if (newStudent.getName() != null) {
                oldStudent.setName(newStudent.getName());
            }

            if (newStudent.getAge() != 0) {
                oldStudent.setAge(newStudent.getAge());
            }

            if (newStudent.getGpa() != 0) {
                oldStudent.setGpa(newStudent.getGpa());
            }

            if (newStudent.getBirthDate() != null) {
                oldStudent.setBirthDate(newStudent.getBirthDate());
            }

            if (newStudent.getCourseId() != 0) {
                oldStudent.setCourseId(newStudent.getCourseId());
            }

            studentService.save(oldStudent);

            mapper.writeValue(resp.getWriter(), oldStudent);
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        try {
            int id = Integer.parseInt(pathInfo.substring(1));

            studentService.deleteById(id);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
