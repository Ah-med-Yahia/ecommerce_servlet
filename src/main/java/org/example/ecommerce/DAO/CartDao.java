package org.example.ecommerce.DAO;

import org.example.ecommerce.models.Cart;

public interface CartDao {
    Cart getByUserId(int userId);
    Cart create(int userId);
}
