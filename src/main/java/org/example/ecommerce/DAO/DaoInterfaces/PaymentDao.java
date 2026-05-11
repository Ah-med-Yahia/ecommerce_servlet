package org.example.ecommerce.DAO.DaoInterfaces;

import org.example.ecommerce.Models.Payment;

public interface PaymentDao {

    Payment save(Payment payment);

    Payment findByOrderId(int orderId);

    boolean updateStatus(int paymentId, String status);
}
