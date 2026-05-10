package org.example.ecommerce.DAO;

import org.example.ecommerce.models.Order;
import java.util.List;

public interface OrderDao {
    List<Order> getAll();
    List<Order> getByUserId(int userId);
    Order getById(int id);
    int create(Order order);
    void updateStatus(int id, String status);
}