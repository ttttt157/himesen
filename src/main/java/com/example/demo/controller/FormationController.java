package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.CharacterDisplayDto;
import com.example.demo.entity.Character;
import com.example.demo.entity.Formation;
import com.example.demo.entity.OwnedCharacter;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.repository.UserDataRepository;
import com.example.demo.service.FormationService;
import com.example.demo.service.OwnedCharacterService;

import jakarta.servlet.http.HttpSession;

@Controller
public class FormationController {

    @Autowired
    private FormationService formationService;

    @Autowired
    private OwnedCharacterService ownedCharacterService;

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private UserDataRepository userDataRepository;

    @GetMapping("/formation")
    public String formation(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/login";

        model.addAttribute("username", username);

        // 編成取得
        Formation formation = formationService.getFormationByUsername(username);
        model.addAttribute("formation", formation);

        // 各スロットのキャラ情報を DTO に
        List<CharacterDisplayDto> slots = new ArrayList<>();
        if (formation != null) {
            Integer[] ids = {
                formation.getCharacterid1(),
                formation.getCharacterid2(),
                formation.getCharacterid3(),
                formation.getCharacterid4(),
                formation.getCharacterid5(),
                formation.getCharacterid6()
            };
            for (Integer ocId : ids) {
                if (ocId != null && ocId != 0) {
                    OwnedCharacter oc = ownedCharacterService.getOwnedCharacterById(ocId);
                    if (oc != null) {
                        Character c = characterRepository.findByCharactername(oc.getOwnedcharacter()).orElse(null);
                        slots.add(new CharacterDisplayDto(oc, c));
                    } else {
                        slots.add(null);
                    }
                } else {
                    slots.add(null);
                }
            }
        }
        model.addAttribute("slots", slots);

        // 所持キャラ一覧（DTO）
        List<OwnedCharacter> ownedCharacters = ownedCharacterService.getOwnedCharacters(username);
        List<CharacterDisplayDto> displayList = ownedCharacters.stream()
            .map(oc -> {
                Character c = characterRepository.findByCharactername(oc.getOwnedcharacter()).orElse(null);
                return new CharacterDisplayDto(oc, c);
            })
            .collect(Collectors.toList());
        model.addAttribute("ownedCharacters", displayList);

        return "formation";
    }

    @PostMapping("/formation/setCharacter")
    public String setCharacter(HttpSession session,
                               @RequestParam int slot,
                               @RequestParam int ownedCharacterId,
                               Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        OwnedCharacter oc = ownedCharacterService.getOwnedCharacterById(ownedCharacterId);
        if (oc == null || !username.equals(oc.getUsername())) {
            return "redirect:/formation";
        }

        // --- 同名キャラ重複チェック（スロット除外） ---
        Formation f = formationService.getFormationByUsername(username);
        if (f != null) {
            Integer[] ids = {
                f.getCharacterid1(),
                f.getCharacterid2(),
                f.getCharacterid3(),
                f.getCharacterid4(),
                f.getCharacterid5(),
                f.getCharacterid6()
            };

            for (int i = 0; i < ids.length; i++) {
                if (i == slot - 1) continue; // 現在セットするスロットはスキップ

                Integer id = ids[i];
                if (id != null && id != 0) {
                    OwnedCharacter existing = ownedCharacterService.getOwnedCharacterById(id);
                    if (existing != null && existing.getOwnedcharacter().equals(oc.getOwnedcharacter())) {
                        // 他スロットに同名キャラが編成済み
                        session.setAttribute("formationError", "同じ名前のキャラは別スロットに編成できません。");
                        return "redirect:/formation";
                    }
                }
            }
        }

        // --- 問題なければ編成にセット（置き換えOK） ---
        formationService.setCharacterInSlot(username, slot, oc.getId());

        // ★ 正常完了なのでエラーをクリア
        session.removeAttribute("formationError");
        return "redirect:/formation";
    }



