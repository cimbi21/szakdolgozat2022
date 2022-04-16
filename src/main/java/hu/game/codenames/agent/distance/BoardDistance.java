package hu.game.codenames.agent.distance;

import java.util.List;
import java.util.Map;

public abstract class BoardDistance {
    public abstract Map<String, Map<String, Double>> getBoardDistances();
    public abstract List<String> getVocab();
}
