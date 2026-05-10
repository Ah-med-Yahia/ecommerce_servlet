package org.example.ecommerce.DAO;

import org.example.ecommerce.models.OrderItem;
import java.util.List;

public interface OrderItemDao {
    List<OrderItem> getByOrderId(int orderId);
    void add(OrderItem item);
}
