package hu.game.codenames.agent;

import hu.game.codenames.agent.distance.*;
import hu.game.codenames.agent.game.board.*;
import hu.game.codenames.agent.game.play.*;
import hu.game.codenames.agent.game.players.*;


public class MainTest {
    public static void main(String[] args) throws Exception {
        ColorCounts cc = new ColorCounts(13,9,0,0,3);
        Board board = new BoardRandom("data/hu/hu_all_board_words.txt", cc);
       BoardDistance bd = new BoardDistanceFromFile("data/hu/matrix/hu_fasttext.csv", board.getWords());
       Play coop = new PlayCooperative(board, bd,"scoreHarmonicDivide",-1);
       coop.play();


    }
}
