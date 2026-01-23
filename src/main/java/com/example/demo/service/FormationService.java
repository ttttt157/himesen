package com.example.demo.service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Character;
import com.example.demo.entity.Formation;
import com.example.demo.entity.OwnedCharacter;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.repository.FormationRepository;
import com.example.demo.repository.OwnedCharacterRepository;

@Service
public class FormationService {

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private OwnedCharacterRepository ownedCharacterRepository;

    // ログインユーザーの編成取得
    public Formation getFormationByUsername(String username) {
        return formationRepository.findByPlayerid(username);
    }

    // IDからOwnedCharacter→キャラクター名経由でCharacter取得
    public Character getCharacterByOwnedId(int ownedId) {
        OwnedCharacter oc = ownedCharacterRepository.findById(ownedId).orElse(null);
        if (oc == null) return null;
        return characterRepository.findByCharactername(oc.getOwnedcharacter()).orElse(null);
    }

    // ランダムセリフ取得（キャラクターID経由）
    public String getRandomDialogue(String username) {
        Formation f = getFormationByUsername(username);
        if (f == null || f.getCharacterid1() == null) return null;

        Character c = getCharacterByOwnedId(f.getCharacterid1());
        if (c == null) return null;

        List<String> lines = Arrays.asList(
                c.getLine1(),
                c.getLine2(),
                c.getLine3(),
                c.getLine4(),
                c.getLine5()
        );

        List<String> validLines = lines.stream()
                .filter(l -> l != null && !l.trim().isEmpty())
                .collect(Collectors.toList());

        if (validLines.isEmpty()) return null;

        int idx = ThreadLocalRandom.current().nextInt(validLines.size());
        return validLines.get(idx);
    }

    // 編成名更新
    public void updateFormationName(String username, String formationName) {
        Formation f = getFormationByUsername(username);
        if (f != null) {
            f.setFormationname(formationName);
            formationRepository.save(f);
        }
    }

    // スロットにキャラをセット（キャラクターID）
    public void setCharacterInSlot(String username, int slot, int characterId) {
        Formation f = getFormationByUsername(username);
        if (f == null) return;

        switch (slot) {
            case 1: f.setCharacterid1(characterId); break;
            case 2: f.setCharacterid2(characterId); break;
            case 3: f.setCharacterid3(characterId); break;
            case 4: f.setCharacterid4(characterId); break;
            case 5: f.setCharacterid5(characterId); break;
            case 6: f.setCharacterid6(characterId); break;
            default: return;
        }
        formationRepository.save(f);
    }

    // OwnedCharacter ID からキャラIDを解決してセット
    public void setCharacterInSlotByOwnedCharacter(String username, int slot, OwnedCharacter oc) {
        if (oc == null) return;
        int characterId = oc.getId(); // ここでOwnedCharacterのIDを使用
        setCharacterInSlot(username, slot, characterId);
    }

    // キャラ情報取得（IDベース）
    public Character getCharacterById(int id) {
        return characterRepository.findById(id).orElse(null);
    }
    
    public void deleteOwnedCharacterById(int id) {
        ownedCharacterRepository.deleteById(id);
    }

 // FormationService に追加
    public void saveFormation(Formation f) {
        formationRepository.save(f);
    }


}
