package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Character;
import com.example.demo.entity.Formation;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.repository.FormationRepository;

@Service
public class FormationService {

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private CharacterRepository characterRepository;

    // ログインユーザーの編成取得
    public Formation getFormationByUsername(String username) {
        return formationRepository.findByPlayerid(username);
    }
 // 編成のcharacterid1からキャラ画像取得
    public String getCharacter1Picture(String username) {
        Formation f = getFormationByUsername(username);
        if(f != null && f.getCharacterid1() != null) {
            Character c = characterRepository.findById(f.getCharacterid1()).orElse(null);
            if(c != null) {
                // DBにフルパスや images/ が入っていても、ファイル名だけ取り出す
                String fileName = c.getCharacterpicture().replaceAll(".*/", ""); // 例: "images/1.png" -> "1.png"
                return "/img/" + fileName; // HTML からは /img/1.png としてアクセス
            }
        }
        return "/img/placeholder.png"; // キャラなし時の代替
    }


}