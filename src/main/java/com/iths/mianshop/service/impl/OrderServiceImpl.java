package com.iths.mianshop.service.impl;

import com.iths.mianshop.pojo.Cart;
import com.iths.mianshop.pojo.Order;
import com.iths.mianshop.pojo.OrderDetail;
import com.iths.mianshop.repository.CartRepository;
import com.iths.mianshop.repository.OrderDetailRepository;
import com.iths.mianshop.repository.OrderRepository;
import com.iths.mianshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        for (Order order : orders) {
            List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(order.getId());
            order.setOrderDetails(orderDetails);
        }
        return orders;
    }

    @Override
    public List<Order> getOrdersByStatus(Integer status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    public void updateOrderStatus(Integer orderId, Integer status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        order.setStatus(status);
        orderRepository.save(order);
    }


    @Override
    public Order createOrder(Integer userId) {
        // 1️⃣ 获取购物车明细
        List<Cart> cartList = cartRepository.findByUserId(userId);
        if (cartList.isEmpty()) {
            throw new RuntimeException("购物车为空，无法生成订单");
        }

        // 2️⃣ 生成订单对象
        Order order = new Order();
        order.setUser(cartList.get(0).getUser());
        order.setCreateTime(LocalDateTime.now());
        order.setStatus(0); // 0 = 未处理
        int totalFee = 0;
        orderRepository.save(order);

        // 3️⃣ 创建订单详情 + 统计总价
        for (Cart cart : cartList) {
            // 生成订单详情
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setItem(cart.getItem());
            orderDetail.setQuantity(cart.getQuantity());
            orderDetail.setName(cart.getItem().getName());
            orderDetail.setSpec(cart.getItem().getSpecification());
            orderDetail.setPrice(cart.getItem().getPrice());
            orderDetail.setCreateTime(LocalDateTime.now());
            orderDetailRepository.save(orderDetail);

            // 统计总价
            totalFee += cart.getQuantity() * cart.getItem().getPrice();
        }

        // 4️⃣ 保存订单
        order.setTotalFee(totalFee);
        orderRepository.save(order);

        // 5️⃣ 清空购物车
        cartRepository.deleteAll(cartList);

        return order;
    }

    @Override
    public Order getOrderById(Integer orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("订单不存在"));
    }

    @Override
    public List<Order> getOrdersByUserId(Integer userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public void deleteOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        orderRepository.delete(order);
    }


}
