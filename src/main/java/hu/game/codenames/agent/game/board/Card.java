package hu.game.codenames.agent.game.board;

public class Card {
    public final String word;
    public final int color;
    public boolean revealed;
    public boolean marked;
    public boolean bot;

    public Card(String word, int color) {
        this.word = word;
        this.color = color;
        this.revealed = false;
        this.marked = false;
        this.bot = false;
    }

    public String getWord() {
        return word;
    }

    public int getColor() {
        return color;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(){
        this.revealed = true;
    }
    public void setBot() {
        this.bot = true;
    }


    public String[] markColor() {
        // braces around the word mark color when the board is printed
        if(color == 0){
           return new String[]{"!!", "!!"}; // assassin
        }
        if(color == 1){
            return new String[]{"((", "))"}; // neutral
        }
        if(color == 2){
            return  new String[]{"[[", "]]"}; // blue team
        }
        if(color == 3){
            return new String[]{"{{", "}}"}; // red team
        }
        if(color == 4){
            return new String[]{"//", "//"}; // purple team
        }
        return null;
    }

    public String toStringGuessers() {
        // for guessers: when revealed, color is visible
        return revealed ? this.markColor()[0] + word + this.markColor()[1] : word;
    }

    public String toStringSpymasters() {
        // for spymasters: when revealed, word is not visible
        return revealed ?
                this.markColor()[0] + "XXXX" + this.markColor()[1] :
                this.markColor()[0] + word + this.markColor()[1];
    }
}
