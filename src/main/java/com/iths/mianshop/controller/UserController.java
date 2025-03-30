package com.iths.mianshop.controller;


import com.iths.mianshop.pojo.User;
import com.iths.mianshop.service.ItemService;
import com.iths.mianshop.service.UserService;
import com.iths.mianshop.utils.JwtTool;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private JwtTool jwtTool;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    //    @PostMapping("/register")
//    public String register(@RequestBody User user) {
//        return userService.register(user);
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody User user) {
//        String token = userService.login(user.getUsername(), user.getPassword());
//        if (token != null) {
//            Map<String, Object> response = new HashMap<>();
//            response.put("token", token);
//            return ResponseEntity.ok(response);
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ 用户名或密码错误");
//    }
    @GetMapping("/register")
    public String registerPage() {
        return "user_register"; // 渲染 user_register.html
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password, HttpServletResponse response,
                           RedirectAttributes redirectAttributes) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        // 调用注册逻辑
        String token = userService.register(user);

        if (token != null && !token.contains("⚠️")) {
            // ✅ 注册成功，直接设置 JWT
            Cookie cookie = new Cookie("Authorization", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24); // 1天有效期
            response.addCookie(cookie);

            // ✅ 跳转到首页，显示当前登录状态
            return "redirect:/user/index";
        }

        // ❌ 注册失败，显示提示信息
        redirectAttributes.addFlashAttribute("errorMessage", token);
        return "redirect:/user/register";
    }


    @GetMapping("/login")
    public String loginPage() {
        return "user_login"; // 渲染 user_login.html
    }


    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletResponse response,
                        RedirectAttributes redirectAttributes) {
        // ✅ 调用 UserService 里的 login 方法
        String token = userService.login(username, password);
        if (token != null) {
            // ✅ 成功生成 JWT，将 Token 设置在 HttpOnly Cookie 里
            Cookie cookie = new Cookie("Authorization", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24); // 有效期 1 天
            response.addCookie(cookie);

            // ✅ 登录成功，重定向到首页
            return "redirect:/user/index";
        }

        // ❌ 登录失败，跳转回登录页
        redirectAttributes.addFlashAttribute("errorMessage", "❌ 用户名或密码错误");
        return "redirect:/user/login";
    }

    //    @GetMapping("/index")
//    public String userIndex(Model model) {
//        List<Item> itemList = itemService.getAllItems();
//        itemList.forEach(item -> {
//            System.out.println("ID: " + item.getId());
//            System.out.println("名称: " + item.getName());
//            System.out.println("价格: " + item.getPrice());
//            System.out.println("库存: " + item.getStock());
//            System.out.println("图片: " + item.getImage());
//            System.out.println("品牌: " + item.getBrand());
//            System.out.println("类别: " + item.getCategory());
//            System.out.println("规格: " + item.getSpecification());
//            System.out.println("---------------------");
//        });
//        model.addAttribute("items", itemList);
//        return "user_index"; // 这个应该和 HTML 文件名一致
//    }
    @GetMapping("/index")
    public String userIndex(Model model) {
        String username = "未登录"; // 默认用户名

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // ✅ 判断是否登录
        if (auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getPrincipal())) {

            // 👇 从 principal 拆解出 "mianwang|USER" 或 "mianwang|ADMIN"
            String fullPrincipal = auth.getName();

            if (fullPrincipal.contains("|")) {
                String[] parts = fullPrincipal.split("\\|");
                username = parts[0]; // 取出 mianwang
                String userType = parts[1];

                // ✅ 拒绝 ADMIN 用户访问用户首页（可选）
                if ("ADMIN".equals(userType)) {
                    return "redirect:/admin/dashboard"; // 或跳转到提示页
                }
            }
        }

        model.addAttribute("username", username);
        return "user_index";
    }


    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // ✅ 删除 Cookie
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 设置为 0 立即失效
        response.addCookie(cookie);

        // ✅ 重定向到首页
        return "redirect:/user/index";
    }


}
