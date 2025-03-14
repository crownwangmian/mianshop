package com.iths.mianshop.service.impl;

import com.iths.mianshop.pojo.Admin;
import com.iths.mianshop.repository.AdminRepository;
import com.iths.mianshop.service.AdminService;
import com.iths.mianshop.utils.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl  implements AdminService{
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTool jwtTool;

    @Override
    public String addAdmin(Admin admin) {
        // 判断管理员是否已存在
        if (adminRepository.findByUsername(admin.getUsername()) != null) {
            return "❌ 管理员已存在";
        }

        // 加密密码
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminRepository.save(admin);
        return "✅ 管理员创建成功";
    }

    @Override
    public String login(String username, String password) {
        Admin admin = adminRepository.findByUsername(username);

        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
            // 创建 claims（包含用户信息）
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", admin.getUsername());
            claims.put("id", admin.getId());
            claims.put("roles", List.of("ROLE_ADMIN")); // 角色直接写成 ROLE_ADMIN

            // 生成 token
            return jwtTool.generateToken(admin.getUsername(), claims);
        }
        return null;
    }


}
