package hu.game.codenames.controller;

import hu.game.codenames.model.GameInfo;
import hu.game.codenames.service.GameInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class MainController {

    @GetMapping("/")
    public String initPage(){
        return "index";
    }

    @GetMapping("/rules")
    public String rules(){
        return "about_game";
    }

    @GetMapping("/bots")
    public String bots(){
        return "about_bots";
    }

    @PostMapping("/")
    public String initPostPage(){
        return "index";
    }

    @GetMapping("/settings")
    public String settings(Model model){
        GameInfo c= new GameInfo();
        c.setLang("hu");
        c.setMatrix("FastText");
        c.setFunc("Harmonic");
        c.setClueNum(2);
        model.addAttribute("config", c);
        return "game_settings";
    }

}
