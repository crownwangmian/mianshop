package com.iths.mianshop.service;

import com.iths.mianshop.pojo.User;

public interface UserService {
    String register(User user);

    String login(String username, String password);



}
