package com.example.demo.dto;

public class ItemCatalogDto {
    private Integer itemId;
    private String itemName;
    private String image;       // 一覧カード用
    private String explanation; // itempicture に入れる説明文
    private boolean owned;

    // ===== Getter / Setter =====
    public Integer getItemId() { return itemId; }
    public void setItemId(Integer itemId) { this.itemId = itemId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public boolean isOwned() { return owned; }
    public void setOwned(boolean owned) { this.owned = owned; }
}
