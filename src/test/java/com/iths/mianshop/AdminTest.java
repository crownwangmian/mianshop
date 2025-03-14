package com.iths.mianshop;

import com.iths.mianshop.pojo.Admin;
import com.iths.mianshop.repository.AdminRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdminTest {
    @Autowired
    private AdminRepository adminRepository;

    @Test
    void testAddAdmin() {
        String username = "tom";
        if (adminRepository.findByUsername(username) == null) {
            Admin admin = new Admin();
            admin.setUsername(username);
            admin.setPassword("123456");
            adminRepository.save(admin);
            System.out.println("✅ 管理员创建成功！");
        } else {
            System.out.println("⚠️ 用户名已存在，创建失败");
        }
    }

}
