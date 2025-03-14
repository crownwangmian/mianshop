package com.iths.mianshop.service;

import com.iths.mianshop.pojo.Admin;

public interface AdminService {
    String addAdmin(Admin admin);

    String login(String username, String password);

}
