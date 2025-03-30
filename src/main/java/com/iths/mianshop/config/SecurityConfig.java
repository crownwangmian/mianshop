package com.iths.mianshop.config;

import com.iths.mianshop.filter.JwtAuthenticationFilter;
import com.iths.mianshop.utils.JwtTool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.KeyStoreKeyFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.KeyPair;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 使用 BCrypt 进行密码加密
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public KeyPair keyPair(JwtProperties properties) {
        // 获取秘钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
                new ClassPathResource(properties.getLocation()),
                properties.getPassword().toCharArray()
        );

        // 读取秘钥对
        return keyStoreKeyFactory.getKeyPair(
                properties.getAlias(),
                properties.getPassword().toCharArray()
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTool jwtTool) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // ✅ 允许匿名访问的路径
                        .requestMatchers(
                                "/user/login",
                                "/user/register",
                                "/user/index",
                                "/admin/login",
                                "/item/**",
                                "/"
                        ).permitAll()

                        // ✅ 用户接口 -> 需要 USER 或 ADMIN 角色
                        .requestMatchers("/user/**").hasAuthority("ROLE_USER")

                        // ✅ 管理接口 -> 需要 ADMIN 角色
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")

                        // ✅ 其他请求都需要认证
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTool), UsernamePasswordAuthenticationFilter.class)
                .build();
    }


}
