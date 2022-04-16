package hu.game.codenames.agent.game.players;

import hu.game.codenames.agent.distance.BoardDistance;
import hu.game.codenames.agent.game.board.Board;
import hu.game.codenames.agent.game.board.Card;

import java.util.Map;
import java.util.Random;

public class GuesserAgent extends Guesser{
    private final Board board;
    private final Map<String, Map<String , Double>> distances;

    public GuesserAgent(BoardDistance bd, Board board) {
        this.board = board;
        this.distances = bd.getBoardDistances();
    }

    @Override
    public String guess(Clue clue) {
        Map<String, Double> distanceMap = distances.get(clue.word);

        // ignore the revealed cards
        for (Card card : board.getCards())
            if (card.isRevealed()) distanceMap.remove(card.getWord());

        // find the word closest to the clue word
        String word = null;
        Double dist = Double.POSITIVE_INFINITY;
        for (Map.Entry<String, Double> entry : distanceMap.entrySet())
            if (entry.getValue() < dist) {
                dist = entry.getValue();
                word = entry.getKey();
            }

        // in case there's no connected word, make a random guess
        if (word == null){
            System.out.println("No idea, just a random guess...");
            Random r = new Random();
            word = board.getWords().get(r.nextInt(board.getWords().size()));
        }

        System.out.println("The guess of the guesser agent is " + word);
        return word;
    }
}
