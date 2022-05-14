package hu.game.codenames.model;

import lombok.Data;

@Data
public class GameConfig {
    String lang;
    String matrix;
    String func;
    int clueNum;
}