    @PostMapping("/ownedCharacter/toggleRock")
    @ResponseBody
    public Map<String, Boolean> toggleRock(@RequestParam int ownedCharacterId, HttpSession session) {
        Map<String, Boolean> result = new HashMap<>();
        String username = (String) session.getAttribute("username");
        if (username == null) {
            result.put("rock", false);
            return result;
        }
        OwnedCharacter oc = ownedCharacterService.getOwnedCharacterById(ownedCharacterId);
        if (oc != null && username.equals(oc.getUsername())) {
            oc.setRock(!oc.isRock());
            ownedCharacterService.save(oc);
            result.put("rock", oc.isRock());
        } else {
            result.put("rock", false);
        }
        return result;
    }
 // ===== 解体機能 =====
    @PostMapping("/ownedCharacter/demolish")
    @ResponseBody
    public Map<String, Object> demolish(@RequestParam int ownedCharacterId,
                                        HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        String username = (String) session.getAttribute("username");
        if (username == null) {
            result.put("success", false);
            result.put("message", "ログインしてください");
            return result;
        }

        OwnedCharacter oc = ownedCharacterService.getOwnedCharacterById(ownedCharacterId);
        if (oc == null || !username.equals(oc.getUsername())) {
            result.put("success", false);
            result.put("message", "キャラクターが見つかりません");
            return result;
        }

        // ロックされている場合は解体不可
        if (oc.isRock()) {
            result.put("success", false);
            result.put("message", "ロックされているキャラクターは解体できません");
            return result;
        }

        // 編成中かチェック
        Formation f = formationService.getFormationByUsername(username);
        if (f != null) {
            Integer[] ids = {
                f.getCharacterid1(), f.getCharacterid2(), f.getCharacterid3(),
                f.getCharacterid4(), f.getCharacterid5(), f.getCharacterid6()
            };
            for (Integer id : ids) {
                if (id != null && id == ownedCharacterId) {
                    result.put("success", false);
                    result.put("message", "編成中のキャラクターは解体できません");
                    return result;
                }
            }
        }

        // キャラ情報取得
        Character c = formationService.getCharacterByOwnedId(ownedCharacterId);
        if (c != null) {
            // ユーザー資源更新
            Map<String, Object> userdata = userDataRepository.getUserData(username);
            int haveFood = Integer.parseInt((String) userdata.get("havefood"));
            int haveFund = Integer.parseInt((String) userdata.get("havefund"));
            int haveMaterial = Integer.parseInt((String) userdata.get("havematerial"));

            haveFood += c.getDemolitionfood();
            haveFund += c.getDemolitionmoney();
            haveMaterial += c.getDemolitionresource();

            userDataRepository.updateResources(username, haveFood, haveFund, haveMaterial);
        }

        // キャラ削除
        try {
            ownedCharacterService.deleteOwnedCharacterById(ownedCharacterId);
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }

        return result;
    }
    
    
    @PostMapping("/formation/removeCharacter")
    @ResponseBody
    public Map<String, Object> removeCharacter(HttpSession session,
                                               @RequestParam int slot) {
        Map<String, Object> result = new HashMap<>();
        String username = (String) session.getAttribute("username");
        if (username == null) {
            result.put("success", false);
            result.put("message", "ログインしてください");
            return result;
        }

        Formation f = formationService.getFormationByUsername(username);
        if (f == null) {
            result.put("success", false);
            result.put("message", "編成が見つかりません");
            return result;
        }

        // 該当スロットを null に
        switch (slot) {
            case 1: f.setCharacterid1(null); break;
            case 2: f.setCharacterid2(null); break;
            case 3: f.setCharacterid3(null); break;
            case 4: f.setCharacterid4(null); break;
            case 5: f.setCharacterid5(null); break;
            case 6: f.setCharacterid6(null); break;
            default:
                result.put("success", false);
                result.put("message", "スロット番号が不正です");
                return result;
        }

        formationService.saveFormation(f);
        result.put("success", true);
        return result;
    }
    
    
    @PostMapping("/formation/updatename")
    public String updateFormationName(HttpSession session,
                                      @RequestParam String formationName) {
        String username = (String) session.getAttribute("username");
        if (username != null && formationName != null && !formationName.isEmpty()) {
            formationService.updateFormationName(username, formationName);
        }
        return "redirect:/formation";
    }




}
