package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.user;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // ログイン画面
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // ログイン処理
    @PostMapping("/login")
    public String login(@RequestParam String username, 
                        @RequestParam String password, 
                        HttpSession session,
                        Model model) {

        if(userService.login(username, password)) {
            // セッションにログイン者のIDを保持
            session.setAttribute("username", username);
            return "redirect:/home";
        } else {
            model.addAttribute("error", "ユーザー名またはパスワードが間違っています");
            return "login";
        }
    }

    // 新規登録画面
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new user());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute user user, Model model) {
        if(userService.register(user)) {
            return "redirect:/login";
        } else {
            model.addAttribute("error", "ユーザー名が既に存在します");
            return "register";
        }
    }


    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // セッションを破棄
        return "redirect:/login"; // ログイン画面に戻す
    }

}
