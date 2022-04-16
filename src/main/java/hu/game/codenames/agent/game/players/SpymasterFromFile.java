package hu.game.codenames.agent.game.players;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SpymasterFromFile extends Spymaster{
    private final int boardId;
    private final Map<Integer, List<Clue>> cluesMap;

    public SpymasterFromFile(String cluesFilename, int boardId) throws IOException {
        this.boardId = boardId;

        BufferedReader br = new BufferedReader(new FileReader(cluesFilename));
       // br.readLine(); // first line is a header
        String line = br.readLine();
        Map<Integer, List<Clue>> cluesMap = new HashMap<>();

        while(line != null) {
            String[] elements = line.split("[,;]");
            List<Clue> row = new ArrayList<>();

            int id = Integer.parseInt(elements[0]);
            for (int i = 1; i < elements.length; i += 2) {
                row.add(new Clue(elements[i], Integer.parseInt(elements[i + 1])));
            }
            cluesMap.put(id, row);
            line = br.readLine();
        }

        this.cluesMap = cluesMap;
    }

    public int NumberOfClues(){return cluesMap.get(boardId).size();}

    @Override
    public Clue giveClue(int clueId){
        List<Clue> clues = cluesMap.get(boardId);
        return clues.get(clueId);
    }

    public List<String> solution(int boardId, int clueId, String txt) throws IOException {
        List<String> row = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(txt));
        br.readLine(); // first line is a header
        String line = br.readLine();
        while(line != null) {
            String[] elements = line.split("[,;]");
            int bId = Integer.parseInt(elements[0]);
            int cId = Integer.parseInt(elements[1]);
            if(bId==boardId && cId == clueId) {
                for (int i = 4; i < elements.length; i++) {
                    row.add(elements[i]);
                }
                break;
            }
            line = br.readLine();
        }
        return row;
    }


}
