package hu.game.codenames.agent.game.play;

import hu.game.codenames.agent.distance.BoardDistance;
import hu.game.codenames.agent.game.board.Board;
import hu.game.codenames.agent.game.board.BoardFromFile;
import hu.game.codenames.agent.game.board.ColorCounts;
import hu.game.codenames.agent.game.players.*;
import hu.game.codenames.service.Database;
import hu.game.codenames.service.Info;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class PlayCooperative extends Play{
    private final Spymaster spymaster;
    private  Guesser guesser;
    private final int team;
    public final Board board;
    private int clueNum;
    public Clue newClue;
    public boolean min;
    public boolean unlock;



    public PlayCooperative(Board board, BoardDistance bd, String scoreFunction, int clueNum) throws Exception {
        this.board = board;
        this.clueNum = clueNum;
        this.min = false;
        List<String> vocab = bd.getVocab();

        // check if board is correct
        ColorCounts cc = board.getCounts();
        if (cc.green == 0 && cc.red == 0 && cc.purple > 0) this.team = 4;
        else if (cc.green == 0 && cc.red > 0 && cc.purple == 0) this.team = 3;
        else if (cc.green > 0 && cc.red == 0 && cc.purple == 0) this.team = 2;
        else throw new Exception("The board is not a cooperative game board.");

        // ask user about roles, assign attributes
        Scanner sc = new Scanner(System.in);
        String option = "";

        while (!option.equals("1") && !option.equals("2")){
            System.out.println("Choose one of the following options:");
            System.out.println("1 - I play as a guesser with a spymaster agent.");
            System.out.println("2 - I play as a spymaster with a guesser agent.");
            System.out.println("Please type 1 or 2.");
            option = sc.nextLine();
        }

        this.spymaster = option.equals("1") ?  new SpymasterAgentSim(bd, board, team, scoreFunction) : new SpymasterUser(board, vocab);
        //this.spymaster = new SpymasterAgentSim(bd, board, team, scoreFunction);
        this.guesser = option.equals("1") ? new GuesserUser(board) : new GuesserAgentDist(bd, board);
    }

    public void addNewClue() throws Exception{
        this.newClue = spymaster.giveClue(clueNum);
        System.out.println(newClue);
    }
    public void play() throws Exception {
        do {
            Clue clue = spymaster.giveClue(clueNum);
            guesserRound(clue, guesser, board, team);
        } while (!super.end);
    }
}