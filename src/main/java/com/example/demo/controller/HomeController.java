package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.user;
import com.example.demo.service.FormationService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private FormationService formationService;

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        // セッションからログインユーザー名を取得
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login"; // ログインしていなければログイン画面へ
        }

        // ユーザー情報取得
        user u = userService.findByUsername(username);
        model.addAttribute("username", u.getUsername());
        model.addAttribute("havefood", u.getHavefood());
        model.addAttribute("havefund", u.getHavefund());
        model.addAttribute("havematerial", u.getHavematerial());

        // 編成1番目のキャラ画像取得
        String char1Pic = formationService.getCharacter1Picture(username);
        model.addAttribute("characterPicture1", char1Pic);

        return "home";
    }
}
