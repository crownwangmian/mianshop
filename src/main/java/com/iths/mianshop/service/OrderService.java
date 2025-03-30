package com.iths.mianshop.service;

import com.iths.mianshop.pojo.Order;

import java.util.List;

public interface OrderService {
    // 🔥 生成订单
    Order createOrder(Integer userId);

    // 🔥 查询订单详情
    Order getOrderById(Integer orderId);

    // 🔥 查询用户所有订单
    List<Order> getOrdersByUserId(Integer userId);

    List<Order> getAllOrders();  // 查询所有订单

    List<Order> getOrdersByStatus(Integer status);  // 按状态查询

    void updateOrderStatus(Integer orderId, Integer status);  // 更新订单状态


    void deleteOrder(Integer orderId);
}
