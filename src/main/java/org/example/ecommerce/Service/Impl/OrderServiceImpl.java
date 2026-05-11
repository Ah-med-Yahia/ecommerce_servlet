package org.example.ecommerce.Service.Impl;

import org.example.ecommerce.DAO.DaoImpl.CartDaoImpl;
import org.example.ecommerce.DAO.DaoImpl.OrderDaoImpl;
import org.example.ecommerce.DAO.DaoImpl.ProductDaoImpl;
import org.example.ecommerce.DAO.DaoInterfaces.CartDao;
import org.example.ecommerce.DAO.DaoInterfaces.OrderDao;
import org.example.ecommerce.DAO.DaoInterfaces.ProductDAO;
import org.example.ecommerce.Models.*;
import org.example.ecommerce.Service.Interfaces.OrderService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderServiceImpl implements OrderService {
    private final OrderDao orderRepository   = new OrderDaoImpl();
    private final CartDao cartRepository    = new CartDaoImpl();
    private final ProductDAO productRepository = new ProductDaoImpl();

    public Order placeOrder(int userId) {

        List<Cart> cartItems = cartRepository.findByUserId(userId);
        if (cartItems == null || cartItems.isEmpty()) return null;

        try {
            BigDecimal total = BigDecimal.ZERO;
            List<OrderItem> items = new ArrayList<>();

            for (Cart c : cartItems) {
                Product p = productRepository.findById(c.getProductId());

                if (p == null || p.getStock() < c.getQuantity()) {
                    return null;
                }

                total = total.add(p.getPrice().multiply(BigDecimal.valueOf(c.getQuantity())));

                OrderItem oi = new OrderItem();
                oi.setProductId(p.getId());
                oi.setQuantity(c.getQuantity());
                oi.setPrice(p.getPrice());
                items.add(oi);
            }

            Order order = new Order();
            order.setUserId(userId);
            order.setTotalPrice(total);
            order.setStatus(OrderStatus.PENDING);

            Order saved = orderRepository.save(order);
            if (saved == null) return null;

            for (OrderItem i : items) i.setOrderId(saved.getId());


            // save after add order id
            List<OrderItem> savedItems = orderRepository.saveItems(items);
            if (savedItems == null) return null;

            for (Cart c : cartItems) {
                boolean ok = orderRepository.decrementStock(c.getProductId(), c.getQuantity());
                if (!ok) {
                    return null;
                }
            }

            cartRepository.deleteByUserId(userId);

            return saved;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // admin
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // user
    public List<Order> getOrdersByUserId(int userId) {
        return orderRepository.findByUserId(userId);
    }


    public Map<String, Object> getOrderWithItems(int orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) return null;

        List<OrderItem> items = orderRepository.findItemsByOrderId(orderId);


        Map<String, Object> response = new HashMap<>();
        response.put("order", order);
        response.put("items", items);

        return response;
    }
}
