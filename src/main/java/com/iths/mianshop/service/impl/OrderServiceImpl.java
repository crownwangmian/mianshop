package com.iths.mianshop.service.impl;

import com.iths.mianshop.pojo.Order;
import com.iths.mianshop.repository.OrderRepository;
import com.iths.mianshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public String createOrder(Order order) {
        orderRepository.save(order);
        return "✅ 订单已创建";
    }

    @Override
    public List<Order> getOrdersByUserId(Integer userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public String updateOrderStatus(Integer orderId, Integer status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
            return "✅ 订单状态已更新";
        }
        return "❌ 订单不存在";
    }

}
