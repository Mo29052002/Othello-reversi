package model;

import java.util.List;

/**
 * Represents a move in the Othello/Reversi game.
 * Includes the position and the player making the move.
 */
public class Move {
    private final int x;
    private final int y;
    private final Player player;
    private List<int[]> capturedPieces; // List of [x,y] positions captured

    public Move(int x, int y, Player player) {
        this.x = x;
        this.y = y;
        this.player = player;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Player getPlayer() {
        return player;
    }

    public List<int[]> getCapturedPieces() {
        return capturedPieces;
    }

    public void setCapturedPieces(List<int[]> capturedPieces) {
        this.capturedPieces = capturedPieces;
    }
}