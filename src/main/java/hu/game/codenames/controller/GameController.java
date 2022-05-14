package hu.game.codenames.controller;

import hu.game.codenames.agent.distance.BoardDistance;
import hu.game.codenames.agent.distance.BoardDistanceFromFile;
import hu.game.codenames.agent.game.board.Board;
import hu.game.codenames.agent.game.board.BoardRandom;
import hu.game.codenames.agent.game.board.Card;
import hu.game.codenames.agent.game.board.ColorCounts;
import hu.game.codenames.model.CooperativeGame;
import hu.game.codenames.model.GameInfo;
import hu.game.codenames.service.GameInfoService;
import hu.game.codenames.service.GuessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameInfoService gameInfoService;

    @Autowired
    private GuessService guessService;

    @GetMapping("/init")
    public String initGame(@ModelAttribute GameInfo config, Model model, HttpServletRequest request) throws Exception {
        try {
            ColorCounts cc = new ColorCounts(13, 9, 0, 0, 3);
            Board board = new BoardRandom("data/" + config.getLang() + "/" + config.getLang() + "_all_board_words.txt", cc);
            BoardDistance bd;
            if (config.getLang().equals("hu")) {
                bd = switch (config.getMatrix()) {
                    case "npmi" -> new BoardDistanceFromFile("data/hu/matrix/hu_npmi.csv", board.getWords());
                    case "npmi2" -> new BoardDistanceFromFile("data/hu/matrix/hu_npmi2.csv", board.getWords());
                    case "graph" -> new BoardDistanceFromFile("data/hu/matrix/hu_graph.csv", board.getWords());
                    default -> new BoardDistanceFromFile("data/hu/matrix/hu_fasttext.csv", board.getWords());
                };
            } else {
                bd = switch (config.getMatrix()) {
                    case "npmi" -> new BoardDistanceFromFile("data/en/matrix/en_npmi.csv", board.getWords());
                    case "npmi2" -> new BoardDistanceFromFile("data/en/matrix/en_npmi2.csv", board.getWords());
                    case "graph" -> new BoardDistanceFromFile("data/en/matrix/en_graph.csv", board.getWords());
                    default -> new BoardDistanceFromFile("data/en/matrix/en_fasttext.csv", board.getWords());
                };
            }
            CooperativeGame coop = new CooperativeGame(board, bd, config);
            coop.addNewClue();
            model.addAttribute("game", coop);
            request.getSession().setAttribute("game", coop);
            return "cooperative_game";
        }catch (Exception e){
            return "error_page";
        }

    }

    @PostMapping("/pass")
    public String pass(Model model, HttpServletRequest request) throws Exception {
        try {
            CooperativeGame coop = (CooperativeGame) request.getSession().getAttribute("game");
            coop.pass();
            model.addAttribute("game", coop);
            request.getSession().setAttribute("game", coop);
            return "cooperative_game";
        }
        catch (Exception e){
            return "error_page";
        }

    }

    @PostMapping("")
    public String game(@ModelAttribute Card k, Model model, HttpServletRequest request) throws Exception {
        try {
            CooperativeGame coop = (CooperativeGame) request.getSession().getAttribute("game");
            coop.game(k);
            guessService.save(coop.getG());
            model.addAttribute("game", coop);
            request.getSession().setAttribute("game", coop);
            if (coop.end){
                gameInfoService.saveInfo(coop.info);
            }
            return "cooperative_game";
        }
        catch (Exception e){
            return "error_page";
        }

    }

    @GetMapping("")
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

