package hu.game.codenames.agent.game.players;

import hu.game.codenames.agent.game.board.Board;

import java.util.Scanner;

public class GuesserUser extends Guesser{
    private final Board board;

    public GuesserUser(Board board){
        this.board = board;
    }

    /**
     * get a guess from human players
     * @param clue the spymasers's clue
     * @return the guessed word from the board
     */
    @Override
    public String guess (Clue clue){
        Scanner sc = new Scanner(System.in);
        String word;
        while (true) {
            board.printForGuessers();
            System.out.println();
            System.out.println("Your spymaster's message: " + clue.word + ", " + clue.number);
            System.out.println("Type a word from the board!");
            try {
                word = sc.nextLine();
            } catch (Exception e) {
                System.err.println("Something went wrong, please try again.");
                continue;
            }
            if (board.getWords().contains(word)) {
                if (!board.getCards().get(board.getWords().indexOf(word)).isRevealed()) {
                    break;
                } else System.err.println("This word is already revealed!");
            } else System.err.println("This word is not on the board!");
        }
        return word;
    }
}
