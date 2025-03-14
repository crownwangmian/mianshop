package com.iths.mianshop.service.impl;

import com.iths.mianshop.pojo.Item;
import com.iths.mianshop.repository.ItemRepository;
import com.iths.mianshop.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public List<Item> getItemsByCategory(String category) {
        return itemRepository.findByCategory(category);
    }

    @Override
    public String addItem(Item item) {
        itemRepository.save(item);
        return "✅ 商品添加成功";
    }

}
