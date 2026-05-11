package org.example.ecommerce.DAO.DaoImpl;

import org.example.ecommerce.Config.DBCApplication;
import org.example.ecommerce.DAO.DaoInterfaces.ProductDAO;
import org.example.ecommerce.Models.Product;
import java.sql.*;
        import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDAO {

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection conn = DBCApplication.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public Product findById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = DBCApplication.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Product save(Product product) {
        String sql = "INSERT INTO products(name, description, price, stock, category_id) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = DBCApplication.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setInt(4, product.getStock());
            if (product.getCategoryId() != null)
                stmt.setInt(5, product.getCategoryId());
            else
                stmt.setNull(5, Types.INTEGER);

            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return findById(keys.getInt(1));
            }
            return product;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Product update(Product product) {
        String sql = "UPDATE products SET name=?, description=?, price=?, stock=?, category_id=? WHERE id=?";
        try (Connection conn = DBCApplication.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setInt(4, product.getStock());
            if (product.getCategoryId() != null)
                stmt.setInt(5, product.getCategoryId());
            else
                stmt.setNull(5, Types.INTEGER);
            stmt.setInt(6, product.getId());
            stmt.executeUpdate();
            return findById(product.getId());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DBCApplication.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setStock(rs.getInt("stock"));
        int catId = rs.getInt("category_id");
        p.setCategoryId(rs.wasNull() ? null : catId);

        p.setCreatedAt(
                rs.getTimestamp("created_at") != null
                        ? rs.getTimestamp("created_at").toLocalDateTime()
                        : null
        );

        p.setUpdatedAt(
                rs.getTimestamp("updated_at") != null
                        ? rs.getTimestamp("updated_at").toLocalDateTime()
                        : null
        );

        return p;
    }
}