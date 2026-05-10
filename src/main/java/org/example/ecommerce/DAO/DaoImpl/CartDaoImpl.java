package org.example.ecommerce.DAO.DaoImpl;

import org.example.ecommerce.Config.DBCApplication;
import org.example.ecommerce.DAO.CartDao;
import org.example.ecommerce.models.Cart;
import java.sql.*;

public class CartDaoImpl implements CartDao {

    @Override
    public Cart getByUserId(int userId) {
        try (
                Connection conn = DBCApplication.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT * FROM cart WHERE user_id = ?"
                )
        ) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Cart(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Cart create(int userId) {
        try (
                Connection conn = DBCApplication.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO cart (user_id) VALUES (?)",
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {
            ps.setInt(1, userId);
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return new Cart(keys.getInt(1), userId, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
