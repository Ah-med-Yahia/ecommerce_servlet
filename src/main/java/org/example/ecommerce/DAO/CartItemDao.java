package org.example.ecommerce.DAO;

import org.example.ecommerce.models.CartItem;

import java.util.List;


public interface CartItemDao {
    List<CartItem> getByCartId(int cartId);              // View cart
    CartItem getByCartIdAndProductId(int cartId,
                                     int productId);
    void add(CartItem item);
    void updateQuantity(int id, int quantity);
    void delete(int id);
    void clearByCartId(int cartId);
}
