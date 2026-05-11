package org.example.ecommerce.DAO.DaoInterfaces;

import org.example.ecommerce.Models.Cart;
import java.util.List;

public interface CartDao {

    List<Cart> findByUserId(int userId);

    Cart findByUserIdAndProductId(int userId, int productId);

    Cart save(Cart cart);

    Cart update(Cart cart);

    boolean delete(int id);

    boolean deleteByUserId(int userId);
}
