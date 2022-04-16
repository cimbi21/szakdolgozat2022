package hu.game.codenames.agent.distance;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * given a distance matrix filename and a list of board words,
 * this class creates distance maps from all vocabulary words to the words on the board
 * if you have enough free memory and want high speed, use BoardDistanceFromMatrix instead
 */
public class BoardDistanceFromFile extends BoardDistance{
    private final String matrixName;
    private final List<String> vocab;
    private final List<String> boardWords;
    private final Map<String, Map<String, Double>> boardDistances;

    /**
     *
     * @param matrixName path to a distance matrix file
     * @param boardWords list of board words
     * @throws IOException problems of input file
     */
    public BoardDistanceFromFile(String matrixName, List<String> boardWords) throws IOException {
        this.matrixName = matrixName;
        this.vocab = readVocab();
        this.boardWords = boardWords;
        this.boardDistances = setBoardDistances();
    }

    public List<String> getVocab() {
        return vocab;
    }

    public Map<String, Map<String, Double>> getBoardDistances() {
        return boardDistances;
    }

    /**
     * read the header of the distance matrix file
     * @return List of all words
     * @throws IOException problems of input file
     */
    private List<String> readVocab() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(this.matrixName));
        String header = br.readLine();
        br.close();

        String[] elements = header.split(",");
        return new ArrayList<>(Arrays.asList(elements).subList(1, elements.length));
    }


    /**
     * get a map for each word, containing its distance from the board words
     * @return Map<String, Map<String, Double>> a map for each vocab word, containing its distance from the board words
     * @throws IOException problems of input file
     */
    private Map<String, Map<String, Double>> setBoardDistances() throws IOException {

        Map<String, Map<String, Double>> mapMap = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(this.matrixName));

        System.out.println("Reading distance matrix...");
        br.readLine(); // the first line is a header
        String line = br.readLine();

        while(line != null) {
            String[] elements = line.split(",");
            String word = elements[0];

            // create a distance map from the board words to all words in vocab
            if (boardWords.contains(word)){
                Map<String, Double> map = new HashMap<>();
                for(int i=0; i<elements.length - 1; i++) {
                    try {
                        double dist =  Double.parseDouble(elements[i + 1]);
                        map.put(vocab.get(i), dist);
                    } catch (Exception e) {
                        map.put(vocab.get(i), Double.POSITIVE_INFINITY);
                    }

                }
                mapMap.put(word, map);
            }

            line = br.readLine();
        }
        br.close();
        if (mapMap.size() != boardWords.size()) {
            throw new EOFException("Not all board words are in the word distance matrix!");
        }

        // transpose the mapMap -> from all words to board words
        Map<String, Map<String, Double>> boardDistances = new HashMap<>();
        for (String word : vocab){
            boardDistances.put(word, new HashMap<>());
        }

        for (String boardWord : boardWords){
            for (Map.Entry<String, Double> entry : (mapMap.get(boardWord)).entrySet()){
                String word = entry.getKey();
                Double distance = entry.getValue();
                (boardDistances.get(word)).put(boardWord, distance);
            }
        }

        return boardDistances;
    }

}