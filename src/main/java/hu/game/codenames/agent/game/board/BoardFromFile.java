package hu.game.codenames.agent.game.board;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BoardFromFile extends Board{
    private final List<Card> cards;

    public BoardFromFile(int boardId, String wordsFile, String colorsFile) throws IOException {
        this.cards = cardsFromFiles(boardId, wordsFile, colorsFile);
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    @Override
    public ColorCounts getCounts() {
        ColorCounts cc = new ColorCounts();
        for (Card card: cards){
            cc.all++;
            switch (card.getColor()) {
                case 0 : cc.black++;
                case 1 : cc.neutral++;
                case 2 : cc.green++;
                case 3 : cc.red++;
                case 4 : cc.purple++;
            }
        }
        return cc;
    }

    @Override
    public List<String> getWords() {
        List<String> words = new ArrayList<>();
        for (Card c: cards){
            words.add(c.getWord());
        }
        return words;
    }

    private List<Card> cardsFromFiles(int boardId, String wordsFile, String colorsFile) throws IOException{
        BufferedReader brWords = new BufferedReader(new FileReader(wordsFile));
        BufferedReader brColors = new BufferedReader(new FileReader(colorsFile));

        String lineWords;
        String[] words = new String[0];
        int wordsId = -1;
        while (wordsId != boardId){
            lineWords = brWords.readLine();
            if (lineWords == null) throw new EOFException("BoardID not found in BoardWords input file.");
            words = lineWords.split("[,;]");
            wordsId = Integer.parseInt(words[0]);
        }

        String lineColors;
        String[] colors = new String[0];
        int colorsId = -1;
        while (colorsId != boardId){
            lineColors = brColors.readLine();
            if (lineColors == null) throw new EOFException("BoardID not found in BoardColors input file.");
            colors = lineColors.split("[,;]");
            colorsId = Integer.parseInt(colors[0]);
        }

        if (colors.length != words.length){
            throw new IOException("BoardColors and BoardWords files must have the same number of columns!");
        }

        List<Card> cards = new ArrayList<>();
        for (int i=1; i<colors.length; i++){
            cards.add(new Card(words[i], Integer.parseInt(colors[i])));
        }

        return cards;
    }
}
