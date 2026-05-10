package org.example.ecommerce.DAO.DaoImpl;

import org.example.ecommerce.Config.DBCApplication;
import org.example.ecommerce.DAO.ProductDAO;
import org.example.ecommerce.models.Product;
import java.sql.*;
        import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDAO {


    private Product mapRow(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("id"),
                rs.getInt("category_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getBigDecimal("price"),
                rs.getInt("stock"),
                rs.getString("image_url"),
                rs.getTimestamp("created_at")
        );
    }

    @Override
    public List<Product> getAll() {
        List<Product> list = new ArrayList<>();

        try (
                Connection conn = DBCApplication.getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM products");
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public Product getById(int id) {
        try (
                Connection conn = DBCApplication.getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM products WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Product> getByCategory(int categoryId) {
        List<Product> list = new ArrayList<>();
        try (
                Connection conn = DBCApplication.getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM products WHERE category_id = ?")
        ) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void add(Product p) {
        try (
                Connection conn = DBCApplication.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO products (category_id, name, description, price, stock, image_url) " +
                                "VALUES (?, ?, ?, ?, ?, ?)"
                )
        ) {
            ps.setInt(1, p.getCategoryId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getDescription());
            ps.setBigDecimal(4, p.getPrice());
            ps.setInt(5, p.getStock());
            ps.setString(6, p.getImageUrl());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Product p) {
        try (
                Connection conn = DBCApplication.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE products SET category_id=?, name=?, description=?, " +
                                "price=?, stock=?, image_url=? WHERE id=?"
                )
        ) {
            ps.setInt(1, p.getCategoryId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getDescription());
            ps.setBigDecimal(4, p.getPrice());
            ps.setInt(5, p.getStock());
            ps.setString(6, p.getImageUrl());
            ps.setInt(7, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try (
                Connection conn = DBCApplication.getConnection();
                PreparedStatement ps = conn.prepareStatement("DELETE FROM products WHERE id = ?")
        ) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}