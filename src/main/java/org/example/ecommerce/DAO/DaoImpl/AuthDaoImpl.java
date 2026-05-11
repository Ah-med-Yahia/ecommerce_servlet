package org.example.ecommerce.DAO.DaoImpl;

import org.example.ecommerce.Config.DBCApplication;
import org.example.ecommerce.DAO.DaoInterfaces.AuthDao;

import java.sql.*;
import java.time.LocalDateTime;

public class AuthDaoImpl implements AuthDao {

    @Override
    public boolean blacklistToken(String tokenHash, LocalDateTime expiresAt) {
        String sql = "INSERT IGNORE INTO revoked_tokens (token_hash, expires_at) VALUES (?, ?)";

        try (Connection conn = DBCApplication.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tokenHash);
            stmt.setTimestamp(2, Timestamp.valueOf(expiresAt));

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("blacklistToken error: " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean isBlacklisted(String tokenHash) {
        String sql = "SELECT COUNT(*) FROM revoked_tokens WHERE token_hash = ? AND expires_at > NOW() ";


        try (Connection conn =DBCApplication.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tokenHash);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void deleteExpiredTokens() {
        String sql = "DELETE FROM revoked_tokens WHERE expires_at <= NOW()";
        try (Connection conn = DBCApplication.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int deleted = stmt.executeUpdate();
            System.out.println("Cleaned up " + deleted + " expired tokens");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}