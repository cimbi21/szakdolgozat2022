package hu.game.codenames.agent.game.players;

import hu.game.codenames.agent.distance.BoardDistance;
import hu.game.codenames.agent.game.board.Board;
import hu.game.codenames.agent.game.board.Card;
import hu.game.codenames.agent.game.players.Clue;
import hu.game.codenames.agent.game.players.Spymaster;

import java.util.*;

public class SpymasterAgentSim extends Spymaster {
    private final Board board;
    private final Map<String, Map<String, Double>> sims;
    private final int team;
    private final String scoreFunction;
    private final List<String> used;

    public SpymasterAgentSim(BoardDistance bd, Board board, int team, String scoreFunction) {
        this.board = board;
        this.team = team;
        this.scoreFunction = scoreFunction;
        this.used = new ArrayList<>();

        Map<String, Map<String, Double>> sims = bd.getBoardDistances();
        Map<String, Map<String, Double>> simsCopy = new HashMap<>(Map.copyOf(sims));
        // remove board words from possible clue words
        for (String word : sims.keySet()){
            for (String boardWord: board.getWords()){
                if (word.contains(boardWord)) {
                    simsCopy.remove(word);
                }
            }
        }
        this.sims = simsCopy;
    }

    @Override
    public Clue giveClue(int num) {

        Map<String, Double[]> clueMap;
            if(Objects.equals(scoreFunction, "scoreKoyyalagunta")) clueMap = scoreKoyyalagunta(num, 0.5);
            else if(Objects.equals(scoreFunction, "scoreKoyyalaguntaRestrict")) clueMap =  scoreKoyyalaguntaRestrict(num, 0.5);
            else if(Objects.equals(scoreFunction, "scoreHarmonic")) clueMap =  scoreHarmonic(num, 0.5);
            else if(Objects.equals(scoreFunction, "scoreHarmonicDivide")) clueMap =  scoreHarmonicDivide(num, num);
            else clueMap = null;
        assert clueMap != null;

        // return the word with the best score and the matching number
        String bestWord = null;
        double maxScore = Double.NEGATIVE_INFINITY;
        for (Map.Entry<String, Double[]> entry : clueMap.entrySet()) {
            if (entry.getValue()[0] > maxScore && !(this.used.contains(entry.getKey()))) {
                maxScore = entry.getValue()[0];
                bestWord = entry.getKey();
            }
        }

        int clueNum = clueMap.get(bestWord)[1].intValue();
        System.out.println("The spymaster's message is " + bestWord + ", " + clueNum);
        this.used.add(bestWord);
        return new Clue(bestWord, clueNum);
    }

    /**
     * "scoreKoyyalagunta" scoring function for clues
     * map a score and a number to each possible word in the vocab,
     * @param num number of targeted board words, must be specified
     * @return a map with each word to [score, matching number]
     */
    private Map<String, Double[]> scoreKoyyalagunta(int num, double lambda){
        Map<String, Double[]> wordScores = new HashMap<>();

        // go over all vocab words as possible clue word
        for (Map.Entry<String, Map<String, Double>> entry : sims.entrySet()) {
            String word = entry.getKey();
            Map<String, Double> distanceMap = entry.getValue();

            // for each possible clue word, find the maximal similarity of bad words,
            // and the similarities of good words
            List<Double> goodWordsSims = new LinkedList<>();
            Double maxBadSim = 0.0;

            for (Card card : board.getCards()){
                if (!card.isRevealed()) {
                    String cardWord = card.getWord();
                    if (card.getColor() == team) {
                        goodWordsSims.add((distanceMap.get(cardWord) == null) ? 0.0 : distanceMap.get(cardWord));
                    }
                    else if (distanceMap.get(cardWord) != null && maxBadSim < distanceMap.get(cardWord))
                        maxBadSim = distanceMap.get(cardWord);
                }
            }


            double score = 0.0;
            int number;

            // score function:
            // (the sum of n best good similarities) - lambda x maxBadSim
            goodWordsSims.sort(Collections.reverseOrder());
            number = Math.min(num, goodWordsSims.size());

            for (int i=0; i<number; i++)
                score += goodWordsSims.get(i);

            score -= lambda * maxBadSim;

            wordScores.put(word, new Double[] {score, (double) number});
        }

        return wordScores;
    }

    /**
     * "scoreKoyyalaguntaRestrict" scoring function for clues
     * map a score and a number to each possible word in the vocab.
     * @param num number of targeted board words, must be specified
     * @return a map with each word to [score, matching number]
     */
    private Map<String, Double[]> scoreKoyyalaguntaRestrict(int num, double lambda){
        Map<String, Double[]> wordScores = new HashMap<>();

        // go over all vocab words as possible clue word
        for (Map.Entry<String, Map<String, Double>> entry : sims.entrySet()) {
            String word = entry.getKey();
            Map<String, Double> distanceMap = entry.getValue();

            // for each possible clue word, find the maximal similarity of bad words,
            // and the similarities of good words
            List<Double> goodWordsSims = new LinkedList<>();
            Double maxBadSim = 0.0;

            for (Card card : board.getCards()){
                if (!card.isRevealed()) {
                    String cardWord = card.getWord();
                    if (card.getColor() == team) {
                        goodWordsSims.add((distanceMap.get(cardWord) == null) ? 0.0 : distanceMap.get(cardWord));
                    }
                    else if (distanceMap.get(cardWord) != null && maxBadSim < distanceMap.get(cardWord))
                        maxBadSim = distanceMap.get(cardWord);
                }
            }


            double score = 0.0;
            int number;

            // score function:
            // (the sum of n best good similarities) - lambda x maxBadSim
            goodWordsSims.sort(Collections.reverseOrder());
            number = Math.min(num, goodWordsSims.size());

            for (int i=0; i<number; i++)
                score += goodWordsSims.get(i);

            score -= lambda * maxBadSim;

            if (goodWordsSims.get(number - 1) < maxBadSim) score = 0;
            wordScores.put(word, new Double[] {score, (double) number});
        }

        return wordScores;
    }

