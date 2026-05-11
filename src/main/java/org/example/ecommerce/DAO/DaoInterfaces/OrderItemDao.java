package org.example.ecommerce.DAO.DaoInterfaces;

import org.example.ecommerce.Models.OrderItem;
import java.util.List;

public interface OrderItemDao {
    List<OrderItem> getByOrderId(int orderId);
    void add(OrderItem item);
}
