
package org.example.ecommerce.Service.Interfaces;

import org.example.ecommerce.Models.Cart;

import java.util.List;

public interface CartService {

    List<Cart> getCartByUserId(int userId);

    Cart addToCart(int userId, int productId, int quantity);

    Cart updateCartItem(int cartId, int quantity);

    boolean removeFromCart(int cartId);
}
