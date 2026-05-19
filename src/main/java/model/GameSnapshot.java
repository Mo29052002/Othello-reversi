package model;

public class GameSnapshot {
    private Move move;                    // Le coup joué pour arriver à cet état
    private Cell.CellState[][] boardState;  // Copie de l'état du plateau
    private int boardSize;                // Taille du plateau
    private Cell.CellState currentPlayer; // Qui doit jouer maintenant
    private int blackScore;               // Score noir
    private int whiteScore;               // Score blanc
    private int moveNumber;               // Numéro du coup dans la partie


    public GameSnapshot(Move move, Cell.CellState[][] boardState, int boardSize,
                        Cell.CellState currentPlayer, int blackScore, int whiteScore, int moveNumber) {
        this.move = move;
        this.boardState = copyBoardState(boardState);
        this.boardSize = boardSize;
        this.currentPlayer = currentPlayer;
        this.blackScore = blackScore;
        this.whiteScore = whiteScore;
        this.moveNumber = moveNumber;
    }

    private Cell.CellState[][] copyBoardState(Cell.CellState[][] original) {
        Cell.CellState[][] copy = new Cell.CellState[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, original[i].length);
        }
        return copy;
    }

    public Move getMove() {
        return move;
    }

    public Cell.CellState[][] getBoardState() {
        return copyBoardState(boardState);
    }

    public int getBoardSize() {
        return boardSize;
    }

    public Cell.CellState getCurrentPlayer() {
        return currentPlayer;
    }

    public int getBlackScore() {
        return blackScore;
    }

    public int getWhiteScore() {
        return whiteScore;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public void restoreToBoard(Board board) {
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                board.placePiece(x, y, boardState[x][y]);
            }
        }
    }
}
