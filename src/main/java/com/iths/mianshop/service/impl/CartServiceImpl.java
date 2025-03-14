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
        cartRepository.save(cart);
        return "✅ 商品已加入购物车";
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

}
