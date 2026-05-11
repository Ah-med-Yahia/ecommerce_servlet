package org.example.ecommerce.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.ecommerce.Models.Payment;
import org.example.ecommerce.Service.Impl.PaymentServiceImpl;
import org.example.ecommerce.Service.Interfaces.PaymentService;
import org.example.ecommerce.Utils.ResponseUtil;

import java.io.IOException;
import java.util.Map;

@WebServlet("/payments/*")
public class PaymentServlet extends HttpServlet {

    private PaymentService paymentService;
    private ObjectMapper mapper;

    @Override
    public void init() {
        ServletContext ctx = getServletContext();
        mapper = (ObjectMapper) ctx.getAttribute("objectMapper");
        paymentService = new PaymentServiceImpl();
    }

    // GET /payments/{orderId}  → view payment status
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            int orderId = Integer.parseInt(req.getPathInfo().substring(1));
            if(orderId<0){
                ResponseUtil.sendError(req,res,400, "Invalid order ID");
            }
            Payment payment = paymentService.getPaymentByOrderId(orderId);
            if (payment == null) ResponseUtil.sendError(req,res, 404, "Payment not found");
            else ResponseUtil.sendJson(req,res, 200, payment);
        } catch (NumberFormatException e) {
            ResponseUtil.sendError(req,res, 400, "Something Wrong Happen");
        }
    }

    // POST /payments  { orderId, method: "CASH" | "CARD" }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int userId = (int) req.getAttribute("userId");
        try {
            Map<String, Object> body = mapper.readValue(req.getInputStream(), Map.class);
            int orderId = (Integer) body.get("orderId");
            String method = (String) body.get("method");

            Payment payment = paymentService.pay(userId, orderId, method);

            if (payment == null) {
                ResponseUtil.sendError(req,res, 400,
                        "Payment failed. Order may not exist, already paid, or rate-limited.");
            } else {
                ResponseUtil.sendJson(req,res, 201, payment);
            }
        } catch (Exception e) {
            ResponseUtil.sendError(req,res, 400, "Invalid request body");
        }
    }
}
