package com.iths.mianshop.service;

import com.iths.mianshop.pojo.Cart;

import java.util.List;

public interface CartService {
    String addToCart(Cart cart);

    List<Cart> getCartByUserId(Integer userId);

    String removeFromCart(Integer cartId);

    // 🔥 新增
    String updateCartQuantity(Integer cartId, Integer quantity);

    // 🔥 新增
    void clearCart(Integer userId);

}
