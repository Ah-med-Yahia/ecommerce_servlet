package org.example.ecommerce.Service.Interfaces;

import org.example.ecommerce.Models.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {


    Order placeOrder(int userId);

    // ================= GET ALL ORDERS (Admin) =================
    List<Order> getAllOrders();

    // ================= GET USER ORDERS =================
    List<Order> getOrdersByUserId(int userId);

    // ================= GET ORDER DETAILS =================
    Map<String, Object> getOrderWithItems(int orderId);
}
