package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Character;
import com.example.demo.repository.CharacterRepository;

@Service
public class CharacterService {

    @Autowired
    private CharacterRepository characterRepository;

    public List<Character> findAllCharacters() {
        return characterRepository.findAll();
    }

    public Character findById(int id) {
        return characterRepository.findById(id).orElse(null);
    }

    public void save(Character c) {
        characterRepository.save(c);
    }

    public void delete(Character c) {
        characterRepository.delete(c);
    }
    public boolean createCharacter(Character character) {
        // ID重複チェック
        if (characterRepository.existsById(character.getCharacterid())) {
            return false;
        }
        characterRepository.save(character);
        return true;
    }
    @Transactional
    public void deleteCharacterById(int id) {
        System.out.println("[DEBUG] Deleting character id=" + id);
        characterRepository.deleteById(id);
    }
    
    


}
