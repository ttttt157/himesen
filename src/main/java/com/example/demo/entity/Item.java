package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "item")
public class Item {

    @Id
    private Integer itemid;

    private String itemname;
    private String itemimage;   // 小さい画像
    private String itempicture; // モーダルで表示する大きい画像

    // ===== Getter / Setter =====
    public Integer getItemid() { return itemid; }
    public void setItemid(Integer itemid) { this.itemid = itemid; }

    public String getItemname() { return itemname; }
    public void setItemname(String itemname) { this.itemname = itemname; }

    public String getItemimage() { return itemimage; }
    public void setItemimage(String itemimage) { this.itemimage = itemimage; }

    public String getItempicture() { return itempicture; }
    public void setItempicture(String itempicture) { this.itempicture = itempicture; }
}
