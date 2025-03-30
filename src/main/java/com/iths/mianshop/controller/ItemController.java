package com.iths.mianshop.controller;

import com.iths.mianshop.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.iths.mianshop.pojo.Item;

import java.util.List;


@RestController
@RequestMapping("/item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @GetMapping("/category/{category}")
    public List<Item> getItemsByCategory(@PathVariable String category) {
        System.out.println("查询分类：" + category);
        return itemService.getItemsByCategory(category.trim());
    }
    @GetMapping("/all")
    public List<Item> getAllItems() {
        System.out.println("查询所有商品");
        return itemService.getAllItems();
    }

    @GetMapping("/search/{name}")
    public List<Item> searchItemsByName(@PathVariable String name) {
        return itemService.getItemsByName(name.trim());
    }

}
