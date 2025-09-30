package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.user;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // ログイン
    public boolean login(String username, String password) {
        user u = userRepository.findByUsernameAndPassword(username, password);
        return u != null;
    }

    // 新規登録（資源は初期値500、進行状況は1-1）
    public boolean register(user user) {
        if(userRepository.existsById(user.getUsername())) {
            return false; // ユーザー名重複
        }
        user.setHavefood("500");
        user.setHavefund("500");
        user.setHavematerial("500");
        user.setUserinformation("1-1");
        userRepository.save(user);
        return true;
    }

    // -------------------------------
    // 追加機能：ユーザー情報取得
    // -------------------------------
    public user findByUsername(String username) {
        // ログイン済みの前提で存在するはずなので get() で取得
        return userRepository.findById(username).get();
    }
}
