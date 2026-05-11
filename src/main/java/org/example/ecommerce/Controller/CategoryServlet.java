package org.example.ecommerce.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.ecommerce.Service.Impl.CategoryServiceImpl;
import org.example.ecommerce.Service.Interfaces.CategoryService;
import org.example.ecommerce.Models.Category;
import org.example.ecommerce.Utils.ResponseUtil;

import java.io.IOException;

@WebServlet("/categories/*")
public class CategoryServlet extends HttpServlet {

    private CategoryService categoryService;
    private ObjectMapper mapper;

    @Override
    public void init() {
        ServletContext ctx = getServletContext();
        mapper = (ObjectMapper) ctx.getAttribute("objectMapper");
        categoryService = new CategoryServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            ResponseUtil.sendJson(req,res, 200, categoryService.getAllCategories());
        } else {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                if(id<0){

                    ResponseUtil.sendError(req,res, 400, "Invalid ID");

                }
                Category cat = categoryService.getCategoryById(id);
                if (cat == null) ResponseUtil.sendError(req,res, 404, "Category not found");
                else ResponseUtil.sendJson(req,res, 200, cat);
            } catch (NumberFormatException e) {
                ResponseUtil.sendError(req,res, 400, "Wrong Happen");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (!isAdmin(req)) { ResponseUtil.sendError(req, res, 403, "Admin access required"); return; }
        try {
            Category cat = mapper.readValue(req.getInputStream(), Category.class);
            ResponseUtil.sendJson(req, res, 201, categoryService.addCategory(cat));
        } catch (Exception e) {
            ResponseUtil.sendError(req, res, 500, e.toString());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (!isAdmin(req)) { ResponseUtil.sendError(req, res, 403, "Admin access required"); return; }
        try {
            int id = Integer.parseInt(req.getPathInfo().substring(1));
            if (id < 0) {
                ResponseUtil.sendError(req, res, 400, "Invalid ID");
                return;
            }
            Category cat = mapper.readValue(req.getInputStream(), Category.class);
            Category updated = categoryService.updateCategory(id, cat);
            if (updated == null) ResponseUtil.sendError(req, res, 404, "Category not found");
            else ResponseUtil.sendJson(req, res, 200, updated);
        } catch (NumberFormatException e) {
            ResponseUtil.sendError(req, res, 400, "Invalid ID format");
        } catch (Exception e) {
            ResponseUtil.sendError(req, res, 500, "Internal server error");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (!isAdmin(req)) { ResponseUtil.sendError(req, res, 403, "Admin access required"); return; }
        try {
            int id = Integer.parseInt(req.getPathInfo().substring(1));
            if (id < 0) {
                ResponseUtil.sendError(req, res, 400, "Invalid ID");
                return;
            }
            if (categoryService.deleteCategory(id)) ResponseUtil.sendSuccess(req, res, "Category deleted");
            else ResponseUtil.sendError(req, res, 404, "Category not found");
        } catch (NumberFormatException e) {
            ResponseUtil.sendError(req, res, 400, "Invalid ID format");
        } catch (Exception e) {
            ResponseUtil.sendError(req, res, 500, "Internal server error");
        }
    }

    private boolean isAdmin(HttpServletRequest req) {
        return "ADMIN".equals(req.getAttribute("role"));
    }
}
