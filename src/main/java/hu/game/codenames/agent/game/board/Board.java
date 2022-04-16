package hu.game.codenames.agent.game.board;

import java.util.List;

public abstract class Board {

    public abstract List<String> getWords();
    public abstract List<Card> getCards();
    public abstract ColorCounts getCounts();

    /**
     * get the number of unrevealed cards of a team
     * @param team number of the relevant team
     */
    public int unrevealed(int team){
        int count = 0;
        for (Card card : getCards()){
            if (!card.isRevealed() && card.getColor() == team){
                count++;
            }
        }
        return count;
    }

    /**
     * print board for guessers
     */
    public void printForGuessers(){
        for (int i=0; i < this.getCards().size(); i++){
            if (i%5 == 0) System.out.println();
            System.out.print(getCards().get(i).toStringGuessers());
            System.out.print("  ");
        }
        System.out.println("\n");
    }

    /**
     * print board for spymasters
     */
    public void printForSpymasters(){
        for (int i=0; i < getCards().size(); i++){
            if (i%5 == 0) System.out.println();
            System.out.print(getCards().get(i).toStringSpymasters());
            System.out.print("  ");
        }
        System.out.println("\n");
    }

}
