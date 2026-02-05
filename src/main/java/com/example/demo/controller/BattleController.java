package com.example.demo.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.CharacterDto;
import com.example.demo.entity.user;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.GachaService;

import jakarta.servlet.http.HttpSession;

@Controller
public class GachaController {

    private final GachaService gachaService;
    private final UserRepository userRepository;

    public GachaController(GachaService gachaService, UserRepository userRepository) {
        this.gachaService = gachaService;
        this.userRepository = userRepository;
    }

    @GetMapping("/gacha")
    public String gachaPage(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            user u = userRepository.findByUsername(username);
            if (u != null) model.addAttribute("userdata", u);
        }
        return "gacha";
    }

    @PostMapping("/gacha/draw")
    @ResponseBody
    public CharacterDto drawGacha(@RequestParam int useFood,
                                  @RequestParam int useFund,
                                  @RequestParam int useMaterial,
                                  HttpSession session) {
        String username = (String) session.getAttribute("username");
        return gachaService.performGacha(username, useFood, useFund, useMaterial);
    }
    
    @PostMapping("/gacha/draw/rare")
    @ResponseBody
    public Object drawRareGacha(HttpSession session) {
        String username = (String) session.getAttribute("username");

        CharacterDto dto = gachaService.performRareGacha(username);

        // コンプリート時
        if (dto == null) {
            return Map.of("message", "complete");
        }

        return dto;
    }

}
