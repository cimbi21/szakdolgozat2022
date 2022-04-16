package hu.game.codenames.agent.distance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DistanceMatrix {
    private final String matrixName;
    private final List<String> vocab;
    private final Map<String, Map<String, Double>> matrix;

    public DistanceMatrix(String matrixName) throws IOException {
        this.matrixName = matrixName;
        this.vocab = readVocab();
        this.matrix = readMatrix();
    }

    public Map<String, Map<String, Double>> getMatrix() {
        return matrix;
    }

    public List<String> getVocab() {
        return vocab;
    }

    /**
     * read the header of the distance matrix file
     * @return List of all words
     * @throws IOException problems of input file
     */
    public List<String> readVocab() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(this.matrixName));
        String header = br.readLine();
        br.close();

        String[] elements = header.split(",");
        return new ArrayList<>(Arrays.asList(elements).subList(1, elements.length));
    }

    private Map<String, Map<String, Double>> readMatrix() throws IOException{
        Map<String, Map<String, Double>> mapMap = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(this.matrixName));

        System.out.println("Reading distance matrix...");
        br.readLine(); // the first line is a header
        String line = br.readLine();

        while(line != null) {
            String[] elements = line.split("[,;]");
            String word = elements[0];

            // create a distance map between all words in vocab
            Map<String, Double> map = new HashMap<>();
            for(int i=0; i<elements.length - 1; i++) {
                double dist;
                try {
                    dist =  Double.parseDouble(elements[i + 1]);
                } catch (Exception e) {
                    dist = Double.POSITIVE_INFINITY;
                }
                map.put(vocab.get(i), dist);
            }
            mapMap.put(word, map);

            line = br.readLine();
        }
        br.close();
        return mapMap;
    }
}
