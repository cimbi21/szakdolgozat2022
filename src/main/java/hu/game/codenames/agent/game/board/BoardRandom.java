package hu.game.codenames.agent.game.board;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BoardRandom extends Board{
    private final List<Card> cards;
    private final ColorCounts counts;
    private final List<String> possibleWords;

    public BoardRandom(String wordsFile, ColorCounts counts) throws IOException {
        this.counts = counts;
        this.possibleWords = readWords(wordsFile);
        this.cards = randomCards(counts);
    }

    @Override
    public List<String> getWords() {
        List<String> words = new ArrayList<>();
        for (Card c: cards){
            words.add(c.getWord());
        }
        return words;
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    @Override
    public ColorCounts getCounts() {
        return counts;
    }

    private List<String> readWords(String wordsFile) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(wordsFile));
        List<String> vocab = new ArrayList<>();

        String line = br.readLine();

        while(line != null) {
            vocab.add(line);
            line = br.readLine();
        }
        br.close();
        return vocab;
    }

    private List<Card> randomCards(ColorCounts counts){
        Random r = new Random();
        int randomSeed = r.nextInt(500); // 500 different boards
        System.out.println("ID of random board: " + randomSeed);

        // shuffle words
        List<String> words = new ArrayList<>(possibleWords);
        Collections.shuffle(words, new Random(randomSeed));

        // shuffle colors
        List<Integer> colors = new ArrayList<>();
        for (int i=0; i<counts.black; i++) colors.add(0);
        for (int i=0; i<counts.neutral; i++) colors.add(1);
        for (int i=0; i<counts.green; i++) colors.add(2);
        for (int i=0; i<counts.red; i++) colors.add(3);
        for (int i=0; i<counts.purple; i++) colors.add(4);
        Collections.shuffle(colors, new Random(randomSeed));

        List<Card> cards = new ArrayList<>();
        for (int i=0; i<counts.all; i++){
            cards.add(new Card(words.get(i), colors.get(i)));
        }

        return cards;
    }
}
