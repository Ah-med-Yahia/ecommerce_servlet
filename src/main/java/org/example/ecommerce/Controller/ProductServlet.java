package org.example.ecommerce.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.ecommerce.Models.Product;
import org.example.ecommerce.Service.Impl.ProductServiceImpl;
import org.example.ecommerce.Service.Interfaces.ProductService;
import org.example.ecommerce.Utils.ResponseUtil;

import java.io.IOException;
import java.util.List;

@WebServlet("/products/*")
public class ProductServlet extends HttpServlet {

    private final ProductService productService = new ProductServiceImpl();
    private ObjectMapper mapper;

    @Override
    public void init() {
        ServletContext ctx = getServletContext();
        mapper = (ObjectMapper) ctx.getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            List<Product> products = productService.getAllProducts();
            ResponseUtil.sendJson(req,res, 200, products);
        } else {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                if(id<0){
                    ResponseUtil.sendError(req,res, 404, "Invalid product ID");
                }
                Product product = productService.getProductById(id);
                if (product == null) {
                    ResponseUtil.sendError(req,res, 404, "Product not found");
                } else {
                    ResponseUtil.sendJson(req,res, 200, product);
                }
            } catch (NumberFormatException e) {
                ResponseUtil.sendError(req,res, 400, "Invalid product ID");
            }
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (!isAdmin(req)) {
            ResponseUtil.sendError(req,res, 403, "Admin access required");
            return;
        }
        try {
            Product product = mapper.readValue(req.getInputStream(), Product.class);
            Product saved = productService.addProduct(product);
            ResponseUtil.sendJson(req,res, 201, saved);
        } catch (Exception e) {
            ResponseUtil.sendError(req,res, 400, "Invalid request body");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (!isAdmin(req)) {
            ResponseUtil.sendError(req,res, 403, "Admin access required");
            return;
        }
        try {
            int id = Integer.parseInt(req.getPathInfo().substring(1));
            if(id<0){
                ResponseUtil.sendError(req,res, 404, "Invalid product ID");
            }
            Product product = mapper.readValue(req.getInputStream(), Product.class);
            Product updated = productService.updateProduct(id, product);
            if (updated == null) {
                ResponseUtil.sendError(req,res, 404, "Product not found");
            } else {
                ResponseUtil.sendJson(req,res, 200, updated);
            }
        } catch (NumberFormatException e) {
            ResponseUtil.sendError(req,res, 400, "Invalid product ID");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (!isAdmin(req)) {
            ResponseUtil.sendError(req,res, 403, "Admin access required");
            return;
        }
        try {
            int id = Integer.parseInt(req.getPathInfo().substring(1));
            if(id<0){
                ResponseUtil.sendError(req,res, 404, "Invalid product ID");
            }
            boolean deleted = productService.deleteProduct(id);
            if (!deleted) {
                ResponseUtil.sendError(req,res, 404, "Product not found");
            } else {
                ResponseUtil.sendSuccess(req,res, "Product deleted");
            }
        } catch (NumberFormatException e) {
            ResponseUtil.sendError(req,res, 400, "Invalid product ID");
        }
    }

    private boolean isAdmin(HttpServletRequest req) {
        String role = (String) req.getAttribute("role");
        return "ADMIN".equals(role);
    }
}
