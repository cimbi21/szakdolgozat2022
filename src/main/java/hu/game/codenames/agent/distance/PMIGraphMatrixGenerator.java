package hu.game.codenames.agent.distance;

import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.*;
import java.util.*;


public class PMIGraphMatrixGenerator {
    /**
     * find distance of words from the PMI graph
     * @return map-matrix of distances
     */
    public static Map<String, Map<String, Double>> getPathWeights(String filename) throws IOException {
        Map<String, Map<String, Double>> distanceMatrix = new HashMap<>();
        PMI pmi = new PMI(filename);
        List<String> words = pmi.getVocab();

        System.out.println("Computing shortest paths...");
        FloydWarshallShortestPaths<String, DefaultWeightedEdge> paths =
                new FloydWarshallShortestPaths<>(pmi.getPmiGraph());

        // path weights between all word pairs
        for (String w: words) {
            Map<String, Double> distances = new HashMap<>();
            for (String v : words){
                distances.put(v, paths.getPath(w, v).getWeight());
            }
            distanceMatrix.put(w, distances);
        }

        return distanceMatrix;
    }

    /**
     * Save distance matrix to csv file
     * @param args path to the PMI file
     * @throws IOException PMI file in proper format is required
     */
    public static void main(String[] args) throws IOException {
        Map<String, Map<String, Double>> distanceMatrix = getPathWeights(args[0]);
        List<String> words = new ArrayList<>(distanceMatrix.keySet());

        System.out.println("Writing distances to file: PMI_graph_matrix.csv");

        // write distance matrix to csv file
        BufferedWriter writer = new BufferedWriter(new FileWriter("PMI_graph_matrix.csv"));

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

