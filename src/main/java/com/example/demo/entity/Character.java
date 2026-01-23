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
    private String charactername_kana;
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
    public String getCharactername() {
		return charactername;
	}
	public void setCharactername(String charactername) {
		this.charactername = charactername;
	}
	public String getCharactername_kana() {
	    return charactername_kana;
	}

	public void setCharactername_kana(String charactername_kana) {
	    this.charactername_kana = charactername_kana;
	}

	public int getFightstyle() {
		return fightstyle;
	}
	public void setFightstyle(int fightstyle) {
		this.fightstyle = fightstyle;
	}
	public String getAffiliation() {
		return affiliation;
	}
	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}
	public String getExplanation() {
		return explanation;
	}
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	public int getInitialhp() {
		return initialhp;
	}
	public void setInitialhp(int initialhp) {
		this.initialhp = initialhp;
	}
	public int getInitialstrength() {
		return initialstrength;
	}
	public void setInitialstrength(int initialstrength) {
		this.initialstrength = initialstrength;
	}
	public int getCharactertype() {
		return charactertype;
	}
	public void setCharactertype(int charactertype) {
		this.charactertype = charactertype;
	}
	public int getUseresource() {
		return useresource;
	}
	public void setUseresource(int useresource) {
		this.useresource = useresource;
	}
	public int getUsemoney() {
		return usemoney;
	}
	public void setUsemoney(int usemoney) {
		this.usemoney = usemoney;
	}
	public int getUsefood() {
		return usefood;
	}
	public void setUsefood(int usefood) {
		this.usefood = usefood;
	}
	public String getCutinpicture() {
		return cutinpicture;
	}
	public void setCutinpicture(String cutinpicture) {
		this.cutinpicture = cutinpicture;
	}
	public int getTotalexp() {
		return totalexp;
	}
	public void setTotalexp(int totalexp) {
		this.totalexp = totalexp;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getGiveline() {
		return giveline;
	}
	public void setGiveline(String giveline) {
		this.giveline = giveline;
	}
	public String getAvailability() {
		return availability;
	}
	public void setAvailability(String availability) {
		this.availability = availability;
	}
	public int getDemolitionresource() {
		return demolitionresource;
	}
	public void setDemolitionresource(int demolitionresource) {
		this.demolitionresource = demolitionresource;
	}
	public int getDemolitionmoney() {
		return demolitionmoney;
	}
	public void setDemolitionmoney(int demolitionmoney) {
		this.demolitionmoney = demolitionmoney;
	}
	public int getDemolitionfood() {
		return demolitionfood;
	}
	public void setDemolitionfood(int demolitionfood) {
		this.demolitionfood = demolitionfood;
	}
	private String availability;
    private int demolitionresource;
    private int demolitionmoney;
    private int demolitionfood;

    // getter / setter 省略（必要に応じて生成）
    public int getCharacterid() { return characterid; }
    public void setCharacterid(int characterid) { this.characterid = characterid; }

    public String getCharacterpicture() { return characterpicture; }
    public void setCharacterpicture(String characterpicture) { this.characterpicture = characterpicture; }
    
    public String getLine1() { return line1; }
    public void setLine1(String line1) { this.line1 = line1; }

    public String getLine2() { return line2; }
    public void setLine2(String line2) { this.line2 = line2; }

    public String getLine3() { return line3; }
    public void setLine3(String line3) { this.line3 = line3; }

    public String getLine4() { return line4; }
    public void setLine4(String line4) { this.line4 = line4; }

    public String getLine5() { return line5; }
    public void setLine5(String line5) { this.line5 = line5; }

}
