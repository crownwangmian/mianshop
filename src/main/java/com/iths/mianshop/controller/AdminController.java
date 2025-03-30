package com.iths.mianshop.controller;

import com.iths.mianshop.pojo.Admin;
import com.iths.mianshop.pojo.Item;
import com.iths.mianshop.pojo.Order;
import com.iths.mianshop.service.AdminService;
import com.iths.mianshop.service.ItemService;
import com.iths.mianshop.service.OrderService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemService itemService;


    // ✅ 1️⃣ 管理员注册接口 (JSON 格式)
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody Admin admin) {
        String result = adminService.addAdmin(admin);
        if (result.equals("✅ 管理员创建成功")) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    // ✅ 2️⃣ 管理员登录接口 (JSON 格式)
//    @PostMapping("/login")
//    @ResponseBody
//    public ResponseEntity<?> login(@RequestBody Admin admin) {
//        String token = adminService.login(admin.getUsername(), admin.getPassword());
//        if (token != null) {
//            Map<String, Object> response = new HashMap<>();
//            response.put("token", token);
//            return ResponseEntity.ok(response);
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ 管理员用户名或密码错误");
//    }
// ✅ 1️⃣ 跳转到管理员登录页
    @GetMapping("/login")
    public String adminLoginPage() {
        return "admin_login"; // 👉 渲染 admin_login.html
    }
    @GetMapping("/dashboard")
//    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard() {
        return "admin";
    }
    @PostMapping("/login")

    public String  login(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletResponse response) {

        String token = adminService.login(username, password);

        if (token != null) {
            // ✅ 将 token 存入 Cookie
            Cookie cookie = new Cookie("Authorization", token);
            cookie.setHttpOnly(true); // 防止 JavaScript 读取
            cookie.setPath("/");      // 设置为整个项目的根路径可访问
            cookie.setMaxAge(60 * 60); // 有效期 1 小时
            response.addCookie(cookie);

            // ✅ 登录成功时，返回成功状态
            return "redirect:/admin/dashboard";
        }

        // ❌ 登录失败，返回状态 401（未授权）

        return "redirect:/admin/login"; // 👉 登录失败，重定向回登录页
    }


    // ✅ 3️⃣ 获取所有订单 (Thymeleaf 渲染)
    @GetMapping("/orders")
    public String getAllOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        if (orders == null || orders.isEmpty()) {
            orders = new ArrayList<>(); // 确保不会为 null
        }
        model.addAttribute("orders", orders);
        return "admin_order";
    }



    // ✅ 4️⃣ 更新订单状态（JSON）
    @PutMapping("/order/update/{orderId}/{status}")
//    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public String updateOrderStatus(
            @PathVariable Integer orderId,
            @PathVariable Integer status) {
        orderService.updateOrderStatus(orderId, status);
        return "✅ 订单状态已更新";
    }

    // ✅ 5️⃣ 删除订单（JSON）
    @DeleteMapping("/order/delete/{orderId}")
//    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public String deleteOrder(@PathVariable Integer orderId) {
        orderService.deleteOrder(orderId);
        return "✅ 订单已删除";
    }

    // ✅ 6️⃣ 添加商品（JSON）
    @PostMapping("/item/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        String result = itemService.addItem(item);

        // ✅ 将成功信息保存到 flash attribute 中
        redirectAttributes.addFlashAttribute("message", result);

        // ✅ 重定向到管理员页面（或其他你想跳转的页面）
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/item/add")
    public String showAddItemPage() {
        return "admin_item"; // admin_item.html 文件名
    }


}
