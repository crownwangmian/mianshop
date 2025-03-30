package com.iths.mianshop.service.impl;





import com.iths.mianshop.pojo.User;
import com.iths.mianshop.repository.UserRepository;
import com.iths.mianshop.service.UserService;
import com.iths.mianshop.utils.JwtTool;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTool jwtTool;


    // ✅ 新增方法：通过 token 解析出 username，然后查询用户
    @Override
    public User getUserByToken(String token) {
        Claims claims = jwtTool.parseToken(token);
        if (claims != null) {
            String username = claims.getSubject(); // 从 token 中解析出 username
            log.info("解析出的用户名: " + username);
            if (username != null) {
                return getUserByUsername(username);
            }
        }
        return null;
    }

    @Override

    public User getUserByUsername(String username) {
        // 👇 如果用户名中带了 |USER 或 |ADMIN 之类的后缀，先拆出来
        if (username.contains("|")) {
            username = username.split("\\|")[0];
        }

        log.info("正在通过用户名查询用户: " + username);
        User user = userRepository.findByUsername(username);

        if (user == null) {
            log.warn("用户不存在: " + username);
        }

        return user;
    }

    @Override
    public String register(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return "⚠️ 用户名已存在！";
        }

        // 2. 使用 BCrypt 进行密码加密
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        // 3. 保存用户信息
        userRepository.save(user);

        // ✅ 注册成功后直接生成 JWT
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("id", user.getId());
        claims.put("userType", "USER"); // 👈 新增这一行
        claims.put("roles", List.of("ROLE_USER"));

        // 生成 token
        String token = jwtTool.generateToken(user.getUsername(), claims);

        return token; // 直接返回 JWT
    }

    @Override
    public String login(String username, String password) {

        User user = userRepository.findByUsername(username);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            log.info("用户存在，密码匹配成功");

            Map<String, Object> claims = new HashMap<>();
            claims.put("username", user.getUsername());
            claims.put("id", user.getId());
            claims.put("userType", "USER"); // 👈 新增这一行
            claims.put("roles", List.of("ROLE_USER"));

            String token = jwtTool.generateToken(user.getUsername(), claims);
            if (token != null) {
                log.info("生成的 JWT: {}", token);

                // ✅ 设置 SecurityContext
                String principal = user.getUsername() + "|USER";
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        principal, null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);

                return token;
            }
        }
        return null;
    }


}
