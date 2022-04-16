package hu.game.codenames.agent.game.play;

import hu.game.codenames.agent.distance.BoardDistance;
import hu.game.codenames.agent.game.board.Board;
import hu.game.codenames.agent.game.board.Card;
import hu.game.codenames.agent.game.board.ColorCounts;
import hu.game.codenames.agent.game.players.Clue;
import hu.game.codenames.agent.game.players.Guesser;
import hu.game.codenames.agent.game.players.Spymaster;
import hu.game.codenames.agent.game.players.SpymasterAgentSim;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;


public class CooperativeGame {
    private final Spymaster spymaster;
    private Guesser guesser;
    private final int team;
    public final Board board;
    private int clueNum;
    public Clue newClue;
    public boolean min;
    public boolean unlock;
    public int score;
    private int tipp;
    private int status;
    public boolean end;
    public boolean lose;


    public CooperativeGame(Board board, BoardDistance bd, String scoreFunction, int clueNum) throws Exception {
        this.board = board;
        this.clueNum = clueNum;
        this.min = false;
        this.unlock = false;
        this.status = 0;
        this.score = 10;
        this.tipp = 0;
        this.end = false;
        this.lose = false;
        List<String> vocab = bd.getVocab();
        // check if board is correct
        ColorCounts cc = board.getCounts();
        if (cc.green == 0 && cc.red == 0 && cc.purple > 0) this.team = 4;
        else if (cc.green == 0 && cc.red > 0 && cc.purple == 0) this.team = 3;
        else if (cc.green > 0 && cc.red == 0 && cc.purple == 0) this.team = 2;
        else throw new Exception("The board is not a cooperative game board.");
        this.spymaster = new SpymasterAgentSim(bd, board, team, scoreFunction);
    }




    public void addNewClue() throws Exception {
        this.status++;
        this.newClue = spymaster.giveClue(clueNum);
    }

    public void pass() throws Exception {
        this.score-=5;
        this.addNewClue();
    }

    public void game(Card k) throws Exception {
        int ind = this.board.getWords().indexOf(k.word);
        if (this.newClue.number > 0 && !this.board.getCards().get(ind).marked && !this.end) {
            this.board.getCards().get(ind).marked = true;
            this.board.getCards().get(ind).setRevealed();
            if (this.board.getCards().get(ind).color == 2) {
                this.score+=50;
                this.newClue.number--;
                this.tipp++;
                if (this.tipp == this.board.getCounts().green) {
                    this.end = true;
                }
            }
           else if (this.board.getCards().get(ind).color == 1) {
               this.score-=10;
                this.addNewClue();
            }
            else if (this.board.getCards().get(ind).color == 0) {
                this.score-=100;
                this.lose = true;
                this.end = true;
            }

        }
        if(this.newClue.number<=0 && !this.end){
            this.addNewClue();
        }
    }
}
