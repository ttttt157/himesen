package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Character;
import com.example.demo.entity.UserCharacterStamp;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.repository.UserCharacterStampRepository;

@Service
public class CharacterCatalogService {

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private UserCharacterStampRepository userCharacterStampRepository;

    /**
     * ユーザーが一度でも入手したかどうかを判定しつつ
     * 全キャラ情報をリストで返す
     */
    public List<CatalogCharacterDto> getAllCharactersWithOwnership(String username) {
        List<Character> allCharacters = characterRepository.findAll();
        List<UserCharacterStamp> userStamps = userCharacterStampRepository.findByUsername(username);

        // スタンプに登録されているキャラ名セット
        Set<String> obtainedNames = userStamps.stream()
                .map(UserCharacterStamp::getRecordCharactername)
                .collect(Collectors.toSet());

        List<CatalogCharacterDto> catalog = new ArrayList<>();
        for (Character c : allCharacters) {
            boolean owned = obtainedNames.contains(c.getCharactername());
            boolean isEnemy = c.getUseresource() == 0 && c.getUsemoney() == 0 && c.getUsefood() == 0;

            CatalogCharacterDto dto = new CatalogCharacterDto();
            dto.setCharacter(c);
            dto.setOwned(owned);
            dto.setEnemy(isEnemy);
            catalog.add(dto);
        }
        return catalog;
    }

    // DTOクラスはそのまま
    public static class CatalogCharacterDto {
        private Character character;
        private boolean owned;
        private boolean enemy;

        public Character getCharacter() { return character; }
        public void setCharacter(Character character) { this.character = character; }
        public boolean isOwned() { return owned; }
        public void setOwned(boolean owned) { this.owned = owned; }
        public boolean isEnemy() { return enemy; }
        public void setEnemy(boolean enemy) { this.enemy = enemy; }
    }
}
