package hu.game.codenames.agent.distance;

import java.io.EOFException;
import java.io.IOException;
import java.util.*;

/**
 * given a distance matrix object and a list of board words,
 * this class creates distance maps from all vocabulary words to the words on the board
 * if you do not have a lot of free memory, use BoardDistanceFromFile instead
 */
public class BoardDistanceFromMatrix extends BoardDistance{
    private final DistanceMatrix distanceMatrix;
    private final List<String> vocab;
    private final List<String> boardWords;
    private final Map<String, Map<String, Double>> boardDistances;

    /**
     *
     * @param distanceMatrix distance matrix stored in memory
     * @param boardWords list of board words
     * @throws IOException problems of input file
     */
    public BoardDistanceFromMatrix(DistanceMatrix distanceMatrix, List<String> boardWords) throws IOException {
        this.distanceMatrix = distanceMatrix;
        this.vocab = distanceMatrix.getVocab();
        this.boardWords = boardWords;
        this.boardDistances = setBoardDistances();
    }

    public Map<String, Map<String, Double>> getBoardDistances() {
        return boardDistances;
    }

    public List<String> getVocab() {
        return vocab;
    }

    /**
     * get a map for each word, containing its distance from the board words
     * @return Map<String, Map<String, Double>> a map for each vocab word, containing its distance from the board words
     * @throws IOException problems of input file
     */
    private Map<String, Map<String, Double>>  setBoardDistances() throws IOException {

        Map<String, Map<String, Double>> mapMap = distanceMatrix.getMatrix();
        Map<String, Map<String, Double>> boardMap = new HashMap<>();
        for (String word: mapMap.keySet()){
            if (boardWords.contains(word)) {
                boardMap.put(word, mapMap.get(word));
            }
        }

        if (boardMap.size() != boardWords.size()) {
            throw new EOFException("Not all board words are in the word distance matrix!");
        }

        // transpose the mapMap -> from all words to board words
        Map<String, Map<String, Double>> boardDistances = new HashMap<>();
        for (String word : vocab){
            boardDistances.put(word, new HashMap<>());
        }

        for (String boardWord : boardWords){
            for (Map.Entry<String, Double> entry : (boardMap.get(boardWord)).entrySet()){
                String word = entry.getKey();
                Double distance = entry.getValue();
                (boardDistances.get(word)).put(boardWord, distance);
            }
        }

        return boardDistances;
    }

}