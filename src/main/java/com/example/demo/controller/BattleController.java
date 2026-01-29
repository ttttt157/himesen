package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.BattleSessionData;
import com.example.demo.dto.CharacterDisplayDto;
import com.example.demo.entity.Formation;
import com.example.demo.entity.Maps;
import com.example.demo.entity.OwnedCharacter;
import com.example.demo.entity.user;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.repository.FormationRepository;
import com.example.demo.repository.MapsRepository;
import com.example.demo.service.BattleService;
import com.example.demo.service.OwnedCharacterService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BattleController {

    @Autowired
    private UserService userService;

    @Autowired
    private MapsRepository mapRepository;

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private OwnedCharacterService ownedCharacterService;

    @Autowired
    private BattleService battleService;
    
 // --- マップ画面 ---
    @GetMapping("/map")
    public String mapScreen(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/login";

        user u = userService.findByUsername(username);
        String currentMapName = u.getUserinformation();

        Maps currentMap = mapRepository.findByMapname(currentMapName);

        List<Maps> availableMaps = new ArrayList<>();
        if (currentMap != null) {
            availableMaps = mapRepository.findAll().stream()
                    .filter(m -> m.getMapid() <= currentMap.getMapid())
                    .sorted((m1, m2) -> Integer.compare(m1.getMapid(), m2.getMapid()))
                    .toList();
        }

        // --- 初回ユーザー対応: ID1のマップを必ず追加 ---
        if (availableMaps.isEmpty()) {
            mapRepository.findById(1).ifPresent(availableMaps::add);
        }

        // --- 編成の消費資源合計を計算 ---
        Formation formation = formationRepository.findByPlayerid(username);
        int totalFood = 0, totalMoney = 0, totalResource = 0;
        if (formation != null) {
            Integer[] charIds = {
                formation.getCharacterid1(), formation.getCharacterid2(), formation.getCharacterid3(),
                formation.getCharacterid4(), formation.getCharacterid5(), formation.getCharacterid6()
            };
            for (Integer charId : charIds) {
                if (charId != null) {
                    com.example.demo.entity.Character c = characterRepository.findById(charId).orElse(null);
                    if (c != null) {
                        totalFood += c.getUsefood();
                        totalMoney += c.getUsemoney();
                        totalResource += c.getUseresource();
                    }
                }
            }
        }

        // --- 所持資源と比較して出陣可能か判定 ---
        boolean canDeploy = true;
        if (Integer.parseInt(u.getHavefood()) < totalFood ||
            Integer.parseInt(u.getHavefund()) < totalMoney ||
            Integer.parseInt(u.getHavematerial()) < totalResource) {
            canDeploy = false;
        }

        model.addAttribute("username", username);
        model.addAttribute("currentMapId", currentMap != null ? currentMap.getMapid() : 1); // 初回は1
        model.addAttribute("availableMaps", availableMaps);
        model.addAttribute("canDeploy", canDeploy);
        model.addAttribute("requiredFood", totalFood);
        model.addAttribute("requiredMoney", totalMoney);
        model.addAttribute("requiredResource", totalResource);

        session.removeAttribute("battleData"); // 戦闘データをクリア

        return "map";
    }


    // --- 戦闘開始 ---
    @GetMapping("/deploy/{mapId}")
    public String deployBattleById(@PathVariable int mapId, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/login";

        user u = userService.findByUsername(username);

        // 編成の消費資源チェック
        Formation formation = formationRepository.findByPlayerid(username);
        int totalFood = 0, totalMoney = 0, totalResource = 0;
        if (formation != null) {
            Integer[] charIds = {
                formation.getCharacterid1(), formation.getCharacterid2(), formation.getCharacterid3(),
                formation.getCharacterid4(), formation.getCharacterid5(), formation.getCharacterid6()
            };
            for (Integer charId : charIds) {
                if (charId != null) {
                    com.example.demo.entity.Character c = characterRepository.findById(charId).orElse(null);
                    if (c != null) {
                        totalFood += c.getUsefood();
                        totalMoney += c.getUsemoney();
                        totalResource += c.getUseresource();
                    }
                }
            }

        }

        if (Integer.parseInt(u.getHavefood()) < totalFood ||
            Integer.parseInt(u.getHavefund()) < totalMoney ||
            Integer.parseInt(u.getHavematerial()) < totalResource) {
            // 資源不足の場合はマップ画面に戻る
            return "redirect:/map";
        }

        Maps map = mapRepository.findById(mapId).orElse(null);
        if (map == null) return "redirect:/map";

        session.setAttribute("currentMapId", map.getMapid());

        // --- 以下、元の戦闘準備処理 ---
        List<CharacterDisplayDto> allyList = new ArrayList<>();
        Map<Integer, Integer> allyHpMap = new HashMap<>();
        if (formation != null) {
            Integer[] charIds = {
                formation.getCharacterid1(), formation.getCharacterid2(), formation.getCharacterid3(),
                formation.getCharacterid4(), formation.getCharacterid5(), formation.getCharacterid6()
            };
            for (Integer ocId : charIds) {
                if (ocId != null) {
                    OwnedCharacter oc = ownedCharacterService.getOwnedCharacterById(ocId);
                    if (oc != null) {
                        characterRepository.findByCharactername(oc.getOwnedcharacter()).ifPresent(c -> {
                            CharacterDisplayDto dto = new CharacterDisplayDto(oc, c);
                            allyList.add(dto);
                            allyHpMap.put(ocId, dto.getHitpoint());
                        });
                    }
                }
            }
        }

        List<CharacterDisplayDto> enemyList = new ArrayList<>();
        Map<Integer, Integer> enemyHpMap = new HashMap<>();
        String[] enemies = {
            map.getEnemy1(), map.getEnemy2(), map.getEnemy3(),
            map.getEnemy4(), map.getEnemy5(), map.getEnemy6()
        };
        for (String enemyName : enemies) {
            if (enemyName != null && !enemyName.isEmpty()) {
                characterRepository.findByCharactername(enemyName).ifPresent(c -> {
                    CharacterDisplayDto dto = new CharacterDisplayDto(c);
                    enemyList.add(dto);
                    enemyHpMap.put(c.getCharacterid(), dto.getHitpoint());
                });
            }
        }

        BattleSessionData battleData = new BattleSessionData();
        battleData.setMapId(map.getMapid());
        battleData.setAllies(allyList);
        battleData.setAllyHpMap(allyHpMap);
        battleData.setEnemies(enemyList);
        battleData.setEnemyHpMap(enemyHpMap);
        session.setAttribute("battleData", battleData);

        model.addAttribute("username", username);
        model.addAttribute("allies", allyList);
        model.addAttribute("enemies", enemyList);
        model.addAttribute("mapName", map.getMapname());

        return "battle";
    }
    
    @PostMapping("/battle/saveBattleResult")
    @ResponseBody
    public String saveBattleResult(@RequestBody Map<String, Object> data, HttpSession session) {
        BattleSessionData battleData = (BattleSessionData) session.getAttribute("battleData");
        if (battleData == null) return "NG";

        // 勝敗
        Boolean win = (Boolean) data.get("win");
        Boolean lose = (Boolean) data.get("lose");

        // allies
        List<Map<String, Object>> allyList = (List<Map<String,Object>>) data.get("allies");
        Map<String,Integer> allyHpMap = new HashMap<>();
        for (Map<String,Object> a : allyList) {
            allyHpMap.put(String.valueOf(a.get("id")), (Integer)a.get("hp"));
        }

        // enemies
        List<Map<String, Object>> enemyList = (List<Map<String,Object>>) data.get("enemies");
        Map<String,Integer> enemyHpMap = new HashMap<>();
        for (Map<String,Object> e : enemyList) {
            enemyHpMap.put(String.valueOf(e.get("id")), (Integer)e.get("hp"));
        }

        // BattleSessionData に反映
        for (CharacterDisplayDto ally : battleData.getAllies()) {
            if (allyHpMap.containsKey(String.valueOf(ally.getOwnedCharacterId()))) {
                ally.setHitpoint(allyHpMap.get(String.valueOf(ally.getOwnedCharacterId())));
            }
        }
        for (CharacterDisplayDto enemy : battleData.getEnemies()) {
            if (enemyHpMap.containsKey(enemy.getCharacterId())) {
                enemy.setHitpoint(enemyHpMap.get(enemy.getCharacterId()));
            }
        }

        // battleLog 保存（空行を除去）
        List<String> battleLog = ((List<String>) data.get("battleLog"))
            .stream()
            .filter(s -> s != null && !s.trim().isEmpty())
            .toList();
        battleData.setBattleLog(battleLog);

        // ★ 勝敗をセッションに反映
        battleData.setWin(win != null && win);
        battleData.setLose(lose != null && lose);
        session.setAttribute("battleData", battleData);

        // === デバッグ出力 ===
        System.out.println("=== サーバー側デバッグ ===");
        System.out.println("win: " + win + ", lose: " + lose);
        System.out.println("味方HP: " + allyHpMap);
        System.out.println("敵HP: " + enemyHpMap);
        System.out.println("battleLog: " + battleLog);

     // ★ 勝利時のみマップ進行処理を追加 ★
        if (win != null && win) {
            String username = (String) session.getAttribute("username");
            if (username != null) {
                // 今回クリアしたマップID
                Integer clearedMapId = battleData.getMapId();
                Integer nextMapId = clearedMapId + 1;

                // ユーザー情報取得
                user u = userService.findByUsername(username);
                Maps currentMap = mapRepository.findByMapname(u.getUserinformation());
                Integer currentMapId = currentMap.getMapid();

                // 進行度が後退しない場合のみ更新
                if (nextMapId > currentMapId) {
                    mapRepository.findById(nextMapId).ifPresent(nextMap -> {
                        u.setUserinformation(nextMap.getMapname());
                        userService.save(u);
                        System.out.println("マップ進行: " + currentMapId + " → " + nextMapId);
                    });
                } else {
                    System.out.println("既に進行度が " + currentMapId + "。今回の勝利(" + clearedMapId + ")では更新なし。");
                }
            } else {
                System.out.println("ユーザー名がセッションに存在しません。マップ進行スキップ。");
            }
        }


        return "OK";
    }
    
    
    
    








    @GetMapping("/battle/result")
    public String showBattleResult(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/login";

        // 受け取り済みなら result 画面へ入れない
        Boolean rewardTaken = (Boolean) session.getAttribute("rewardTaken");
        if (rewardTaken != null && rewardTaken) {
            return "redirect:/home";
        }

        BattleSessionData battleData = (BattleSessionData) session.getAttribute("battleData");
        if (battleData == null) return "redirect:/map";

        // reward 計算
        if (battleData.getReward() == null) {
            battleService.calculateBattle(username, battleData);
        }

        List<CharacterDisplayDto> leveledUpCharacters = battleService.applyLevelUpAndSave(battleData.getAllies());

        model.addAttribute("win", battleData.getWin());
        model.addAttribute("lose", battleData.getLose());
        model.addAttribute("reward", battleData.getReward());
        model.addAttribute("allies", battleData.getAllies());
        model.addAttribute("allyHpMap", battleData.getAllyHpMap());
        model.addAttribute("enemyHpMap", battleData.getEnemyHpMap());
        model.addAttribute("leveledUpCharacters", leveledUpCharacters);
        model.addAttribute("dropChar", battleData.getDropChar());

        return "result";
    }


    @GetMapping("/explore")
    public String exploreMap(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/login";

        user u = userService.findByUsername(username);
        Integer mapId = (Integer) session.getAttribute("currentMapId");
        if (mapId == null) return "redirect:/map";

        Long lastExplore = (Long) session.getAttribute("lastExploreTime");
        long now = System.currentTimeMillis();
        long recastTime = 3600_000; // 1時間
        if (lastExplore != null && now - lastExplore < recastTime) {
            long remaining = recastTime - (now - lastExplore);
            model.addAttribute("cooldown", remaining / 1000);
            return "exploreCooldown";
        }

        Maps map = mapRepository.findById(mapId).orElse(null);
        if (map == null) return "redirect:/map";

        int materialGain = (int) (map.getGiveresource() * 1.5);
        int fundGain = (int) (map.getGivemoney() * 1.5);
        int foodGain = (int) (map.getGivefood() * 1.5);

        u.setHavematerial(String.valueOf(Integer.parseInt(u.getHavematerial()) + materialGain));
        u.setHavefund(String.valueOf(Integer.parseInt(u.getHavefund()) + fundGain));
        u.setHavefood(String.valueOf(Integer.parseInt(u.getHavefood()) + foodGain));
        userService.save(u);

        session.setAttribute("lastExploreTime", now);

        Map<String, Object> reward = new HashMap<>();
        reward.put("material", materialGain);
        reward.put("fund", fundGain);
        reward.put("food", foodGain);
        model.addAttribute("reward", reward);

        return "exploreResult";
    }
    
    
    
    @GetMapping("/explore/{mapId}")
    public String exploreMap(@PathVariable int mapId, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/login";

        user u = userService.findByUsername(username);

        // ★ ユーザーごとの共通リキャスト
        Long lastExplore = (Long) session.getAttribute("lastExploreTime");
        long now = System.currentTimeMillis();
        long recastTime = 3600_000; // 1時間
        if (lastExplore != null && now - lastExplore < recastTime) {
            long remaining = recastTime - (now - lastExplore);
            model.addAttribute("cooldown", remaining / 1000);
            return "exploreCooldown";
        }

        Maps map = mapRepository.findById(mapId).orElse(null);
        if (map == null) return "redirect:/map";

        int materialGain = (int) (map.getGiveresource() * 1.5);
        int fundGain = (int) (map.getGivemoney() * 1.5);
        int foodGain = (int) (map.getGivefood() * 1.5);

        u.setHavematerial(String.valueOf(Integer.parseInt(u.getHavematerial()) + materialGain));
        u.setHavefund(String.valueOf(Integer.parseInt(u.getHavefund()) + fundGain));
        u.setHavefood(String.valueOf(Integer.parseInt(u.getHavefood()) + foodGain));
        userService.save(u);

        // ★ ユーザーごとの共通リキャスト
        session.setAttribute("lastExploreTime", now);

        Map<String, Object> reward = new HashMap<>();
        reward.put("material", materialGain);
        reward.put("fund", fundGain);
        reward.put("food", foodGain);
        model.addAttribute("reward", reward);

        return "exploreResult";
    }

    
}
