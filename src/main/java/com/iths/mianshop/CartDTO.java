package com.iths.mianshop;

public class CartDTO {

    private Integer itemId;
    private Integer quantity;

    public CartDTO() {
    }

    public CartDTO(Integer itemId, Integer quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    /**
     * 获取
     * @return itemId
     */
    public Integer getItemId() {
        return itemId;
    }

    /**
     * 设置
     * @param itemId
     */
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    /**
     * 获取
     * @return quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * 设置
     * @param quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String toString() {
        return "CartDTO{itemId = " + itemId + ", quantity = " + quantity + "}";
    }
}
