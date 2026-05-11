package org.example.ecommerce.DAO.DaoImpl;


import org.example.ecommerce.Config.DBCApplication;
import org.example.ecommerce.DAO.DaoInterfaces.OrderItemDao;
import org.example.ecommerce.Models.OrderItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDaoImpl implements OrderItemDao {

    private OrderItem mapRow(ResultSet rs) throws SQLException {
        return new OrderItem(
                rs.getInt("id"),
                rs.getInt("order_id"),
                rs.getInt("product_id"),
                rs.getInt("quantity"),
                rs.getBigDecimal("price")
        );
    }

    @Override
    public List<OrderItem> getByOrderId(int orderId) {
        List<OrderItem> list = new ArrayList<>();
        try (
                Connection conn = DBCApplication.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT * FROM order_items WHERE order_id = ?"
                )
        ) {
            ps.setInt(1, orderId);
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
    public void add(OrderItem item) {
        try (
                Connection conn = DBCApplication.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)"
                )
        ) {
            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setBigDecimal(4, item.getPrice());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
