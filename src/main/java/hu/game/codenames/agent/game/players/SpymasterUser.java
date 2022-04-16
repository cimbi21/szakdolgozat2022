package hu.game.codenames.agent.game.players;

import hu.game.codenames.agent.game.board.Board;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class SpymasterUser extends Spymaster{
    private final Board board;
    private final List<String> vocab;

    public SpymasterUser(Board board, List<String> vocab) {
        this.board = board;
        this.vocab = vocab;
    }

    @Override
    public Clue giveClue(int num) {
        Scanner sc = new Scanner(System.in);
        String clueWord;
        int clueNumber = num;

        System.out.println();
        System.out.println("Only spymasters watch the next board!");
        System.out.println("Type \"y\" when ready.");
        String input = "";
        while (!input.equals("y")) {
            input = sc.nextLine();
        }

        clearScreen();
        board.printForSpymasters();
        System.out.println();

        if (num == -1) {
            // spymaster gives a clue -- unspecified clue number
            while (true) {
                System.out.println("The next spymaster: please type a clue in format <word>,<number> (e.g. dream,3)");
                input = sc.nextLine();
                String[] pieces = input.split("[,;]");
                clueWord = pieces[0];
                try {
                    clueNumber = Integer.parseInt(pieces[1]);
                } catch (InputMismatchException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.err.println("Wrong format, please try again!");
                    continue;
                }
                if (clueWord.length() <=0){
                    System.err.println("Wrong format, please try again!");
                    continue;
                }
                if (clueNumber <= 0){
                    System.err.println("Please give a positive number!");
                    continue;
                }
                if (board.getWords().contains(clueWord)) {
                    System.err.println("The clue word must not be on the board!");
                    continue;
                }
                // the clue word must be in the vocabulary
                if (vocab.contains(clueWord)) break;
                else System.err.println("Sorry, this word is not in the vocabulary. Please give another one.");
            }
        } else {
            // spymaster gives a clue -- specified clue number
            while (true) {
                System.out.println("The next spymaster: please type a clue word! The fixed clue number is " + num + ".");
                try {
                    clueWord = sc.nextLine();
                } catch (Exception e) {
                    System.err.println("Something went wrong, please try again.");
                    continue;
                }
                if (board.getWords().contains(clueWord)) {
                    System.err.println("The clue word must not be on the board!");
                    continue;
                }
                // the clue word must be in the vocabulary
                if (vocab.contains(clueWord)) break;
                else System.err.println("Sorry, this word is not in the vocabulary. Please give another one.");
            }
        }

        clearScreen();
        return new Clue(clueWord, clueNumber);
    }
}
