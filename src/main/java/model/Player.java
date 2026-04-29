package model;

/**
 * Represents a player in the Othello/Reversi game.
 * Each player has a color and a score.
 */
public class Player {
    private final Cell.CellState color;
    private int score;

    public Player(Cell.CellState color) {
        this.color = color;
        this.score = 2; // Initial score for Othello
    }

    public Cell.CellState getColor() {
        return color;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void incrementScore() {
        score++;
    }

    public void decrementScore() {
        score--;
    }

    public boolean isBlack() {
        return color == Cell.CellState.BLACK;
    }

    public boolean isWhite() {
        return color == Cell.CellState.WHITE;
    }
}