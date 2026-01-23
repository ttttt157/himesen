package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "level_experience")
public class LevelExperience {

    @Id
    private Integer level; // 主キー

    private Integer requiredExp; // 累計経験値

    // デフォルトコンストラクタ
    public LevelExperience() {}

    // コンストラクタ
    public LevelExperience(Integer level, Integer requiredExp) {
        this.level = level;
        this.requiredExp = requiredExp;
    }

    // getter / setter
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getRequiredExp() {
        return requiredExp;
    }

    public void setRequiredExp(Integer requiredExp) {
        this.requiredExp = requiredExp;
    }

}
