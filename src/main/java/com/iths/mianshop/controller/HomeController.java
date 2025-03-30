package com.iths.mianshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // 首页跳转
    @GetMapping("/")
    public String home() {
        return "index"; // 对应 src/main/resources/templates/index.html
    }}

    // Admin 登录页
//    @GetMapping("/admin/login")
//    public String adminLogin() {
//        return "admin"; // 指向 admin.html 页面
//    }

    // User 登录页
//    @GetMapping("/user/login")
//    public String userLogin() {
//        return "user"; // 指向 user.html 页面
//    }
//}
