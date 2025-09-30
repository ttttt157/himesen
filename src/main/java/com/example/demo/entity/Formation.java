package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "formation")
public class Formation {

    @Id
    private int formationid;

    private String playerid;
    private Integer characterid1;
    private Integer characterid2;
    private Integer characterid3;
    private Integer characterid4;
    private Integer characterid5;
    private Integer characterid6;
    private String formationname;

    // getter / setter
    public int getFormationid() { return formationid; }
    public void setFormationid(int formationid) { this.formationid = formationid; }

    public String getPlayerid() { return playerid; }
    public void setPlayerid(String playerid) { this.playerid = playerid; }

    public Integer getCharacterid1() { return characterid1; }
    public void setCharacterid1(Integer characterid1) { this.characterid1 = characterid1; }

    public Integer getCharacterid2() { return characterid2; }
    public void setCharacterid2(Integer characterid2) { this.characterid2 = characterid2; }

    public Integer getCharacterid3() { return characterid3; }
    public void setCharacterid3(Integer characterid3) { this.characterid3 = characterid3; }

    public Integer getCharacterid4() { return characterid4; }
    public void setCharacterid4(Integer characterid4) { this.characterid4 = characterid4; }

    public Integer getCharacterid5() { return characterid5; }
    public void setCharacterid5(Integer characterid5) { this.characterid5 = characterid5; }

    public Integer getCharacterid6() { return characterid6; }
    public void setCharacterid6(Integer characterid6) { this.characterid6 = characterid6; }

    public String getFormationname() { return formationname; }
    public void setFormationname(String formationname) { this.formationname = formationname; }
}
