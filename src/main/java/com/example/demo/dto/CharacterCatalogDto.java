package com.example.demo.dto;

public class CharacterCatalogDto {
    private int characterId;
    private String characterName;
    private String image;
    private String availability;
    private String explanation;
    private boolean isOwned;

    // getter / setter
    public int getCharacterId() { return characterId; }
    public void setCharacterId(int characterId) { this.characterId = characterId; }
    public String getCharacterName() { return characterName; }
    public void setCharacterName(String characterName) { this.characterName = characterName; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
    public boolean isOwned() { return isOwned; }
    public void setOwned(boolean owned) { isOwned = owned; }
}
