package com.iths.mianshop.filter;

import com.iths.mianshop.utils.JwtTool;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

        // ✅ 1. 从 Cookie 中获取 token
        String token = null;
        Cookie[] cookies = request.getCookies(); // 从 Cookie 中读取
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Authorization".equals(cookie.getName())) { // 匹配名为 Authorization 的 Cookie
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null) {
            try {
                Claims claims = jwtTool.parseToken(token);
                if (claims != null) {
                    String username = claims.getSubject();

                    // 日志输出，方便排查问题
                    if (username == null) {
                        logger.warn("解析出的用户名为空");
                        filterChain.doFilter(request, response);
                        return;
                    }

                    logger.info("解析出的用户名: " + username);

                    // 👇 提取角色信息
                    List<String> roles = claims.get("roles", List.class);
                    logger.info("解析出的用户名: " + roles);
                    if (roles == null) {
                        roles = new ArrayList<>();
                    }
                    List<GrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    String userType = claims.get("userType", String.class); // 👈 取出 userType

// 用 username + userType 拼成唯一标识，比如 "mianwang|ADMIN"
                    String principal = username + "|" + userType;

                    // 👇 创建认证对象，存入 SecurityContext
                    Authentication authentication =
                            new UsernamePasswordAuthenticationToken(principal, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                logger.warn("Token 解析失败：" + e.getMessage());
            }
        }


        // ✅ 6. 继续处理下一个过滤器
        filterChain.doFilter(request, response);
    }
}
