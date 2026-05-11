package org.example.ecommerce.DAO.DaoInterfaces;

import org.example.ecommerce.Models.Order;
import org.example.ecommerce.Models.OrderItem;

import java.util.List;

public interface OrderDao {

    Order save(Order order);

    List<OrderItem> saveItems(List<OrderItem> items);


    // admin
    List<Order> findAll();

    // user
    List<Order> findByUserId(int userId);


    Order findById(int id);

    List<OrderItem> findItemsByOrderId(int orderId);

    boolean updateStatus(int orderId, String status);

    boolean decrementStock(int productId, int quantity);

}