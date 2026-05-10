package org.example.ecommerce.DAO.DaoImpl;

import org.example.ecommerce.Config.DBCApplication;
import org.example.ecommerce.DAO.CartItemDao;
import org.example.ecommerce.models.CartItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartItemDaoImpl implements CartItemDao {

    private CartItem mapRow(ResultSet rs) throws SQLException {
        return new CartItem(
                rs.getInt("id"),
                rs.getInt("cart_id"),
                rs.getInt("product_id"),
                rs.getInt("quantity")
        );
    }

    @Override
    public List<CartItem> getByCartId(int cartId) {
        List<CartItem> list = new ArrayList<>();
        try (
                Connection conn = DBCApplication.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT * FROM cart_items WHERE cart_id = ?"
                )
        ) {
            ps.setInt(1, cartId);
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
    public CartItem getByCartIdAndProductId(int cartId, int productId) {
        try (
                Connection conn = DBCApplication.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT * FROM cart_items WHERE cart_id = ? AND product_id = ?"
                )
        ) {
            ps.setInt(1, cartId);
            ps.setInt(2, productId);
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
    public void add(CartItem item) {
        try (
                Connection conn = DBCApplication.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (?, ?, ?)"
                )
        ) {
            ps.setInt(1, item.getCartId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateQuantity(int id, int quantity) {
        try (
                Connection conn = DBCApplication.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE cart_items SET quantity = ? WHERE id = ?"
                )
        ) {
            ps.setInt(1, quantity);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try (
                Connection conn = DBCApplication.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM cart_items WHERE id = ?"
                )
        ) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearByCartId(int cartId) {
        try (
                Connection conn = DBCApplication.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM cart_items WHERE cart_id = ?"
                )
        ) {
            ps.setInt(1, cartId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
