package com.example.demo.dto;

import com.example.demo.entity.Character;
import com.example.demo.entity.OwnedCharacter;

public class CharacterDisplayDto {
    private int ownedCharacterId;   // OwnedCharacter.id
    private String characterId;     // 任意の識別用
    private int level;
    private int hitpoint;
    private int strength;
    private boolean rock;
    private String cutinpicture;
    private String name;            // 表示用キャラ名
    private String charactername;   // Characterテーブル由来のキャラ名
    private int levelBeforeBattle;
    private int expBeforeBattle;      // 戦闘前の経験値
    private int expGained = 0;        // 戦闘で獲得した経験値（表示用）
    private int useMoney = 0;         // 戦闘で消費した資源
    private int useFood = 0;
    private int useResource = 0;
    private Integer nextLevelExp;     // 次のレベルに必要な経験値（nullならMAX）
    private String characterpicture;
    private String region;  

    // ===== Getter / Setter =====
    public int getOwnedCharacterId() { return ownedCharacterId; }
    public void setOwnedCharacterId(int ownedCharacterId) { this.ownedCharacterId = ownedCharacterId; }

    public String getCharacterId() { return characterId; }
    public void setCharacterId(String characterId) { this.characterId = characterId; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getHitpoint() { return hitpoint; }
    public void setHitpoint(int hitpoint) { this.hitpoint = hitpoint; }

    public int getStrength() { return strength; }
    public void setStrength(int strength) { this.strength = strength; }

    public boolean isRock() { return rock; }
    public void setRock(boolean rock) { this.rock = rock; }

    public String getCutinpicture() { return cutinpicture; }
    public void setCutinpicture(String cutinpicture) { this.cutinpicture = cutinpicture; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCharactername() { return charactername; }
    public void setCharactername(String charactername) { this.charactername = charactername; }

    public int getLevelBeforeBattle() { return levelBeforeBattle; }
    public void setLevelBeforeBattle(int levelBeforeBattle) { this.levelBeforeBattle = levelBeforeBattle; }

    public int getExpBeforeBattle() { return expBeforeBattle; }
    public void setExpBeforeBattle(int expBeforeBattle) { this.expBeforeBattle = expBeforeBattle; }

    public int getExpGained() { return expGained; }
    public void setExpGained(int expGained) { this.expGained = expGained; }

    public int getUseMoney() { return useMoney; }
    public void setUseMoney(int useMoney) { this.useMoney = useMoney; }

    public int getUseFood() { return useFood; }
    public void setUseFood(int useFood) { this.useFood = useFood; }

    public int getUseResource() { return useResource; }
    public void setUseResource(int useResource) { this.useResource = useResource; }

    public Integer getNextLevelExp() { return nextLevelExp; }
    public void setNextLevelExp(Integer nextLevelExp) { this.nextLevelExp = nextLevelExp; }
    
    public String getCharacterpicture() {  return characterpicture; }
    public void setCharacterpicture(String characterpicture) { this.characterpicture = characterpicture; }
    
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    // ===== コンストラクタ =====
 // OwnedCharacter + Character 情報で生成（味方用）
    public CharacterDisplayDto(OwnedCharacter oc, Character c) {
        this.ownedCharacterId = oc.getId();
        this.characterId = oc.getOwnedcharacter();
        this.level = oc.getLevel();
        this.hitpoint = oc.getHitpoint();
        this.strength = oc.getStrength();
        this.rock = oc.isRock();

        if (c != null) {
            this.cutinpicture = c.getCutinpicture();
            this.name = c.getCharactername();
            this.charactername = c.getCharactername();

            // 戦闘消費資源を DTO にコピー
            this.useMoney = c.getUsemoney();
            this.useFood = c.getUsefood();
            this.useResource = c.getUseresource();

            // region判定
            String aff = c.getAffiliation();
            if (aff != null) {
                this.region = switch (aff) {
                    case "北海道","青森県","岩手県","宮城県","秋田県","山形県","福島県" -> "北海道・東北";
                    case "茨城県","栃木県","群馬県","埼玉県","千葉県","東京都","神奈川県" -> "関東";
                    case "新潟県","富山県","石川県","福井県","山梨県","長野県","岐阜県","静岡県","愛知県" -> "中部";
                    case "三重県","滋賀県","京都府","大阪府","兵庫県","奈良県","和歌山県" -> "近畿";
                    case "鳥取県","島根県","岡山県","広島県","山口県" -> "中国";
                    case "徳島県","香川県","愛媛県","高知県" -> "四国";
                    case "福岡県","佐賀県","長崎県","熊本県","大分県","宮崎県","鹿児島県","沖縄県" -> "九州・沖縄";
                    case "スコットランド","スペイン","ドイツ","フランス","中国" ->"ガチャ";
                    default -> "";
                };
            } else {
                this.region = "";
            }
        } else {
            this.region = "";
        }
    }


    // Character単体用（敵用）
    public CharacterDisplayDto(Character c) {
        if (c != null) {
            this.characterId = String.valueOf(c.getCharacterid());
            this.charactername = c.getCharactername();
            this.cutinpicture = c.getCutinpicture();
            this.level = c.getLevel();
            this.hitpoint = c.getInitialhp();
            this.strength = c.getInitialstrength();
            this.rock = false;
            this.name = c.getCharactername();
            this.useMoney = c.getUsemoney();
            this.useFood = c.getUsefood();
            this.useResource = c.getUseresource();
        }
    }
}
