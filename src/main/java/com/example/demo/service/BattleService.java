package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.BattleSessionData;
import com.example.demo.dto.CharacterDisplayDto;
import com.example.demo.entity.Character;
import com.example.demo.entity.LevelExperience;
import com.example.demo.entity.Maps;
import com.example.demo.entity.OwnedCharacter;
import com.example.demo.entity.UserCharacterStamp;
import com.example.demo.entity.UserItem;
import com.example.demo.entity.user;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.repository.LevelExperienceRepository;
import com.example.demo.repository.MapsRepository;
import com.example.demo.repository.OwnedCharacterRepository;
import com.example.demo.repository.UserCharacterStampRepository;
import com.example.demo.repository.UserItemRepository;
import com.example.demo.repository.UserRepository;


@Service
public class BattleService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OwnedCharacterRepository ownedCharacterRepository;

    @Autowired
    private UserItemRepository userItemRepository;

    @Autowired
    private MapsRepository mapRepository;

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private LevelExperienceRepository levelExperienceRepository;
    
    @Autowired
    private UserCharacterStampRepository userCharacterStampRepository;

    private static final double ITEM_DROP_RATE = 0.1;
    private static final double CHARACTER_DROP_RATE = 0.1;

    /**
     * バトル計算・報酬決定
     */
    public Map<String, Object> calculateBattle(String username, BattleSessionData battleData) {
        Map<String, Object> reward = new HashMap<>();
        if (battleData == null) return reward;

        Integer mapId = battleData.getMapId();
        if (mapId == null) return reward;

        Maps mapEntity = mapRepository.findById(mapId).orElse(null);
        if (mapEntity == null) return reward;

        List<CharacterDisplayDto> allies = battleData.getAllies();

        // --- 戦闘前の経験値を保存 ---
        for (CharacterDisplayDto ally : allies) {
            ally.setExpBeforeBattle(ally.getLevel() * 100 + ally.getExpGained());
            ally.setCutinpicture("/img/" + ally.getOwnedCharacterId() + ".png");
        }

        // MAP報酬計算
        int mapFund = mapEntity.getGivemoney();
        int mapFood = mapEntity.getGivefood();
        int mapMaterial = mapEntity.getGiveresource();
        int mapExp = (int) mapEntity.getExp();

     // 消費資源計算（レベル補正付き）
        int totalUseMoney = 0, totalUseFood = 0, totalUseResource = 0;
        for (CharacterDisplayDto ally : allies) {
            int level = ally.getLevel();

            // 基礎消費資源
            int baseMoney = ally.getUseMoney();
            int baseFood = ally.getUseFood();
            int baseResource = ally.getUseResource();

            // レベル補正付き消費
            totalUseMoney += (int) (baseMoney * (1 + 0.04 * level));
            totalUseFood += (int) (baseFood * (1 + 0.04 * level));
            totalUseResource += (int) (baseResource * (1 + 0.04 * level));
        }

        // 勝敗フラグは変更せず、報酬だけ計算
        boolean win = battleData.getWin();
        if (win) {
            reward.put("fund", mapFund - totalUseMoney);
            reward.put("food", mapFood - totalUseFood);
            reward.put("material", mapMaterial - totalUseResource);
            reward.put("exp", mapExp);

            for (CharacterDisplayDto ally : allies) {
                ally.setExpGained(mapExp);
            }
        } else {
            reward.put("fund", 0 - totalUseMoney);
            reward.put("food", 0 - totalUseFood);
            reward.put("material", 0 - totalUseResource);

            int loseExp = Math.max(mapExp / 20, 1);
            reward.put("exp", loseExp);

            for (CharacterDisplayDto ally : allies) {
                ally.setExpGained(loseExp);
            }
        }

        // アイテム
        String item = mapEntity.getGiveitem();
        boolean gotItem = false;
        if (win && item != null && !item.isEmpty() && !userItemRepository.existsByUsernameAndItemname(username, item)) {
            if (Math.random() < ITEM_DROP_RATE) {
                reward.put("item", item);
                gotItem = true;
            }
        }
        reward.put("getItem", gotItem);

     // キャラドロップ
        List<Character> possibleDrops = characterRepository.findByAffiliation(mapEntity.getMapname());
        boolean gotCharacter = false;
        Character droppedChar = null;
        CharacterDisplayDto dropCharDto = null;

        if (win && !possibleDrops.isEmpty() && Math.random() < CHARACTER_DROP_RATE) {

            droppedChar = possibleDrops.get((int) (Math.random() * possibleDrops.size()));
            gotCharacter = true;

            dropCharDto = new CharacterDisplayDto(droppedChar);

            // ★ キャラ画像パス設定
            if (droppedChar.getCharacterpicture() != null && !droppedChar.getCharacterpicture().isEmpty()) {
                dropCharDto.setCharacterpicture("/img/" + droppedChar.getCharacterpicture());
            }

            reward.put("droppedCharacterDto", dropCharDto);
            reward.put("droppedCharacter", droppedChar);
            battleData.setDropChar(dropCharDto);

            System.out.println("ドロップキャラ: " + droppedChar.getCharactername());
            System.out.println("画像パス: " + dropCharDto.getCharacterpicture());

         // ★★★★★ スタンプ登録処理 (Repository に合わせて修正) ★★★★★
            String charName = droppedChar.getCharactername();

            // Optional で取得して存在チェック
            Optional<UserCharacterStamp> existingStamp = userCharacterStampRepository
                    .findByUsernameAndRecordCharactername(username, charName);

            // なければ新規登録
            if (existingStamp.isEmpty()) {
                UserCharacterStamp stamp = new UserCharacterStamp();
                stamp.setUsername(username);
                stamp.setRecordCharactername(charName);
                userCharacterStampRepository.save(stamp);
            }


        }

        reward.put("getCharacter", gotCharacter);

        battleData.setReward(reward);
        return reward;
    }


    /**
     * 経験値・レベルアップ・ステータス上昇を適用してDB保存
     */
    public List<CharacterDisplayDto> applyLevelUpAndSave(List<CharacterDisplayDto> allies) {
        List<CharacterDisplayDto> leveledUpCharacters = new ArrayList<>();

        for (CharacterDisplayDto ally : allies) {
            OwnedCharacter oc = ownedCharacterRepository.findById(ally.getOwnedCharacterId()).orElse(null);
            if (oc == null) continue;

            int totalExp = oc.getExperience() + ally.getExpGained();
            oc.setExperience(totalExp);

            int oldLevel = oc.getLevel();
            int newLevel = oldLevel;

            while (true) {
                LevelExperience nextLevel = levelExperienceRepository.findByLevel(newLevel + 1);
                if (nextLevel == null || totalExp < nextLevel.getRequiredExp()) break;
                newLevel++;
            }

            ally.setLevelBeforeBattle(oldLevel);

            LevelExperience nextLevelExp = levelExperienceRepository.findByLevel(newLevel + 1);
            ally.setNextLevelExp(nextLevelExp != null ? nextLevelExp.getRequiredExp() : null);

            if (newLevel > oldLevel) {
                int levelDiff = newLevel - oldLevel;
                oc.setLevel(newLevel);
                oc.setHitpoint(oc.getHitpoint() + 2 * levelDiff);
                oc.setStrength(oc.getStrength() + 1 * levelDiff);

                ownedCharacterRepository.save(oc);

                ally.setLevel(newLevel);
                ally.setHitpoint(oc.getHitpoint());
                ally.setStrength(oc.getStrength());
                leveledUpCharacters.add(ally);
            } else {
                ownedCharacterRepository.save(oc);
            }
        }

        return leveledUpCharacters;
    }

    /**
     * 報酬適用
     */
    public void applyReward(String username, BattleSessionData battleData) {
        if (battleData == null || battleData.getReward() == null) return;

        Map<String, Object> reward = battleData.getReward();
        user u = userRepository.findByUsername(username);
        if (u == null) return;

        try {
            int haveFood = Integer.parseInt(u.getHavefood());
            int haveFund = Integer.parseInt(u.getHavefund());
            int haveMaterial = Integer.parseInt(u.getHavematerial());

            int addFood = (int) reward.getOrDefault("food", 0);
            int addFund = (int) reward.getOrDefault("fund", 0);
            int addMaterial = (int) reward.getOrDefault("material", 0);

            u.setHavefood(String.valueOf(haveFood + addFood));
            u.setHavefund(String.valueOf(haveFund + addFund));
            u.setHavematerial(String.valueOf(haveMaterial + addMaterial));

            userRepository.save(u);

            // アイテム
            if ((boolean) reward.getOrDefault("getItem", false)) {
                String itemName = (String) reward.get("item");
                UserItem ui = new UserItem();
                ui.setUsername(username);
                ui.setItemname(itemName);
                userItemRepository.save(ui);
            }

            // ドロップキャラ保存
            if ((boolean) reward.getOrDefault("getCharacter", false)) {
                Character c = (Character) reward.get("droppedCharacter");
                OwnedCharacter oc = new OwnedCharacter();
                oc.setUsername(username);
                oc.setOwnedcharacter(c.getCharactername());
                oc.setLevel(1);
                oc.setHitpoint(c.getInitialhp());
                oc.setStrength(c.getInitialstrength());
                oc.setRock(false);
                oc.setExperience(0);
                ownedCharacterRepository.save(oc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
