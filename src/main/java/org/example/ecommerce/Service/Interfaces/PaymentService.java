package org.example.ecommerce.Service.Interfaces;

import org.example.ecommerce.Models.Payment;

public interface PaymentService {

    Payment pay(int userId, int orderId, String method);

    Payment getPaymentByOrderId(int orderId);
}
