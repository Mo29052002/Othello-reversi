package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Othello/Reversi game board.
 * Manages the grid of cells and game state.
 */
public class Board {
    private int size;
    private Cell[][] grid;

    public Board(int size) {
        this.size = size;
        grid = new Cell[size][size];
        initializeBoard();
    }

    // Default constructor for backward compatibility
    public Board() {
        this(8);
    }

    private void initializeBoard() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                grid[x][y] = new Cell(x, y);
            }
        }
        // Initial setup: center four pieces
        int center = size / 2;
        grid[center - 1][center - 1].setState(Cell.CellState.WHITE);
        grid[center - 1][center].setState(Cell.CellState.BLACK);
        grid[center][center - 1].setState(Cell.CellState.BLACK);
        grid[center][center].setState(Cell.CellState.WHITE);
    }

    public Cell getCell(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size) {
            return null;
        }
        return grid[x][y];
    }

    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
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
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (grid[x][y].getState() == state) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean isFull() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (grid[x][y].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getSize() {
        return size;
    }
}