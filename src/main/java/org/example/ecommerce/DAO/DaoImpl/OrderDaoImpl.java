package org.example.ecommerce.DAO.DaoImpl;

import org.example.ecommerce.Config.DBCApplication;
import org.example.ecommerce.DAO.OrderDao;
import org.example.ecommerce.models.Order;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {

    private Order mapRow(ResultSet rs) throws SQLException {
        return new Order(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getBigDecimal("total_amount"),
                rs.getString("status"),
                rs.getTimestamp("created_at")
        );
    }

    @Override
    public List<Order> getAll() {
        List<Order> list = new ArrayList<>();
        try (
                Connection conn = DBCApplication.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT * FROM orders ORDER BY created_at DESC"
                )
        ) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Order> getByUserId(int userId) {
        List<Order> list = new ArrayList<>();
        try (
                Connection conn = DBCApplication.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT * FROM orders WHERE user_id = ? ORDER BY created_at DESC"
                )
        ) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Order getById(int id) {
        try (
                Connection conn = DBCApplication.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT * FROM orders WHERE id = ?"
                )
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int create(Order order) {
        try (
                Connection conn = DBCApplication.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO orders (user_id, total_amount, status) VALUES (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {
            ps.setInt(1, order.getUserId());
            ps.setBigDecimal(2, order.getTotalAmount());
            ps.setString(3, "pending");
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void updateStatus(int id, String status) {
        try (
                Connection conn = DBCApplication.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE orders SET status = ? WHERE id = ?"
                )
        ) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
