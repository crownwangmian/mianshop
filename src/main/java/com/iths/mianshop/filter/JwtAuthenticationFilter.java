package com.iths.mianshop.filter;

import com.iths.mianshop.utils.JwtTool;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTool jwtTool;

    public JwtAuthenticationFilter(JwtTool jwtTool) {
        this.jwtTool = jwtTool;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. 从请求头中获取 token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 2. 解析 token
            Claims claims = jwtTool.parseToken(token);
            if (claims != null) {
                String username = claims.getSubject();

                // 👇 提取角色信息
                List<String> roles = claims.get("roles", List.class);
                if (roles == null) {
                    roles = new ArrayList<>();
                }
                List<GrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // 创建认证对象，带上权限
                // 直接创建认证对象，不需要 password
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                // 👇 将认证对象存储到 SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.warn("Token 解析失败：" + e.getMessage());
        }

        // 5. 继续处理下一个过滤器
        filterChain.doFilter(request, response);
    }

}
