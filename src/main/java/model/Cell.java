package model;

/**
 * Represents a cell on the Othello/Reversi board.
 * Each cell has coordinates and a state (empty, black, or white).
 */
public class Cell {
    private final int x;
    private final int y;
    private CellState state;

    public enum CellState {
        EMPTY, BLACK, WHITE
    }

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.state = CellState.EMPTY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public CellState getState() {
        return state;
    }

    public void setState(CellState state) {
        this.state = state;
    }

    public boolean isEmpty() {
        return state == CellState.EMPTY;
    }

    public boolean isBlack() {
        return state == CellState.BLACK;
    }

    public boolean isWhite() {
        return state == CellState.WHITE;
    }
}