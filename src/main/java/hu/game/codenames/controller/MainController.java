package hu.game.codenames.controller;

import hu.game.codenames.CodeNamesApplication;
import hu.game.codenames.agent.distance.BoardDistance;
import hu.game.codenames.agent.distance.BoardDistanceFromMatrix;
import hu.game.codenames.agent.game.board.Board;
import hu.game.codenames.agent.game.board.BoardRandom;
import hu.game.codenames.agent.game.board.Card;
import hu.game.codenames.agent.game.board.ColorCounts;
import hu.game.codenames.agent.game.play.CooperativeGame;
import hu.game.codenames.agent.game.play.PlayForSpymasterEvaluation;
import hu.game.codenames.model.GameConfig;
import hu.game.codenames.model.User;
import hu.game.codenames.service.Database;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

@Controller
public class MainController {

    @GetMapping("/index")
    public String initPage(){
        return "index";
    }
    @PostMapping("/index")
    public String initPostPage(){
        return "index";
    }
    @GetMapping("/settings")
    public String settings(Model model){
        GameConfig c= new GameConfig("hu","fasttext", "scoreKoyyalaguntaRestrict", 2);
        model.addAttribute("config", c);
        return "game_settings";
    }
/*
    @GetMapping("/game")
    public String initGame(Model model, HttpServletRequest request) throws Exception {
        ColorCounts cc = new ColorCounts(13,9,0,0,3);
        Board board = new BoardRandom("data/hu/hu_all_board_words.txt", cc);
        BoardDistance bd = new BoardDistanceFromMatrix(CodeNamesApplication.huFasttextSim, board.getWords());
        CooperativeGame coop = new CooperativeGame(board, bd,"scoreHarmonicDivide",2);
        coop.addNewClue();
        model.addAttribute("game", coop);
        request.getSession().setAttribute("game", coop);
        return "cooperative_game";
    }
    @PostMapping("/game")
    public String game(@ModelAttribute Card k, Model model, HttpServletRequest request) throws Exception {
        CooperativeGame coop = (CooperativeGame) request.getSession().getAttribute("game");
        coop.game(k);
        model.addAttribute("game", coop);
        request.getSession().setAttribute("game", coop);
        return "cooperative_game";
    }*/
    /*
   @PostMapping("/reveal")
    public String megjelenit(Model model, HttpServletRequest request) throws IOException, SQLException, URISyntaxException {
        Database db = new Database();
        PlayForSpymasterEvaluation play = (PlayForSpymasterEvaluation) request.getSession().getAttribute("game");
        play.unlock = false;
        for(int i=0;i<play.board.getCards().size();i++){
            if(play.board.getCards().get(i).marked) {
                play.board.getCards().get(i).setRevealed();
            }if(play.solution!=null && play.solution.contains(play.board.getCards().get(i).word)){
                play.board.getCards().get(i).setBot();
            }
        }
        db.save(play.info);
        play.end=true;
        model.addAttribute("game", play);
        return "cooperative_game";
    }*/
   /* @PostMapping("/felfed")
    public String felfed(Model model, HttpServletRequest request) {
        PlayForSpymasterEvaluation play = (PlayForSpymasterEvaluation) request.getSession().getAttribute("game");
        play.unlock = true;
        model.addAttribute("game", play);
        return "solution";
    }
    @PostMapping("/vissza")
    public String vissza(Model model, HttpServletRequest request) {
        PlayForSpymasterEvaluation play = (PlayForSpymasterEvaluation) request.getSession().getAttribute("game");
        play.unlock = false;
        model.addAttribute("game", play);
        return "minigame";
    }
    */

}
