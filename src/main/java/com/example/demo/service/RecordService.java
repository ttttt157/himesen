package com.example.demo.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.OwnedCharacter;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.OwnedCharacterRepository;
import com.example.demo.repository.UserItemRepository;

@Service
public class RecordService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserItemRepository userItemRepository; // こっちのインスタンスを使う

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private OwnedCharacterRepository ownedCharacterRepository;

    // アイテム所有率
    public double getItemCompletionRate(String username) {
        int totalItems = (int) itemRepository.count();
        int ownedItems = userItemRepository.countByUsername(username); // ←インスタンス経由
        return (totalItems == 0) ? 0 : ((double) ownedItems / totalItems) * 100;
    }

    // キャラ所有率
    public double getCharacterCompletionRate(String username) {
        int totalDrop = characterRepository.countByAvailability("ドロップ");

        List<OwnedCharacter> owned = ownedCharacterRepository.findByUsername(username);
        Set<String> distinctOwnedNames = owned.stream()
                .map(OwnedCharacter::getOwnedcharacter)
                .collect(Collectors.toSet());

        return (totalDrop == 0) ? 0 : ((double) distinctOwnedNames.size() / totalDrop) * 100;
    }
}
