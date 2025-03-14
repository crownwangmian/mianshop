package com.iths.mianshop.service;

import com.iths.mianshop.pojo.Order;

import java.util.List;

public interface OrderService {
    String createOrder(Order order);

    List<Order> getOrdersByUserId(Integer userId);

    String updateOrderStatus(Integer orderId, Integer status);
}
