package hu.game.codenames.service;

import hu.game.codenames.agent.game.board.Board;
import hu.game.codenames.agent.game.players.Clue;

import java.sql.Timestamp;

public class Info {
    public String[] tipps;
    public Timestamp begin;
    public Timestamp end;
    public int clueId;
    public String sessionId;
    public int boardId;
    public int tipp;
    public Board board;

    public Info(int clueId,int boardId, String sessionId ) {
        this.clueId = clueId;
        this.sessionId = sessionId;
        this.boardId = boardId;
        this.tipps = new String[12];
        this.begin = new Timestamp(System.currentTimeMillis());
        this.tipp=0;
    }

}
