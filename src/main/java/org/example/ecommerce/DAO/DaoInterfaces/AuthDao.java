package org.example.ecommerce.DAO.DaoInterfaces;

import java.time.LocalDateTime;

public interface AuthDao {


    boolean blacklistToken(String tokenHash, LocalDateTime expiresAt);


    boolean isBlacklisted(String tokenHash);


    void deleteExpiredTokens();
}
