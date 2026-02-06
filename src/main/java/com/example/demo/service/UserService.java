package com.example.demo.service;

// 追加
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Formation;
import com.example.demo.entity.OwnedCharacter;
import com.example.demo.entity.UserCharacterStamp;
import com.example.demo.entity.UserItem;
import com.example.demo.entity.user;
import com.example.demo.repository.FormationRepository;
import com.example.demo.repository.OwnedCharacterRepository;
import com.example.demo.repository.UserCharacterStampRepository;
import com.example.demo.repository.UserItemRepository;
import com.example.demo.repository.UserRepository;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private OwnedCharacterRepository ownedCharacterRepository;
    
    @Autowired
    private FormationRepository formationRepository;
    
    @Autowired
    private UserItemRepository userItemRepository;

    @Autowired
    private UserCharacterStampRepository userCharacterStampRepository;




    // ログイン
    public boolean login(String username, String password) {
        user u = userRepository.findByUsernameAndPassword(username, password);
        return u != null;
    }

    // 新規登録（資源は初期値500、進行状況は東京）
    public boolean register(user user) {
        if(userRepository.existsById(user.getUsername())) {
            return false; // ユーザー名重複
        }
        // 1. プレイヤー情報初期化
        user.setHavefood("2000");
        user.setHavefund("2000");
        user.setHavematerial("2000");
        user.setUserinformation("東京都");
        userRepository.save(user);

        String username = user.getUsername();

     // 2. 初期キャラ「風花」を OwnedCharacter に登録
        OwnedCharacter initialChar = new OwnedCharacter();
        initialChar.setUsername(username);
        initialChar.setOwnedcharacter("風花");
        initialChar.setLevel(1);
        initialChar.setCharactertype(1); // fightstyle
        initialChar.setHitpoint(15);      // 初期HP
        initialChar.setStrength(3);      // 初期STR
        initialChar.setRock(false);       // 初期は未ロック
        initialChar.setExperience(0);     // 初期EXP
        ownedCharacterRepository.save(initialChar); // ←保存後、ID が自動でセットされる

        // 3. Formation に初期キャラの ID を 1 枠目に設定
        Formation formation = new Formation();
        formation.setPlayerid(username);
        formation.setCharacterid1(initialChar.getId()); // ←Integer型のまま渡す
        formation.setCharacterid2(null);
        formation.setCharacterid3(null);
        formation.setCharacterid4(null);
        formation.setCharacterid5(null);
        formation.setCharacterid6(null);
        formation.setFormationname("編成");
        formationRepository.save(formation);


        // 4. 初期アイテム「落花生」を UserItem に追加
        UserItem item = new UserItem();
        item.setUsername(username);
        item.setItemname("落花生");
        userItemRepository.save(item);
        
        
     // 5. 初期キャラを user_character_stamp にも登録
        UserCharacterStamp stamp = new UserCharacterStamp();
        stamp.setUsername(username);               // 新規登録ユーザー名
        stamp.setRecordCharactername("風花");     // 初期キャラ名
        userCharacterStampRepository.save(stamp);
        

        return true;
    }
    

    // Service 内に追加
    public List<user> findAllUsers() {
        return userRepository.findAll();
    }


    // -------------------------------
    // 追加機能：ユーザー情報取得
    // -------------------------------
    public user findByUsername(String username) {
        // ログイン済みの前提で存在するはずなので get() で取得
        return userRepository.findById(username).get();
    }

    public void save(user u) {
        userRepository.save(u);
    }
    
    public void delete(user u) {
        userRepository.delete(u);
    }

    
}
