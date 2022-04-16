package hu.game.codenames.agent.game.play;

import com.fasterxml.jackson.databind.JsonSerializer;
import hu.game.codenames.agent.game.board.Board;
import hu.game.codenames.agent.game.board.BoardFromFile;
import hu.game.codenames.agent.game.players.*;
import hu.game.codenames.service.Database;
import hu.game.codenames.service.Info;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PlayForSpymasterEvaluation extends Play{
    private final List<Integer> order;
    private int status;
    public boolean min;
    public boolean unlock;
    public int numberOfBoards;
    public int numberOfBots;
    private final String wordsFile;
    private final String colorsFile;
    private final String cluesFile;
    private final String solutionFile;
    public Board board;
    public Clue newClue;
    public List<String> solution;

    public PlayForSpymasterEvaluation(int allBoards,int allBots, String wordsFile, String colorsFile, String cluesFile, String solutionFile) {
        List<Integer> order = new ArrayList<>();
        for (int i=0; i < allBoards; i++){
            order.add(i);
        }
        Collections.shuffle(order);
        this.numberOfBoards = allBoards;
        this.numberOfBots = allBots;
        this.min = false;
        this.unlock = false;
        this.order = order;
        this.status = 0;
        this.wordsFile = wordsFile;
        this.colorsFile = colorsFile;
        this.cluesFile = cluesFile;
        this.solutionFile = solutionFile;

    }
    public PlayForSpymasterEvaluation(int allBoards,int allBots, String wordsFile, String colorsFile, String cluesFile) {
        List<Integer> order = new ArrayList<>();
        for (int i=0; i < allBoards; i++){
            order.add(i);
        }
        Collections.shuffle(order);
        this.numberOfBoards = allBoards;
        this.numberOfBots = allBots;
        this.min = false;
        this.unlock = false;
        this.order = order;
        this.status = 0;
        this.wordsFile = wordsFile;
        this.colorsFile = colorsFile;
        this.cluesFile = cluesFile;
        this.solutionFile = "without_solution_file";

    }

    public void addNewClue() throws Exception{
        Random rand = new Random();
        Database db = new Database();
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        int boardId= rand.nextInt(numberOfBoards);
        while(db.check(boardId, attr.getSessionId()) && db.numberOfGame(attr.getSessionId())<=numberOfBoards){
           boardId = rand.nextInt(numberOfBoards);
        }
        SpymasterFromFile sff = new SpymasterFromFile(cluesFile, boardId);
        this.numberOfBots = sff.NumberOfClues();
        this.info = new Info(rand.nextInt(numberOfBots),boardId,attr.getSessionId());
        this.board = new BoardFromFile(info.boardId, wordsFile, colorsFile);
        this.newClue = sff.giveClue(info.clueId);
        if(!this.solutionFile.equals("without_solution_file")){
            this.solution = sff.solution(boardId, info.clueId, solutionFile);
        }
        this.status++;

    }
    public void play() throws Exception {
        addNewClue();
        guesserRound(newClue, new GuesserUser(info.board), info.board, 2);
    }
}
