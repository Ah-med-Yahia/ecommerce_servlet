package org.example.ecommerce.DAO.DaoImpl;

import org.example.ecommerce.Config.DBCApplication;
import org.example.ecommerce.DAO.DaoInterfaces.CartDao;
import org.example.ecommerce.Models.Cart;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDaoImpl implements CartDao {

    @Override
    public List<Cart> findByUserId(int userId) {
        List<Cart> list = new ArrayList<>();
        String sql = "SELECT * FROM cart WHERE user_id = ?";
        try (Connection conn = DBCApplication.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public Cart findByUserIdAndProductId(int userId, int productId) {
        String sql = "SELECT * FROM cart WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBCApplication.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public Cart save(Cart cart) {
        String sql = "INSERT INTO cart(user_id, product_id, quantity) VALUES(?, ?, ?)";
        try (Connection conn = DBCApplication.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, cart.getUserId());
            stmt.setInt(2, cart.getProductId());
            stmt.setInt(3, cart.getQuantity());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) cart.setId(keys.getInt(1));
            return cart;
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public Cart update(Cart cart) {
        String sql = "UPDATE cart SET quantity = ? WHERE id = ?";
        try (Connection conn = DBCApplication.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cart.getQuantity());
            stmt.setInt(2, cart.getId());
            stmt.executeUpdate();
            return cart;
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM cart WHERE id = ?";
        try (Connection conn = DBCApplication.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean deleteByUserId(int userId) {
        String sql = "DELETE FROM cart WHERE user_id = ?";
        try (Connection conn = DBCApplication.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private Cart mapRow(ResultSet rs) throws SQLException {
        return new Cart(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getInt("product_id"),
                rs.getInt("quantity")
        );
    }
}
