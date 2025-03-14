package com.iths.mianshop.service.impl;

import com.iths.mianshop.pojo.User;
import com.iths.mianshop.repository.UserRepository;
import com.iths.mianshop.service.UserService;
import com.iths.mianshop.utils.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTool jwtTool;

    @Override
    public String register(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return "⚠️ 用户名已存在！";
        }

        // 2.使用 BCrypt 进行密码加密
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        // 3.保存用户信息
        userRepository.save(user);

        return "✅ 注册成功！";
    }

    @Override
    public String login(String username, String password) {

        User user = userRepository.findByUsername(username);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // 创建 claims（包含用户信息）
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", user.getUsername());
            claims.put("id", user.getId());
            claims.put("roles", List.of("ROLE_USER")); // 将角色信息加入 claims
            // 生成 token
            String token = jwtTool.generateToken(user.getUsername(), claims);
            return token;
        }
        return null;
    }




}
