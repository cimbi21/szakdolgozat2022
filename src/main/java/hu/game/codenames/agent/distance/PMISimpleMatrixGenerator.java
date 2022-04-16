package hu.game.codenames.agent.distance;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PMISimpleMatrixGenerator {
    /**
     * @return matrix of inverse PMI
     */
    public static Map<String, Map<String, Double>> getInversePMI(String PMIFileName) throws IOException {
        Map<String, Map<String, Double>> distanceMatrix = new HashMap<>();

        BufferedReader br = new BufferedReader(new FileReader(PMIFileName));
        br.readLine(); // first line is header
        String row = br.readLine();

        while(row != null) {

            String[] elements = row.split(",");
            String word1 = elements[0];
            String word2 = elements[1];
            double pmi = Double.parseDouble(elements[2]);

            if (!word1.equals(word2)) {
                distanceMatrix.computeIfAbsent(word1, k -> new HashMap<>());
                distanceMatrix.get(word1).put(word2, 1/pmi);
            }

            row = br.readLine();
        }

        br.close();
        return distanceMatrix;
    }

    /**
     * Save distance matrix to csv file
     * @param args path to the PMI file
     * @throws IOException PMI file in proper format is required
     */
    public static void main(String[] args) throws IOException {
        Map<String, Map<String, Double>> distanceMatrix = getInversePMI(args[0]);
        List<String> words = new ArrayList<>(distanceMatrix.keySet());

        System.out.println("Writing distances to file: inverse_PMI_matrix.csv");

        // write distance matrix to csv file
        BufferedWriter writer = new BufferedWriter(new FileWriter("inverse_PMI_matrix.csv"));

        // header
        for (String w: words){
            writer.write("," + w);
        }
        writer.write("\n");

        // other rows
        for (String w: words){
            writer.write(w);
            for (String v: words){
                writer.write("," + distanceMatrix.get(w).get(v));
            }
            writer.write("\n");
            writer.flush();
        }
    }
}
