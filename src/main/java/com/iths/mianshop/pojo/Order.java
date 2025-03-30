package com.iths.mianshop.pojo;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "total_fee", nullable = false)
    private Integer totalFee;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ✅ 关联 OrderDetail（订单明细）
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    // 👉 无参构造器（JPA 必须）
    public Order() {}

    // 👉 带参构造器（用于创建订单）
    public Order(Integer totalFee, User user, Integer status) {
        this.totalFee = totalFee;
        this.user = user;
        this.status = status;
        this.createTime = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

}
