package com.example.demo.dto;

import java.util.List;
import java.util.Map;

public class BattleSessionData {
    private int mapId;
    private List<CharacterDisplayDto> allies;
    private Map<Integer, Integer> allyHpMap;
    private Map<Integer, Integer> enemyHpMap;
    private java.util.Map<String,Object> reward;
    private List<CharacterDisplayDto> enemies;
    private CharacterDisplayDto dropChar;

    // ★ 戦闘ログ追加
    private List<String> battleLog;

    // ★ 勝敗追加
    private boolean win;
    private boolean lose;

    // ===== getter / setter =====
    public int getMapId() { return mapId; }
    public void setMapId(int mapId) { this.mapId = mapId; }

    public List<CharacterDisplayDto> getAllies() { return allies; }
    public void setAllies(List<CharacterDisplayDto> allies) { this.allies = allies; }

    public Map<Integer, Integer> getAllyHpMap() { return allyHpMap; }
    public void setAllyHpMap(Map<Integer, Integer> allyHpMap) { this.allyHpMap = allyHpMap; }

    public Map<Integer, Integer> getEnemyHpMap() { return enemyHpMap; }
    public void setEnemyHpMap(Map<Integer, Integer> enemyHpMap) { this.enemyHpMap = enemyHpMap; }

    public java.util.Map<String, Object> getReward() { return reward; }
    public void setReward(java.util.Map<String, Object> reward) { this.reward = reward; }

    // ★ getter / setter for battleLog
    public List<String> getBattleLog() { return battleLog; }
    public void setBattleLog(List<String> battleLog) { this.battleLog = battleLog; }

    public List<CharacterDisplayDto> getEnemies() { return enemies; }
    public void setEnemies(List<CharacterDisplayDto> enemies) { this.enemies = enemies; }

    // ★ getter / setter for win/lose
    public boolean getWin() { return win; }
    public void setWin(boolean win) { this.win = win; }

    public boolean getLose() { return lose; }
    public void setLose(boolean lose) { this.lose = lose; }
    
    public CharacterDisplayDto getDropChar() {
        return dropChar;
    }
    public void setDropChar(CharacterDisplayDto dropChar) {
        this.dropChar = dropChar;
    }
}
