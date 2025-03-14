package com.iths.mianshop.controller;

import com.iths.mianshop.pojo.Admin;
import com.iths.mianshop.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    // 管理员注册接口
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Admin admin) {
        String result = adminService.addAdmin(admin);
        if (result.equals("✅ 管理员创建成功")) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    // 管理员登录接口
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Admin admin) {
        String token = adminService.login(admin.getUsername(), admin.getPassword());
        if (token != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ 管理员用户名或密码错误");
    }

}
