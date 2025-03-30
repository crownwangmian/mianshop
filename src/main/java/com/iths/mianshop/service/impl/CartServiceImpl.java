package com.iths.mianshop.service.impl;

import com.iths.mianshop.pojo.Cart;
import com.iths.mianshop.repository.CartRepository;
import com.iths.mianshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Override
    public String addToCart(Cart cart) {
        Cart existingCart = cartRepository.findByUserIdAndItemId(cart.getUser().getId(), cart.getItem().getId());

        if (existingCart != null) {
            // 🔥 已存在，增加数量
            existingCart.setQuantity(existingCart.getQuantity() + cart.getQuantity());
            cartRepository.save(existingCart);
            return "商品数量已更新";
        } else {
            cartRepository.save(cart);
            return "✅ 商品已加入购物车";
        }
    }

    @Override
    public List<Cart> getCartByUserId(Integer userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public String removeFromCart(Integer cartId) {
        cartRepository.deleteById(cartId);
        return "✅ 商品已从购物车中删除";
    }

    @Override
    public String updateCartQuantity(Integer cartId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("购物车不存在"));
        cart.setQuantity(quantity);
        cartRepository.save(cart);
        return "✅ 商品数量已更新";
    }

    @Override
    public void clearCart(Integer userId) {
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        cartRepository.deleteAll(cartItems);
    }

}
