package com.iths.mianshop.pojo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_detail")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "spec")
    private String spec;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    // 👉 无参构造器（JPA 需要）
    public OrderDetail() {}

    // 👉 带参构造器（创建订单时使用）
    public OrderDetail(Order order, Item item, Integer quantity, String name, String spec, Integer price) {
        this.order = order;
        this.item = item;
        this.quantity = quantity;
        this.name = name;
        this.spec = spec;
        this.price = price;
        this.createTime = LocalDateTime.now();
    }

    // ✅ Getter 和 Setter


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }


}
