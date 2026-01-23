package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.OwnedCharacter;
import com.example.demo.repository.OwnedCharacterRepository;

@Service
public class OwnedCharacterService {

    @Autowired
    private OwnedCharacterRepository ownedCharacterRepository;

    // username に紐づく所持キャラ一覧を取得
    public List<OwnedCharacter> getOwnedCharacters(String username) {
        return ownedCharacterRepository.findByUsername(username);
    }
    public OwnedCharacter getOwnedCharacterById(int id) {
        return ownedCharacterRepository.findById(id).orElse(null);
    }
    public void save(OwnedCharacter oc) {
        ownedCharacterRepository.save(oc);
    }
    public void deleteOwnedCharacterById(int id) {
        ownedCharacterRepository.deleteById(id);
    }




}
