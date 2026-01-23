package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ItemCatalogDto;
import com.example.demo.entity.Item;
import com.example.demo.entity.UserItem;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserItemRepository;

@Service
public class ItemCatalogService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserItemRepository userItemRepository;

    public List<ItemCatalogDto> getAllItemsWithOwnership(String username) {
        // ユーザーが所持しているアイテム名一覧
        List<String> ownedNames = userItemRepository.findByUsername(username)
                                                    .stream()
                                                    .map(UserItem::getItemname)
                                                    .toList();

        // 全アイテムを DTO に変換して所持フラグを付与
        return itemRepository.findAll().stream()
                .map(item -> {
                    ItemCatalogDto dto = new ItemCatalogDto();
                    dto.setItemId(item.getItemid());
                    dto.setItemName(item.getItemname());
                    dto.setImage(item.getItemimage());         // 一覧カード用画像
                    dto.setExplanation(item.getItempicture()); // 説明文として流用
                    dto.setOwned(ownedNames.contains(item.getItemname()));
                    return dto;
                })
                .collect(Collectors.toList());
    }
    public boolean createItem(Item item) {
        if(itemRepository.existsById(item.getItemid())) {
            return false; // ID重複
        }
        itemRepository.save(item);
        return true;
    }
}
