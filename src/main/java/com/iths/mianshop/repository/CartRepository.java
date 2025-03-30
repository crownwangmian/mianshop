package com.iths.mianshop.repository;

import com.iths.mianshop.pojo.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByUserId(Integer userId);

    // 查询当前用户购物车中是否存在相同商品
    Cart findByUserIdAndItemId(Integer userId, Integer itemId);

}
