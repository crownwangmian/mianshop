package com.iths.mianshop.service;

import com.iths.mianshop.pojo.User;

public interface UserService {
    String register(User user);

    String login(String username, String password);

    // ✅ 新增方法：通过 token 解析出 username，然后查询用户
    User getUserByToken(String token);

    User getUserByUsername(String username);



}
