package org.example.ecommerce.Service.Impl;

import org.example.ecommerce.DAO.DaoImpl.OrderDaoImpl;
import org.example.ecommerce.DAO.DaoImpl.PaymentDaoImpl;
import org.example.ecommerce.DAO.DaoInterfaces.OrderDao;
import org.example.ecommerce.DAO.DaoInterfaces.PaymentDao;
import org.example.ecommerce.Models.Order;
import org.example.ecommerce.Models.Payment;
import org.example.ecommerce.Models.PaymentMethod;
import org.example.ecommerce.Models.PaymentStatus;
import org.example.ecommerce.Service.Interfaces.PaymentService;
import org.example.ecommerce.Utils.RedisUtil;

public class PaymentServiceImpl implements PaymentService {

    private final PaymentDao paymentRepository = new PaymentDaoImpl();
    private final OrderDao orderRepository   = new OrderDaoImpl();

    private static final int RATE_LIMIT_SECONDS = 60;

    public Payment pay(int userId, int orderId, String method) {

        String rateLimitKey = "payment:ratelimit:" + userId + ":" + orderId;
        if (RedisUtil.exists(rateLimitKey)) {
            return null;
        }
        RedisUtil.saveWithTTL(rateLimitKey, "1", RATE_LIMIT_SECONDS);

        Order order = orderRepository.findById(orderId);
        if (order == null || order.getUserId() != userId) return null;

        Payment existing = paymentRepository.findByOrderId(orderId);
        if (existing != null && existing.getStatus() == PaymentStatus.SUCCESS) {
            return null;
        }

        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(order.getTotalPrice());
        payment.setStatus(PaymentStatus.PENDING);

        try {
            payment.setMethod(PaymentMethod.valueOf(method.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return null;
        }

        Payment saved = paymentRepository.save(payment);




        if (saved != null) {
            boolean updated = paymentRepository.updateStatus(saved.getId(), PaymentStatus.SUCCESS.name());
            if (!updated) {
                paymentRepository.updateStatus(saved.getId(), PaymentStatus.FAILED.name());
                return null;
            } else {
                saved.setStatus(PaymentStatus.SUCCESS);
                orderRepository.updateStatus(orderId, "CONFIRMED");
            }
        }

        return saved;
    }

    public Payment getPaymentByOrderId(int orderId) {
        return paymentRepository.findByOrderId(orderId);
    }
}
