package com.iths.mianshop.controller;

import com.iths.mianshop.pojo.Cart;
import com.iths.mianshop.pojo.Order;
import com.iths.mianshop.pojo.OrderDetail;
import com.iths.mianshop.pojo.User;
import com.iths.mianshop.service.CartService;
import com.iths.mianshop.service.OrderDetailService;
import com.iths.mianshop.service.OrderService;
import com.iths.mianshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDetailService orderDetailService;


    // ✅ 查看订单详情（使用路径参数）
    @GetMapping("/view/{orderId}")
    public String viewOrder(@PathVariable Integer orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);
            model.addAttribute("orderDetails", orderDetails);
            model.addAttribute("orderTotalFee", order.getTotalFee());
            return "order"; // 渲染到 Thymeleaf 订单详情页
        }
        return "redirect:/user/index";
    }

    @PostMapping("/create")
    public String createOrder(RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        if (user != null) {
            try {
                // 🔥 调用现成的 createOrder() 方法
                Order order = orderService.createOrder(user.getId());

                // ✅ 将订单 ID 传递给视图
                redirectAttributes.addAttribute("orderId", order.getId());
                return "redirect:/order/view/" + order.getId();
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
                return "redirect:/cart/view";
            }
        }

        redirectAttributes.addFlashAttribute("errorMessage", "❌ 用户未登录");
        return "redirect:/user/login";
    }


}
