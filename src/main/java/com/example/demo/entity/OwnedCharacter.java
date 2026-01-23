package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ownedcharacter")
public class OwnedCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String ownedcharacter;
    private int level;
    private int charactertype;
    private int hitpoint;
    private int strength;
    private boolean rock;
    private int experience;

    // ===== Getter / Setter =====
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getOwnedcharacter() { return ownedcharacter; }
    public void setOwnedcharacter(String ownedcharacter) { this.ownedcharacter = ownedcharacter; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getCharactertype() { return charactertype; }
    public void setCharactertype(int charactertype) { this.charactertype = charactertype; }

    public int getHitpoint() { return hitpoint; }
    public void setHitpoint(int hitpoint) { this.hitpoint = hitpoint; }

    public int getStrength() { return strength; }
    public void setStrength(int strength) { this.strength = strength; }

    public boolean isRock() { return rock; }
    public void setRock(boolean rock) { this.rock = rock; }

    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }
}
