package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "characters")
public class Character {

    @Id
    private int characterid;

    private String charactername;
    private int fightstyle;
    private String affiliation;
    private String explanation;
    private String characterpicture;
    private int initialhp;
    private int initialstrength;
    private int charactertype;
    private int useresource;
    private int usemoney;
    private int usefood;
    private String cutinpicture;
    private int totalexp;
    private int level;
    private String giveline;
    private String line1;
    private String line2;
    private String line3;
    private String line4;
    private String line5;
    private String availability;
    private int demolitionresource;
    private int demolitionmoney;
    private int demolitionfood;

    // getter / setter 省略（必要に応じて生成）
    public int getCharacterid() { return characterid; }
    public void setCharacterid(int characterid) { this.characterid = characterid; }

    public String getCharacterpicture() { return characterpicture; }
    public void setCharacterpicture(String characterpicture) { this.characterpicture = characterpicture; }
}
