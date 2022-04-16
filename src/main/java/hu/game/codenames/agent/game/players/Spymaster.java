package hu.game.codenames.agent.game.players;

public abstract class Spymaster {
    public abstract Clue giveClue(int num) throws Exception;

    /**
     * clear previous prints
     */
    public static void clearScreen() {
        for (int i = 0; i < 15; i++) System.out.println();
        System.out.flush();
    }
}
