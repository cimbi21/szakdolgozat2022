package hu.game.codenames.model;

import hu.game.codenames.agent.distance.BoardDistance;
import hu.game.codenames.agent.game.board.Board;
import hu.game.codenames.agent.game.board.Card;
import hu.game.codenames.agent.game.board.ColorCounts;
import hu.game.codenames.agent.game.players.Clue;
import hu.game.codenames.agent.game.players.Guesser;
import hu.game.codenames.agent.game.players.Spymaster;
import hu.game.codenames.agent.game.players.SpymasterAgentSim;
import hu.game.codenames.service.GameInfoService;
import hu.game.codenames.service.GuessService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CooperativeGame {
    private final Spymaster spymaster;
    private Guesser guesser;
    private final int team;
    public final Board board;
    public Clue newClue;
    public int score;
    private int tipp;
    private int status;
    public boolean end;
    public boolean lose;
    public GameInfo info;
    private Guess g;



    public CooperativeGame(Board board, BoardDistance bd, GameInfo inf) throws Exception {
        this.board = board;
        this.status = 0;
        this.tipp = 0;
        this.end = false;
        this.lose = false;
        this.info = inf;
        this.info.setScore(10);
        this.info.setGreenCardNum(0);
        this.info.setGreyCardNum(0);
        this.info.setPassNum(0);
        this.info.setStart(LocalDateTime.now());
        List<String> vocab = bd.getVocab();
        // check if board is correct
        ColorCounts cc = board.getCounts();
        if (cc.green == 0 && cc.red == 0 && cc.purple > 0) this.team = 4;
        else if (cc.green == 0 && cc.red > 0 && cc.purple == 0) this.team = 3;
        else if (cc.green > 0 && cc.red == 0 && cc.purple == 0) this.team = 2;
        else throw new Exception("The board is not a cooperative game board.");
        this.spymaster = new SpymasterAgentSim(bd, board, team, info.getFunc());
        this.g=new Guess();
    }




    public void addNewClue() throws Exception {
        this.status++;
        this.newClue = spymaster.giveClue(info.getClueNum());
    }

    public void pass() throws Exception {
        this.info.setScore(info.getScore()-5);
        this.info.setPassNum(info.getPassNum()+1);

        this.addNewClue();
    }

    public void game(Card k) throws Exception {
        int ind = this.board.getWords().indexOf(k.word);
        if (this.newClue.number > 0 && !this.board.getCards().get(ind).marked && !this.end) {
            this.g=new Guess();
            g.setClue(newClue.word);
            g.setGuess(k.word);
            g.setLang(info.getLang());
            g.setFunc(info.getFunc());
            g.setMatrix(info.getMatrix());
            this.board.getCards().get(ind).marked = true;
            this.board.getCards().get(ind).setRevealed();
            if (this.board.getCards().get(ind).color == 2) {
                this.info.setGreenCardNum(info.getGreenCardNum()+1);
                this.info.setScore(info.getScore()+50);
                this.newClue.number--;
                this.tipp++;
                if (this.tipp == this.board.getCounts().green) {
                    this.end = true;
                    info.setWin(true);
                }
            }
           else if (this.board.getCards().get(ind).color == 1) {
                this.info.setGreyCardNum(info.getGreyCardNum()+1);
                this.info.setScore(info.getScore()-10);
                this.addNewClue();
            }
            else if (this.board.getCards().get(ind).color == 0) {
                this.info.setScore(info.getScore()-100);
                this.lose = true;
                this.end = true;
                info.setWin(false);
            }
        }
        if(this.newClue.number<=0 && !this.end){
            this.addNewClue();
        }
    }
}
