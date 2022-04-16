package hu.game.codenames.agent.game.play;

import hu.game.codenames.agent.game.board.Board;
import hu.game.codenames.agent.game.players.Clue;
import hu.game.codenames.agent.game.players.Guesser;
import hu.game.codenames.service.Info;

public abstract class Play {
    public Info info;
    public boolean end = false;

    public abstract void play() throws Exception;

    public void guesserRound(Clue clue, Guesser guesser, Board board, int team) {
        int num = clue.number;

        for (int i=0; i<num; i++) {
            // what is the guess
            String guess = guesser.guess(clue);

            // reveal the word
            int idx = board.getWords().indexOf(guess);
            board.getCards().get(idx).setRevealed();

            // consequences of revealing
            int color = board.getCards().get(idx).getColor();
            if (color == team) {
                System.out.println("Correct!");
                if (board.unrevealed(team) <= 0) {
                    System.out.println("Success!");
                    String colorTeam = color == 2 ? "blue" : color == 3 ? "red" : "purple";
                    System.out.println("The winner is the " + colorTeam + " team!");
                    this.end = true;
                    break;
                }
            } else if (color == 1) {
                System.out.println("This word is neutral.");
                System.out.println("The round ends now.");
                break;
            } else if (color == 0) {
                System.out.println("This card is the assassin!!!");
                System.out.println("Game over.");
                this.end = true;
                break;
            } else {
                System.out.println("This card belongs to the other team!");
                if (board.unrevealed(color) <= 0) {
                    String colorTeam = color == 2 ? "blue" : color == 3 ? "red" : "purple";
                    System.out.println("The winner is the " + colorTeam + " team!");
                    this.end = true;
                    break;
                }
            }
        }
        //board.printForGuessers();
    }
}
