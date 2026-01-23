package com.example.demo.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.ItemCatalogDto;
import com.example.demo.entity.Character;
import com.example.demo.entity.Formation;
import com.example.demo.entity.Item;
import com.example.demo.entity.Maps; // 必須import com.example.demo.entity.Maps; // 必須
import com.example.demo.entity.OwnedCharacter;
import com.example.demo.entity.user;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.OwnedCharacterRepository;
import com.example.demo.repository.UserItemRepository;
import com.example.demo.service.CharacterService;
import com.example.demo.service.FormationService;
import com.example.demo.service.ItemCatalogService;
import com.example.demo.service.MapService;
import com.example.demo.service.RecordService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;


@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private FormationService formationService;
    
    @Autowired
    private CharacterService characterService;
    
    @Autowired
    private ItemCatalogService itemCatalogService;
    
    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private MapService mapService; 
    

    @Autowired
    private RecordService recordService;
    

    @Autowired
    private UserItemRepository userItemRepository;

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private OwnedCharacterRepository ownedCharacterRepository;



    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        // --- ユーザー情報 ---
        user u = userService.findByUsername(username);
        model.addAttribute("username", u.getUsername());
        model.addAttribute("havefood", u.getHavefood());
        model.addAttribute("havefund", u.getHavefund());
        model.addAttribute("havematerial", u.getHavematerial());

        // --- 編成1番目キャラ表示 ---
        Formation f = formationService.getFormationByUsername(username);
        if (f != null && f.getCharacterid1() != null) {
            Character c = formationService.getCharacterByOwnedId(f.getCharacterid1());
            if (c != null) {
                model.addAttribute("characterPicture1", "/img/" + c.getCharacterpicture());
                List<String> lines = Arrays.asList(c.getLine1(), c.getLine2(), c.getLine3(), c.getLine4(), c.getLine5());
                lines = lines.stream().filter(s -> s != null && !s.isEmpty()).toList();
                model.addAttribute("characterLine", lines.isEmpty() ? "……" : lines.get(new Random().nextInt(lines.size())));
            } else {
                model.addAttribute("characterPicture1", "/img/placeholder.png");
                model.addAttribute("characterLine", "……");
            }
        } else {
            model.addAttribute("characterPicture1", "/img/placeholder.png");
            model.addAttribute("characterLine", "……");
        }

        // --- 戦績情報 ---
        // アイテム進捗
        int totalItems = (int) itemRepository.count();
        int ownedItems = userItemRepository.findByUsername(username).size();
        model.addAttribute("ownedItemCount", ownedItems);
        model.addAttribute("totalItemCount", totalItems);

        // キャラクター進捗（ドロップ＋ガチャ）
        List<Character> allChars = characterRepository.findByAvailabilityIn(Arrays.asList("ドロップ", "ガチャ"));
        int totalChars = allChars.size();

        List<OwnedCharacter> owned = ownedCharacterRepository.findByUsername(username);
        long distinctOwned = owned.stream()
                .map(OwnedCharacter::getOwnedcharacter)
                .distinct()
                .count();

        model.addAttribute("ownedCharCount", distinctOwned);
        model.addAttribute("totalCharCount", totalChars);

     // --- 攻略中マップ ---
        model.addAttribute("currentMap", u.getUserinformation());


        return "home";
    }


    
    @GetMapping("/adminhome")
    public String adminHome(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");

        // ログインチェック
        if (username == null || !"admin".equals(username)) {
            return "redirect:/login"; // admin以外はログイン画面へ
        }

        model.addAttribute("username", username);
        return "adminhome";
    }
    
    
    @GetMapping("/playerList")
    public String playerList(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");

        // 管理者チェック
        if (username == null || !"admin".equals(username)) {
            return "redirect:/login";
        }

        List<user> users = userService.findAllUsers();
        model.addAttribute("users", users);

        return "playerList";
    }

    @PostMapping("/updatePlayer")
    public String updatePlayer(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String havefood,
                               @RequestParam String havefund,
                               @RequestParam String havematerial,
                               @RequestParam String userinformation,
                               HttpSession session) {

        String adminUser = (String) session.getAttribute("username");
        if (adminUser == null || !"admin".equals(adminUser)) {
            return "redirect:/login";
        }

        user u = userService.findByUsername(username);
        u.setPassword(password);
        u.setHavefood(havefood);
        u.setHavefund(havefund);
        u.setHavematerial(havematerial);
        u.setUserinformation(userinformation);
        userService.save(u);

        return "redirect:/playerList";
    }
    @PostMapping("/deletePlayer")
    public String deletePlayer(@RequestParam String username, HttpSession session) {
        String adminUser = (String) session.getAttribute("username");
        if (adminUser == null || !"admin".equals(adminUser)) {
            return "redirect:/login";
        }

        user u = userService.findByUsername(username);
        if (u != null) {
            userService.delete(u);
        }

        return "redirect:/playerList";
    }

    
    @GetMapping("/admincharactercatalog")
    public String characterCatalog(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null || !"admin".equals(username)) {
            return "redirect:/login";
        }

        List<Character> characters = characterService.findAllCharacters();
        model.addAttribute("characters", characters);

        return "characterCatalog";
    }

    @PostMapping("/updateCharacter")
    public String updateCharacter(
            @RequestParam int characterid,
            @RequestParam String charactername,
            @RequestParam int fightstyle,
            @RequestParam String Affiliation,
            @RequestParam String explanation,
            @RequestParam String characterpicture,
            @RequestParam int initialhp,
            @RequestParam int initialstrength,
            @RequestParam int charactertype,
            @RequestParam int useresource,
            @RequestParam int usemoney,
            @RequestParam int usefood,
            @RequestParam String cutinpicture,
            @RequestParam int totalexp,
            @RequestParam int level,
            @RequestParam String giveline,
            @RequestParam String line1,
            @RequestParam String line2,
            @RequestParam String line3,
            @RequestParam String line4,
            @RequestParam String line5,
            @RequestParam String availability,
            @RequestParam int demolitionresource,
            @RequestParam int demolitionmoney,
            @RequestParam int demolitionfood,
            HttpSession session) {

        String adminUser = (String) session.getAttribute("username");
        if (adminUser == null || !"admin".equals(adminUser)) {
            return "redirect:/login";
        }

        Character c = characterService.findById(characterid);
        if (c != null) {
            c.setCharactername(charactername);
            c.setFightstyle(fightstyle);
            c.setAffiliation(Affiliation);
            c.setExplanation(explanation);
            c.setCharacterpicture(characterpicture);
            c.setInitialhp(initialhp);
            c.setInitialstrength(initialstrength);
            c.setCharactertype(charactertype);
            c.setUseresource(useresource);
            c.setUsemoney(usemoney);
            c.setUsefood(usefood);
            c.setCutinpicture(cutinpicture);
            c.setTotalexp(totalexp);
            c.setLevel(level);
            c.setGiveline(giveline);
            c.setLine1(line1);
            c.setLine2(line2);
            c.setLine3(line3);
            c.setLine4(line4);
            c.setLine5(line5);
            c.setAvailability(availability);
            c.setDemolitionresource(demolitionresource);
            c.setDemolitionmoney(demolitionmoney);
            c.setDemolitionfood(demolitionfood);

            characterService.save(c);
        }

        return "redirect:/admincharactercatalog";
    }

    
    @PostMapping("/deleteCharacter")
    public String deleteCharacter(@RequestParam int characterid, HttpSession session) {
        String adminUser = (String) session.getAttribute("username");
        System.out.println("[DEBUG] Admin user: " + adminUser + ", characterid: " + characterid);

        if (adminUser == null || !"admin".equals(adminUser)) {
            return "redirect:/login";
        }

        characterService.deleteCharacterById(characterid);
        System.out.println("[DEBUG] deleteCharacterById called for id=" + characterid);

        return "redirect:/admincharactercatalog";
    }



    
    @GetMapping("/adminitemCatalog")
    public String adminItemCatalog(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null || !"admin".equals(username)) {
            return "redirect:/login";
        }

        // 全アイテムと所持フラグを取得
        List<ItemCatalogDto> items = itemCatalogService.getAllItemsWithOwnership(username);
        model.addAttribute("items", items);

        return "adminitemCatalog"; // adminitemCatalog.html を表示
    }

 // アイテム更新
    @PostMapping("/updateItem")
    public String updateItem(
            @RequestParam int itemId,
            @RequestParam String itemName,
            @RequestParam String itemImage,
            @RequestParam String itemPicture,
            HttpSession session) {

        String adminUser = (String) session.getAttribute("username");
        if (adminUser == null || !"admin".equals(adminUser)) {
            return "redirect:/login";
        }

        var item = itemRepository.findById(itemId).orElse(null);
        if (item != null) {
            item.setItemname(itemName);
            item.setItemimage(itemImage);
            item.setItempicture(itemPicture);
            itemRepository.save(item);
        }

        return "redirect:/adminitemCatalog";
    }

    // アイテム削除
    @PostMapping("/deleteItem")
    public String deleteItem(@RequestParam int itemId, HttpSession session) {
        String adminUser = (String) session.getAttribute("username");
        if (adminUser == null || !"admin".equals(adminUser)) {
            return "redirect:/login";
        }

        var item = itemRepository.findById(itemId).orElse(null);
        if (item != null) {
            itemRepository.delete(item);
        }

        return "redirect:/adminitemCatalog";
    }
    
    
    //アイテム新規登録
    @GetMapping("/createItem")
    public String showCreateItemForm(Model model) {
        model.addAttribute("item", new Item()); // Item エンティティ
        return "createItem";
    }

    @PostMapping("/createItem")
    public String createItem(@ModelAttribute Item item, Model model) {
        boolean success = itemCatalogService.createItem(item); // 新規作成サービス
        if (!success) {
            model.addAttribute("errorMessage", "IDが重複しているため作成できません。");
            return "createItem";
        }
        return "redirect:/createItem";
    }

    
    @GetMapping("/createCharacter")
    public String showCreateCharacterForm(Model model) {
        model.addAttribute("character", new Character());
        return "createCharacter";
    }

    // 作成処理
    @PostMapping("/createCharacter")
    public String createCharacter(@ModelAttribute Character character, Model model) {
        boolean success = characterService.createCharacter(character);
        if (!success) {
            model.addAttribute("errorMessage", "IDが重複しているため作成できません。");
            return "createCharacter";
        }
        return "redirect:/createCharacter";
    }
    
    @GetMapping("/adminMapCatalog")
    public String mapCatalog(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null || !"admin".equals(username)) {
            return "redirect:/login";
        }

        List<Maps> maps = mapService.findAllMaps(); // DI されたインスタンスを使う
        model.addAttribute("maps", maps);
        return "adminMapCatalog";
    }

    @PostMapping("/updateMap")
    public String updateMap(@RequestParam int mapid,
                            @RequestParam String mapname,
                            @RequestParam String enemy1,
                            @RequestParam String enemy2,
                            @RequestParam String enemy3,
                            @RequestParam String enemy4,
                            @RequestParam String enemy5,
                            @RequestParam String enemy6,
                            @RequestParam int giveresource,
                            @RequestParam int givemoney,
                            @RequestParam int givefood,
                            @RequestParam String giveitem,
                            @RequestParam long exp,
                            HttpSession session) {

        String adminUser = (String) session.getAttribute("username");
        if (adminUser == null || !"admin".equals(adminUser)) {
            return "redirect:/login";
        }

        Maps map = mapService.findById(mapid);
        if (map != null) {
            map.setMapname(mapname);
            map.setEnemy1(enemy1);
            map.setEnemy2(enemy2);
            map.setEnemy3(enemy3);
            map.setEnemy4(enemy4);
            map.setEnemy5(enemy5);
            map.setEnemy6(enemy6);
            map.setGiveresource(giveresource);
            map.setGivemoney(givemoney);
            map.setGivefood(givefood);
            map.setGiveitem(giveitem);
            map.setExp(exp);

            mapService.save(map);
        }

        return "redirect:/adminMapCatalog";
    }

    @PostMapping("/deleteMap")
    public String deleteMap(@RequestParam int mapid, HttpSession session) {
        String adminUser = (String) session.getAttribute("username");
        if (adminUser == null || !"admin".equals(adminUser)) {
            return "redirect:/login";
        }

        mapService.deleteMapById(mapid);
        return "redirect:/adminMapCatalog";
    }
    
    //マップ新規
    @GetMapping("/createMap")
    public String showCreateMapForm(Model model) {
        model.addAttribute("map", new Maps());
        return "createMap";
    }

    @PostMapping("/createMap")
    public String createMap(@ModelAttribute Maps map, Model model) {
        boolean success = mapService.createMap(map); // createMap を MapService に実装しておく
        if (!success) {
            model.addAttribute("errorMessage", "IDが重複しているため作成できません。");
            return "createMap";
        }
        return "redirect:/createMap";
    }

    
   


    
}



