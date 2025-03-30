package com.iths.mianshop.service;

import com.iths.mianshop.pojo.Item;

import java.util.List;

public interface ItemService {
    List<Item> getAllItems();

    List<Item> getItemsByCategory(String category);

    String addItem(Item item);

    List<Item> getItemsByName(String name); // 🔥 新增方法

    Item getItemById(Integer id);
}
