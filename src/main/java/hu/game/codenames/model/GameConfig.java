package hu.game.codenames.model;

public class GameConfig {
    String lang;
    String matrix;
    String func;
    int clueNum;

    public GameConfig(String lang, String matrix, String func, int clueNum) {
        this.lang = lang;
        this.matrix = matrix;
        this.func = func;
        this.clueNum = clueNum;
    }

    public int getClueNum() {
        return clueNum;
    }

    public void setClueNum(int clueNum) {
        this.clueNum = clueNum;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getMatrix() {
        return matrix;
    }

    public void setMatrix(String matrix) {
        this.matrix = matrix;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }
}
