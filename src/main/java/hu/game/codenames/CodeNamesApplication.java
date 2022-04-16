package hu.game.codenames;

import hu.game.codenames.agent.distance.BoardDistance;
import hu.game.codenames.agent.distance.BoardDistanceFromFile;
import hu.game.codenames.agent.distance.DistanceMatrix;
import hu.game.codenames.agent.game.board.Board;
import hu.game.codenames.agent.game.board.BoardRandom;
import hu.game.codenames.agent.game.board.ColorCounts;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class CodeNamesApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(CodeNamesApplication.class, args);


    }

}
