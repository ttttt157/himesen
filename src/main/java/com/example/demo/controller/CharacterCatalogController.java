package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.user;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CharacterCatalogService;
import com.example.demo.service.CharacterCatalogService.CatalogCharacterDto;

import jakarta.servlet.http.HttpSession;

@Controller
public class CharacterCatalogController {

    @Autowired
    private CharacterCatalogService catalogService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/characterCatalog")
    public String showCatalog(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/login";

        user currentUser = userRepository.findByUsername(username);

        // 敵キャラ表示解禁条件
        boolean reachedEnemyArea = currentUser != null &&
            (
                "沖縄県".equals(currentUser.getUserinformation()) ||
                "演習場1".equals(currentUser.getUserinformation()) ||
                "演習場2".equals(currentUser.getUserinformation()) ||
                "EX1".equals(currentUser.getUserinformation())
            );

        List<CatalogCharacterDto> catalog = catalogService.getAllCharactersWithOwnership(username);

        // 敵キャラを隠す（未到達の場合）
        if (!reachedEnemyArea) {
            catalog.removeIf(CatalogCharacterDto::isEnemy);
        }

        // ボタン表示の判断
        boolean showEnemyButton = catalog.stream().anyMatch(CatalogCharacterDto::isEnemy);

        model.addAttribute("catalog", catalog);
        model.addAttribute("showEnemyButton", showEnemyButton);

        return "catalog";
    }


}
