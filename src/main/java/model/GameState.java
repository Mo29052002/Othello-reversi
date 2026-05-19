package model;
import java.util.*;

/**
 * Represents the overall state of the Othello/Reversi game.
 * Manages the board, players, and current game status.
 */
public class GameState {
    private Board board;
    private Player player1; // Black
    private Player player2; // White
    private Player currentPlayer;
    private boolean gameOver;
    private Player winner;
    private List<Move> moveHistory; // For undo/redo functionality

    public GameState(int boardSize) {
        board = new Board(boardSize);
        player1 = new Player(Cell.CellState.BLACK);
        player2 = new Player(Cell.CellState.WHITE);
        currentPlayer = player1; // Black starts
        gameOver = false;
        winner = null;
        moveHistory = new ArrayList<>();
    }

    // Default constructor for backward compatibility
    public GameState() {
        this(8);
    }

    public Board getBoard() {
        return board;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Cell.CellState color) {
        currentPlayer = (player1.getColor() == color) ? player1 : player2;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    public void addMove(Move move) {
        moveHistory.add(move);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public List<Move> getMoveHistory() {
        return moveHistory;
    }

    public void updateScores() {
        player1.setScore(board.countPieces(Cell.CellState.BLACK));
        player2.setScore(board.countPieces(Cell.CellState.WHITE));
    }
}