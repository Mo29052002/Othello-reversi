package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Othello/Reversi game board.
 * Manages the 8x8 grid of cells and game state.
 */
public class Board {
    public static final int SIZE = 8;
    private Cell[][] grid;

    public Board() {
        grid = new Cell[SIZE][SIZE];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                grid[x][y] = new Cell(x, y);
            }
        }
        // Initial setup: center four pieces
        grid[3][3].setState(Cell.CellState.WHITE);
        grid[3][4].setState(Cell.CellState.BLACK);
        grid[4][3].setState(Cell.CellState.BLACK);
        grid[4][4].setState(Cell.CellState.WHITE);
    }

    public Cell getCell(int x, int y) {
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
            return null;
        }
        return grid[x][y];
    }

    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE;
    }

    public void placePiece(int x, int y, Cell.CellState state) {
        if (isValidPosition(x, y)) {
            grid[x][y].setState(state);
        }
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public int countPieces(Cell.CellState state) {
        int count = 0;
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (grid[x][y].getState() == state) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean isFull() {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (grid[x][y].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
}