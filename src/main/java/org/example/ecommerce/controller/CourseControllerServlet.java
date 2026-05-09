package com.example.demo.controller;

import com.example.demo.models.Course;
import com.example.demo.service.CourseDAOImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/courses/*")
public class CourseControllerServlet extends HttpServlet {

    private CourseDAOImpl courseService;
    private ObjectMapper mapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        courseService = new CourseDAOImpl();
        mapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            List<Course> courses = courseService.findAll();
            mapper.writeValue(resp.getWriter(), courses);
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                if(id<=0){
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
                else{
                    Course course = courseService.findById(id);

                    if (course != null) {
                        mapper.writeValue(resp.getWriter(), course);
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

        Course course = mapper.readValue(req.getInputStream(), Course.class);
        courseService.save(course);

        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String pathInfo = req.getPathInfo();

        try {
            int id = Integer.parseInt(pathInfo.substring(1));

            Course oldCourse = courseService.findById(id);

            if (oldCourse == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            Course newCourse = mapper.readValue(req.getInputStream(), Course.class);

            if (newCourse.getName() != null) {
                oldCourse.setName(newCourse.getName());
            }

            if (newCourse.getHours() != 0) {
                oldCourse.setHours(newCourse.getHours());
            }

            courseService.save(oldCourse);

            mapper.writeValue(resp.getWriter(), oldCourse);
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
            courseService.deleteById(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
