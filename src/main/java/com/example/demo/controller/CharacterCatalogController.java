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

        /* ===== ヘッダー用（表示しなくても必須） ===== */
        model.addAttribute("username", username);
        model.addAttribute("havefood", currentUser.getHavefood());
        model.addAttribute("havefund", currentUser.getHavefund());
        model.addAttribute("havematerial", currentUser.getHavematerial());
        model.addAttribute("currentMap", currentUser.getUserinformation());

        // 今回の画面では使わないのでダミーでOK
        model.addAttribute("ownedItemCount", 0);
        model.addAttribute("totalItemCount", 0);
        model.addAttribute("ownedCharCount", 0);
        model.addAttribute("totalCharCount", 0);

        /* ===== 図鑑ロジック ===== */
        boolean reachedEnemyArea =
            "沖縄県".equals(currentUser.getUserinformation()) ||
            "演習場1".equals(currentUser.getUserinformation()) ||
            "演習場2".equals(currentUser.getUserinformation()) ||
            "EX1".equals(currentUser.getUserinformation());

        List<CatalogCharacterDto> catalog =
            catalogService.getAllCharactersWithOwnership(username);

        // ★ characterId 昇順ソート（ここが追加）
        catalog.sort(
            java.util.Comparator.comparing(
                dto -> dto.getCharacter().getCharacterid()
            )
        );

        if (!reachedEnemyArea) {
            catalog.removeIf(CatalogCharacterDto::isEnemy);
        }

        boolean showEnemyButton =
            catalog.stream().anyMatch(CatalogCharacterDto::isEnemy);

        model.addAttribute("catalog", catalog);
        model.addAttribute("showEnemyButton", showEnemyButton);

        return "catalog";
    }
}


