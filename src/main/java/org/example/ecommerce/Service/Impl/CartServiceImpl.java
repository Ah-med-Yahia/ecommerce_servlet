package org.example.ecommerce.Service.Impl;


import org.example.ecommerce.Models.Cart;
import org.example.ecommerce.Service.Interfaces.CartService;
import org.example.ecommerce.DAO.DaoInterfaces.CartDao;
import org.example.ecommerce.DAO.DaoImpl.CartDaoImpl;

import java.util.List;

public class CartServiceImpl implements CartService {

    private final CartDao cartRepository = new CartDaoImpl();

    @Override
    public List<Cart> getCartByUserId(int userId) {
        return cartRepository.findByUserId(userId);
    }

    public Cart addToCart(int userId, int productId, int quantity) {
        Cart existing = cartRepository.findByUserIdAndProductId(userId, productId);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            return cartRepository.update(existing);
        }
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setProductId(productId);
        cart.setQuantity(quantity);
        return cartRepository.save(cart);
    }

    public Cart updateCartItem(int cartId, int quantity) {
        if (quantity <= 0) return null;

        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setQuantity(quantity);

        return cartRepository.update(cart);
    }

    public boolean removeFromCart(int cartId) {
        return cartRepository.delete(cartId);
    }
}
