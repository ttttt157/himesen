package com.example.demo.dto;

public class GachaResultDto {
    private String characterName;
    private int level;
    private int hitpoint;
    private int strength;
    private String characterPicture;
    private String affiliation;

    public String getCharacterName() { return characterName; }
    public void setCharacterName(String characterName) { this.characterName = characterName; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getHitpoint() { return hitpoint; }
    public void setHitpoint(int hitpoint) { this.hitpoint = hitpoint; }
    public int getStrength() { return strength; }
    public void setStrength(int strength) { this.strength = strength; }
    public String getCharacterPicture() { return characterPicture; }
    public void setCharacterPicture(String characterPicture) { this.characterPicture = characterPicture; }
    public String getAffiliation() { return affiliation; }
    public void setAffiliation(String affiliation) { this.affiliation = affiliation; }
}