    /**
     * "scoreHarmonic" scoring function for clues
     * map a score and a number to each possible word in the vocab.
     * @param num number of targeted board words, must be specified
     * @return a map with each word to [score, matching number]
     */
    private Map<String, Double[]> scoreHarmonic(int num, double lambda){
        Map<String, Double[]> wordScores = new HashMap<>();
        // go over all vocab words as possible clue word
        for (Map.Entry<String, Map<String, Double>> entry : sims.entrySet()) {
            String word = entry.getKey();
            Map<String, Double> distanceMap = entry.getValue();

            // for each possible clue word, find the maximal similarity of bad words,
            // and the similarities of good words
            List<Double> goodWordsSims = new LinkedList<>();
            Double maxBadSim = 0.0;

            for (Card card : board.getCards()) {
                if (!card.isRevealed()) {
                    String cardWord = card.getWord();
                    if (card.getColor() == team) {
                        goodWordsSims.add((distanceMap.get(cardWord) == null) ? 0.0 : distanceMap.get(cardWord));
                    } else if (distanceMap.get(cardWord) != null && maxBadSim < distanceMap.get(cardWord))
                        maxBadSim = distanceMap.get(cardWord);
                }
            }


            double score = 0.0;
            int number;
            goodWordsSims.sort(Collections.reverseOrder());

            // score function:
            // (the sum of n best good similarities) - lambda x maxBadSim
            number = Math.min(num, goodWordsSims.size());

            double harmonic_mean = 0;
            for (int j=0; j<number; j++){
                harmonic_mean += 1/goodWordsSims.get(j);
            }
            harmonic_mean = number/harmonic_mean;
            if (score < harmonic_mean - lambda * maxBadSim){
                score = harmonic_mean - lambda * maxBadSim;
            }

            if (number > 0 && goodWordsSims.get(number - 1) < maxBadSim) score = 0;
            wordScores.put(word, new Double[] {score, (double) number});
        }

        return wordScores;
    }

    /**
     * "scoreHarmonicDivide" scoring function for clues
     * map a score and a number to each possible word in the vocab.
     * @param num number of targeted board words, must be specified
     * @return a map with each word to [score, matching number]
     */
    private Map<String, Double[]> scoreHarmonicDivide(int num, double lambda){
        Map<String, Double[]> wordScores = new HashMap<>();

        // go over all vocab words as possible clue word
        for (Map.Entry<String, Map<String, Double>> entry : sims.entrySet()) {
            String word = entry.getKey();
            Map<String, Double> distanceMap = entry.getValue();

            // for each possible clue word, find the maximal similarity of bad words,
            // and the similarities of good words
            List<Double> goodWordsSims = new LinkedList<>();
            Double maxBadSim = 0.0;

            for (Card card : board.getCards()) {
                if (!card.isRevealed()) {
                    String cardWord = card.getWord();
                    if (card.getColor() == team) {
                        goodWordsSims.add((distanceMap.get(cardWord) == null) ? 0.0 : distanceMap.get(cardWord));
                    } else if (distanceMap.get(cardWord) != null && maxBadSim < distanceMap.get(cardWord))
                        maxBadSim = distanceMap.get(cardWord);
                }
            }


            double score = 0.0;
            int number = 0;
            goodWordsSims.sort(Collections.reverseOrder());

            if (num != -1){
                // score function:
                // (the sum of n best good similarities) - lambda x maxBadSim
                number = Math.min(num, goodWordsSims.size());

                double harmonic_mean = 0;
                for (int j=0; j<number; j++){
                    harmonic_mean += 1/goodWordsSims.get(j);
                }
                harmonic_mean = number/harmonic_mean;

                double a = harmonic_mean / Math.max(lambda * maxBadSim, 1);
                if (score < a){
                    score = a;
                }

            } else {

                Double finalMaxBadSim = maxBadSim;
                goodWordsSims.removeIf(sim -> sim < finalMaxBadSim);

                for (int i=1; i<goodWordsSims.size(); i++){
                    double harmonic_mean = 0;
                    for (int j=0; j<i; j++){
                        harmonic_mean += 1/goodWordsSims.get(j);
                    }
                    harmonic_mean = 1/harmonic_mean;
                    double a =  harmonic_mean * i - (lambda * Math.max(maxBadSim, 1));
                    if (score < a){
                        score = a;
                        number = i;
                    }
                }

            }

            if (number > 0 && goodWordsSims.get(number - 1) < maxBadSim) score = 0;
            wordScores.put(word, new Double[] {score, (double) number});
        }

        return wordScores;
    }


}
