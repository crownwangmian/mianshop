package com.iths.mianshop.repository;

import com.iths.mianshop.pojo.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByCategory(String category);

    @Query("SELECT i FROM Item i WHERE i.name LIKE %:name%")
    List<Item> findByNameContaining(@Param("name") String name);


}
