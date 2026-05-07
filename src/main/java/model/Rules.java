package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the rules and logic for validating moves in Othello/Reversi.
 * Provides static methods for game logic.
 */
public class Rules {

    // Directions for checking captures: up, down, left, right, diagonals
    private static final int[][] DIRECTIONS = {
        {-1, 0}, {1, 0}, {0, -1}, {0, 1},
        {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
    };

    public static boolean isValidMove(Board board, int x, int y, Player player) {
        if (!board.isValidPosition(x, y) || !board.getCell(x, y).isEmpty()) {
            return false;
        }
        // Check if placing here captures at least one opponent piece
        return getCapturedPieces(board, x, y, player).size() > 0;
    }

    public static List<int[]> getCapturedPieces(Board board, int x, int y, Player player) {
        List<int[]> captured = new ArrayList<>();
        Cell.CellState opponent = getOpponentColor(player.getColor());

        for (int[] dir : DIRECTIONS) {
            List<int[]> line = checkDirection(board, x, y, dir[0], dir[1], opponent);
            if (line != null) {
                captured.addAll(line);
            }
        }
        return captured;
    }

    private static List<int[]> checkDirection(Board board, int x, int y, int dx, int dy, Cell.CellState opponent) {
        List<int[]> captured = new ArrayList<>();
        int nx = x + dx;
        int ny = y + dy;

        while (board.isValidPosition(nx, ny)) {
            Cell cell = board.getCell(nx, ny);
            if (cell.getState() == opponent) {
                captured.add(new int[]{nx, ny});
            } else if (cell.getState() == Cell.CellState.EMPTY) {
                return null; // No capture in this direction
            } else {
                // Found own color, capture valid
                return captured;
            }
            nx += dx;
            ny += dy;
        }
        return null; // Reached edge without own color
    }

    public static Cell.CellState getOpponentColor(Cell.CellState color) {
        return (color == Cell.CellState.BLACK) ? Cell.CellState.WHITE : Cell.CellState.BLACK;
    }

    public static List<Move> getValidMoves(Board board, Player player) {
        List<Move> moves = new ArrayList<>();
        int size = board.getSize();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (isValidMove(board, x, y, player)) {
                    moves.add(new Move(x, y, player));
                }
            }
        }
        return moves;
    }

    public static boolean hasValidMoves(Board board, Player player) {
        return !getValidMoves(board, player).isEmpty();
    }

    public static void applyMove(Board board, Move move) {
        board.placePiece(move.getX(), move.getY(), move.getPlayer().getColor());
        List<int[]> captured = getCapturedPieces(board, move.getX(), move.getY(), move.getPlayer());
        for (int[] pos : captured) {
            board.placePiece(pos[0], pos[1], move.getPlayer().getColor());
        }
        move.setCapturedPieces(captured);
    }
}