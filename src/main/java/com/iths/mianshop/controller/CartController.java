package com.iths.mianshop.controller;

import com.iths.mianshop.pojo.Cart;
import com.iths.mianshop.pojo.Item;
import com.iths.mianshop.pojo.User;
import com.iths.mianshop.service.CartService;
import com.iths.mianshop.service.ItemService;
import com.iths.mianshop.service.UserService;
import com.iths.mianshop.utils.AuthUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    // ✅ 添加商品到购物车
    @PostMapping("/add")
    public String addToCart(@RequestParam Integer itemId,
                            @RequestParam Integer quantity,
                            RedirectAttributes redirectAttributes) {

        String username = AuthUtils.getCurrentUsername();
        User user = userService.getUserByUsername(username);

        if (user != null) {
            Item item = itemService.getItemById(itemId);
            if (item != null) {
                Cart cart = new Cart(user, item, quantity);
                String result = cartService.addToCart(cart);
                redirectAttributes.addFlashAttribute("successMessage", result);
                return "redirect:/cart/view";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "❌ 商品不存在");
                return "redirect:/user/index";
            }
        }

        redirectAttributes.addFlashAttribute("errorMessage", "❌ 用户未登录");
        return "redirect:/user/login";
    }

    // ✅ 查看购物车页面
    @GetMapping("/view")
    public String viewCart(HttpServletRequest request, Model model) {
        String username = "未登录";
        List<Cart> cartList = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Authorization".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (token != null) {
                        User user = userService.getUserByToken(token);
                        if (user != null) {
                            username = user.getUsername();
                            cartList = cartService.getCartByUserId(user.getId());
                        }
                    }
                }
            }
        }

        model.addAttribute("username", username);
        model.addAttribute("cartList", cartList);
        model.addAttribute("totalPrice", calculateTotalPrice(cartList));
        return "cart";
    }

    // ✅ 返回购物车 JSON 数据
    @GetMapping("/list")
    @ResponseBody
    public List<Cart> getCartByUserId() {
        String username = AuthUtils.getCurrentUsername();
        User user = userService.getUserByUsername(username);
        if (user != null) {
            return cartService.getCartByUserId(user.getId());
        }
        return null;
    }

    // ✅ 总价计算
    private double calculateTotalPrice(List<Cart> cartList) {
        if (cartList == null) return 0.0;
        return cartList.stream()
                .mapToDouble(cart -> cart.getItem().getPrice() * cart.getQuantity())
                .sum();
    }

    // ✅ 修改数量
    @PostMapping("/update/{cartId}")
    public String updateCartQuantity(@PathVariable Integer cartId, @RequestParam Integer quantity) {
        cartService.updateCartQuantity(cartId, quantity);
        return "redirect:/cart/view";
    }

    // ✅ 删除商品
    @PostMapping("/remove/{cartId}")
    public String removeFromCart(@PathVariable Integer cartId) {
        cartService.removeFromCart(cartId);
        return "redirect:/cart/view";
    }

    // ✅ 清空购物车
    @PostMapping("/clear")
    public String clearCart() {
        String username = AuthUtils.getCurrentUsername();
        User user = userService.getUserByUsername(username);
        if (user != null) {
            cartService.clearCart(user.getId());
        }
        return "redirect:/cart/view";
    }
}
