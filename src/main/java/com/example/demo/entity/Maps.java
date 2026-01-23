package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="maps")
public class Maps {
    @Id
    private int mapid;
    private String mapname;
    private String enemy1;
    private String enemy2;
    private String enemy3;
    private String enemy4;
    private String enemy5;
    private String enemy6;
    private int giveresource;
    private int givemoney;
    private int givefood;
    private String giveitem;
    private long exp;
	public int getMapid() {
		return mapid;
	}
	public void setMapid(int mapid) {
		this.mapid = mapid;
	}
	public String getMapname() {
		return mapname;
	}
	public void setMapname(String mapname) {
		this.mapname = mapname;
	}
	public String getEnemy1() {
		return enemy1;
	}
	public void setEnemy1(String enemy1) {
		this.enemy1 = enemy1;
	}
	public String getEnemy2() {
		return enemy2;
	}
	public void setEnemy2(String enemy2) {
		this.enemy2 = enemy2;
	}
	public String getEnemy3() {
		return enemy3;
	}
	public void setEnemy3(String enemy3) {
		this.enemy3 = enemy3;
	}
	public String getEnemy4() {
		return enemy4;
	}
	public void setEnemy4(String enemy4) {
		this.enemy4 = enemy4;
	}
	public String getEnemy5() {
		return enemy5;
	}
	public void setEnemy5(String enemy5) {
		this.enemy5 = enemy5;
	}
	public String getEnemy6() {
		return enemy6;
	}
	public void setEnemy6(String enemy6) {
		this.enemy6 = enemy6;
	}
	public int getGiveresource() {
		return giveresource;
	}
	public void setGiveresource(int giveresource) {
		this.giveresource = giveresource;
	}
	public int getGivemoney() {
		return givemoney;
	}
	public void setGivemoney(int givemoney) {
		this.givemoney = givemoney;
	}
	public int getGivefood() {
		return givefood;
	}
	public void setGivefood(int givefood) {
		this.givefood = givefood;
	}
	public String getGiveitem() {
		return giveitem;
	}
	public void setGiveitem(String giveitem) {
		this.giveitem = giveitem;
	}
	public long getExp() {
		return exp;
	}
	public void setExp(long exp) {
		this.exp = exp;
	}
    
    // getter/setter
    
}
