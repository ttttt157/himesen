package com.example.demo.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.demo.dto.CharacterDto;
import com.example.demo.entity.Character;
import com.example.demo.entity.OwnedCharacter;
import com.example.demo.entity.UserCharacterStamp;
import com.example.demo.entity.user;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.repository.OwnedCharacterRepository;
import com.example.demo.repository.UserCharacterStampRepository;
import com.example.demo.repository.UserRepository;

@Service
public class GachaService {

    private final CharacterRepository characterRepository;
    private final UserRepository userRepository;
    private final OwnedCharacterRepository ownedCharacterRepository;
    private final UserCharacterStampRepository stampRepository; // 追加

    public GachaService(CharacterRepository characterRepository,
                        UserRepository userRepository,
                        OwnedCharacterRepository ownedCharacterRepository,
                        UserCharacterStampRepository stampRepository) { // 追加
        this.characterRepository = characterRepository;
        this.userRepository = userRepository;
        this.ownedCharacterRepository = ownedCharacterRepository;
        this.stampRepository = stampRepository; // 追加
    }

    public CharacterDto performGacha(String username, int useFood, int useFund, int useMaterial) {
        user u = userRepository.findByUsername(username);
        if (u == null) throw new RuntimeException("ユーザー情報が見つかりません: " + username);

        int haveFood = Integer.parseInt(u.getHavefood());
        int haveFund = Integer.parseInt(u.getHavefund());
        int haveMaterial = Integer.parseInt(u.getHavematerial());

        if (haveFood < useFood || haveFund < useFund || haveMaterial < useMaterial) {
            throw new RuntimeException("ガチャに必要な資源が不足しています");
        }

        // 消費
        u.setHavefood(String.valueOf(haveFood - useFood));
        u.setHavefund(String.valueOf(haveFund - useFund));
        u.setHavematerial(String.valueOf(haveMaterial - useMaterial));
        userRepository.save(u);

        List<Character> characters = characterRepository.findByAvailability("ガチャ");
        if (characters.isEmpty()) throw new RuntimeException("ガチャ対象のキャラクターが存在しません");

        // 出現確率計算
        Map<Character, Double> probabilityMap = new LinkedHashMap<>();
        double totalProb = 0.0;
        for (Character c : characters) {
            double prob = 1.0;
            if (c.getFightstyle() == 1 && useMaterial >= 100 && useFund >= 50 && useFood >= 50) prob *= 1.5;
            if (c.getFightstyle() == 2 && useMaterial >= 50 && useFund >= 100 && useFood >= 50) prob *= 1.5;
            if (c.getFightstyle() == 3 && useMaterial >= 50 && useFund >= 50 && useFood >= 100) prob *= 1.5;
            probabilityMap.put(c, prob);
            totalProb += prob;
        }

        double rand = new Random().nextDouble() * totalProb;
        double cumulative = 0.0;
        Character selected = null;
        for (Map.Entry<Character, Double> entry : probabilityMap.entrySet()) {
            cumulative += entry.getValue();
            if (rand <= cumulative) {
                selected = entry.getKey();
                break;
            }
        }

        if (selected == null) selected = characters.get(0);

        // 所持キャラ登録
        OwnedCharacter owned = new OwnedCharacter();
        owned.setUsername(username);
        owned.setOwnedcharacter(selected.getCharactername());
        owned.setLevel(1);
        owned.setCharactertype(selected.getFightstyle());
        owned.setHitpoint(selected.getInitialhp());
        owned.setStrength(selected.getInitialstrength());
        owned.setRock(false);
        owned.setExperience(0);
        ownedCharacterRepository.save(owned);

        // --- 追加: 1度でも所持したスタンプ登録 ---
        if (stampRepository.findByUsernameAndRecordCharactername(username, selected.getCharactername()).isEmpty()) {
            UserCharacterStamp stamp = new UserCharacterStamp();
            stamp.setUsername(username);
            stamp.setRecordCharactername(selected.getCharactername());
            stampRepository.save(stamp);
        }

        CharacterDto dto = new CharacterDto(selected);
        dto.setRemainingFood(haveFood - useFood);
        dto.setRemainingFund(haveFund - useFund);
        dto.setRemainingMaterial(haveMaterial - useMaterial);

        return dto;
    }

    public void setCharacterLock(OwnedCharacter ownedCharacter, boolean lock) {
        ownedCharacter.setRock(lock);
        ownedCharacterRepository.save(ownedCharacter);
    }
}
