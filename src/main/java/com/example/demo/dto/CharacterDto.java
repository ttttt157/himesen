package com.example.demo.dto;

import com.example.demo.entity.Character;

public class CharacterDto {
    private int characterId;
    private String characterName;
    private int fightStyle;
    private String affiliation;
    private String explanation;
    private String characterPicture;
    private int initialHp;
    private int initialStrength;
    private int charactertype;
    private String characterNameKana;


    // 追加：残り資源
    private int remainingFood;
    private int remainingFund;
    private int remainingMaterial;

    public CharacterDto(Character c) {
        this.characterId = c.getCharacterid();
        this.characterName = c.getCharactername();
        this.characterNameKana = c.getCharactername_kana(); // ★追加
        this.fightStyle = c.getFightstyle();
        this.affiliation = c.getAffiliation();
        this.explanation = c.getExplanation();
        this.characterPicture = "/img/" + c.getCharacterpicture();
        this.initialHp = c.getInitialhp();
        this.initialStrength = c.getInitialstrength();
        this.charactertype = c.getCharactertype();
    }
    


    // ゲッター
    public int getCharacterId() { return characterId; }
    public String getCharacterName() { return characterName; }
    public int getFightStyle() { return fightStyle; }
    public String getAffiliation() { return affiliation; }
    public String getExplanation() { return explanation; }
    public String getCharacterPicture() { return characterPicture; }
    public int getInitialHp() { return initialHp; }
    public int getInitialStrength() { return initialStrength; }
    public int getCharactertype() { return charactertype; }
    public String getCharacterNameKana() { return characterNameKana; }


    // 残り資源ゲッター／セッター
    public int getRemainingFood() { return remainingFood; }
    public void setRemainingFood(int remainingFood) { this.remainingFood = remainingFood; }
    public int getRemainingFund() { return remainingFund; }
    public void setRemainingFund(int remainingFund) { this.remainingFund = remainingFund; }
    public int getRemainingMaterial() { return remainingMaterial; }
    public void setRemainingMaterial(int remainingMaterial) { this.remainingMaterial = remainingMaterial; }
}
