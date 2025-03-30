package com.iths.mianshop.service;

import com.iths.mianshop.pojo.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    // 根据订单ID查询订单详情
    List<OrderDetail> getOrderDetailsByOrderId(Integer orderId);

    // 添加订单详情
    void saveOrderDetail(OrderDetail orderDetail);
}
