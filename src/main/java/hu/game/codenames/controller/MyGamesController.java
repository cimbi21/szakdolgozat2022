package hu.game.codenames.controller;


import hu.game.codenames.service.GameInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyGamesController {
    @Autowired
    private GameInfoService gameInfoService;

    @GetMapping("/mygames")
    public String myGames(Model model){
        model.addAttribute("games",gameInfoService.myGameInfos(SecurityContextHolder.getContext().getAuthentication().getName()));
        return "my_games";
    }
    @PostMapping("/mygames")
    public String MyGamesPost(Model model){
        model.addAttribute("games",gameInfoService.myGameInfos(SecurityContextHolder.getContext().getAuthentication().getName()));
        return "my_games";
    }

}
