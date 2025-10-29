package com.travelapp.travel_app.dto;

import com.travelapp.travel_app.model.ItemType;

public class OrderRequest {
    private ItemType itemType;
    private Integer itemId;
    private Integer quantity;
    
   
    public ItemType getItemType() { return itemType; }
    public void setItemType(ItemType itemType) { this.itemType = itemType; }
    
    public Integer getItemId() { return itemId; }
    public void setItemId(Integer itemId) { this.itemId = itemId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}