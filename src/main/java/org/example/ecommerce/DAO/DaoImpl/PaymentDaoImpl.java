package org.example.ecommerce.DAO.DaoImpl;

import org.example.ecommerce.DAO.DaoInterfaces.PaymentDao;
import org.example.ecommerce.Models.Payment;
import org.example.ecommerce.Config.DBCApplication;
import org.example.ecommerce.Models.PaymentMethod;
import org.example.ecommerce.Models.PaymentStatus;

import java.sql.*;

public class PaymentDaoImpl implements PaymentDao {

    @Override
    public Payment save(Payment payment) {
        String sql = "INSERT INTO payments(order_id, method, status, amount) VALUES(?, ?, ?, ?)";
        try (Connection conn = DBCApplication.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, payment.getOrderId());
            stmt.setString(2, payment.getMethod().name());
            stmt.setString(3, payment.getStatus().name());
            stmt.setBigDecimal(4, payment.getAmount());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return findById(keys.getInt(1));
            }

        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public Payment findByOrderId(int orderId) {
        String sql = "SELECT * FROM payments WHERE order_id = ?";
        try (Connection conn = DBCApplication.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public boolean updateStatus(int paymentId, String status) {
        String sql = "UPDATE payments SET status = ? WHERE id = ?";
        try (Connection conn = DBCApplication.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, paymentId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private Payment findById(int id) {
        String sql = "SELECT * FROM payments WHERE id = ?";
        try (Connection conn = DBCApplication.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    private Payment mapRow(ResultSet rs) throws SQLException {
        Payment p = new Payment();
        p.setId(rs.getInt("id"));
        p.setOrderId(rs.getInt("order_id"));
        p.setMethod(PaymentMethod.valueOf(rs.getString("method")));
        p.setStatus(PaymentStatus.valueOf(rs.getString("status")));
        p.setAmount(rs.getBigDecimal("amount"));
        p.setCreatedAt(rs.getTimestamp("created_at") != null
                ? rs.getTimestamp("created_at").toLocalDateTime() : null);
        return p;
    }
}
