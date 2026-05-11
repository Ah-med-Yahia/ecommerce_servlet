package org.example.ecommerce.Controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.ecommerce.Models.Order;
import org.example.ecommerce.Service.Impl.OrderServiceImpl;
import org.example.ecommerce.Service.Interfaces.OrderService;
import org.example.ecommerce.Utils.ResponseUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/orders/*")
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String pathInfo = req.getPathInfo();

        String role = (String) req.getAttribute("role");
        int userId = (Integer) req.getAttribute("userId");

        try {

            // Admin
            if (pathInfo != null && pathInfo.equals("/all")) {

                if (!"ADMIN".equals(role)) {
                    ResponseUtil.sendError(req,res, 403, "Admin access required");
                    return;
                }

                List<Order> all = orderService.getAllOrders();
                ResponseUtil.sendJson(req,res, 200, all);
                return;
            }

            // User
            if (pathInfo == null || pathInfo.equals("/")) {

                List<Order> orders =
                        orderService.getOrdersByUserId(userId);

                ResponseUtil.sendJson(req,res, 200, orders);
                return;
            }

            // Order Details
            int orderId = Integer.parseInt(pathInfo.substring(1));

            if(orderId<0){
                ResponseUtil.sendError(req,res, 400, "Invalid order ID");
            }

            Map<String, Object> detail =
                    orderService.getOrderWithItems(orderId);

            if (detail == null) {
                ResponseUtil.sendError(req,res, 404, "Order not found");
            } else {
                ResponseUtil.sendJson(req,res, 200, detail);
            }

        } catch (NumberFormatException e) {
            ResponseUtil.sendError(req,res, 400, "Invalid order");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        Integer userIdObj = (Integer) req.getAttribute("userId");


        Order order = orderService.placeOrder(userIdObj);

        if (order == null) {
            ResponseUtil.sendError(req,res, 400,
                    "Cart is empty or insufficient stock");
        } else {
            ResponseUtil.sendJson(req,res, 201, order);
        }
    }
}