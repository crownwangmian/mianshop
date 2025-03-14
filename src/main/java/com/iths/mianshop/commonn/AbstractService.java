package com.iths.mianshop.commonn;

import com.iths.mianshop.pojo.User;
import com.iths.mianshop.repository.UserRepository;
import com.iths.mianshop.utils.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractService<T> {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected JwtTool jwtTool;
    protected  String login(String username, String password, String role, UserRepository repository) {
        User user = repository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", user.getUsername());
            claims.put("id", user.getId());
            claims.put("role", "ROLE_" + role.toUpperCase());

            List<String> roles = List.of("ROLE_" + role.toUpperCase());
            return jwtTool.generateToken(user.getUsername(), claims);
        }
        return null;
    }
    protected abstract String getUsername(T user);
    protected abstract String getPassword(T user);
    protected abstract Integer getId(T user);

}
