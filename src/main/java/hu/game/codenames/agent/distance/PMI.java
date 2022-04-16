package hu.game.codenames.agent.distance;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * operations on PMI file and PMI graph
 */
public class PMI {
    private final List<String> vocab;
    private final Graph<String, DefaultWeightedEdge> pmiGraph;

    public List<String> getVocab() {
        return vocab;
    }

    public Graph<String, DefaultWeightedEdge> getPmiGraph() {
        return pmiGraph;
    }

    public PMI(String filename) throws IOException {
        this.pmiGraph = createGraph(filename);
        this.vocab = graphVocab();
    }


    /**
     * create PMI graph from a list of PMI values using JGraphT
     * @return PMI graph
     */
    private Graph<String, DefaultWeightedEdge> createGraph(String filename) throws IOException {

        System.out.println("Creating PMI graph...");

        BufferedReader br = new BufferedReader(new FileReader(filename));

        SimpleWeightedGraph<String, DefaultWeightedEdge> g
                = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        br.readLine(); // first line is header
        String row = br.readLine();

        while(row != null) {

            // one edge from every row
            String[] elements = row.split(",");
            String word1 = elements[0];
            String word2 = elements[1];
            double pmi = Double.parseDouble(elements[2]);

            if (!word1.equals(word2)) {
                g.addVertex(word1);
                g.addVertex(word2);
                DefaultWeightedEdge e = g.addEdge(word1, word2);
                // e == null if the edge already exists
                if (e != null) g.setEdgeWeight(e, 1 / pmi);
            }

            row = br.readLine();
        }

        br.close();
        return g;
    }

    /**
     * @return the vertex set as a list
     */
    private List<String> graphVocab(){
        Set<String> words = pmiGraph.vertexSet();
        return new ArrayList<>(words);
    }
}
