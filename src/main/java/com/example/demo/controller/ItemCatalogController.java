package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.dto.ItemCatalogDto;
import com.example.demo.service.ItemCatalogService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ItemCatalogController {

    @Autowired
    private ItemCatalogService catalogService;

    @GetMapping("/itemCatalog")
    public String showCatalog(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/login";

        List<ItemCatalogDto> catalog = catalogService.getAllItemsWithOwnership(username);
        model.addAttribute("catalog", catalog);

        return "itemCatalog"; // itemCatalog.html を表示
    }
}
