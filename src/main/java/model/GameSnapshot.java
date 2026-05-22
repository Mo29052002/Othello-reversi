package model;

/**
 * @class GameSnapshot
 * @brief Représente un instantané de l'état d'une partie d'Othello/Reversi.
 *
 * Cette classe capture toutes les informations nécessaires pour décrire
 * un état précis de la partie : le coup joué pour atteindre cet état,
 * l'état du plateau, le joueur dont c'est le tour, les scores et le numéro du coup.
 * Elle peut être utilisée pour l'historique de la partie, l'annulation de coups,
 * ou la sauvegarde/restauration de l'état du jeu.
 */
public class GameSnapshot {

    /**
     * @brief Coup ayant conduit à cet état de la partie.
     *
     * Il s'agit généralement du dernier coup joué avant de capturer cet instantané.
     * Peut servir à reconstruire l'historique de la partie.
     */
    private Move move;

    /**
     * @brief Copie de l'état du plateau au moment de l'instantané.
     *
     * Tableau 2D contenant, pour chaque case, l'état correspondant
     * (vide, pion noir ou pion blanc).
     */
    private Cell.CellState[][] boardState;

    /**
     * @brief Taille du plateau au moment de l'instantané.
     *
     * Correspond au nombre de cases par côté (par exemple 8 pour un plateau classique).
     */
    private int boardSize;

    /**
     * @brief Joueur devant jouer au moment de cet instantané.
     *
     * Indique la couleur du prochain joueur (noir ou blanc).
     */
    private Cell.CellState currentPlayer;

    /**
     * @brief Score actuel du joueur noir.
     *
     * Représente le nombre de pions noirs présents sur le plateau à cet instant.
     */
    private int blackScore;

    /**
     * @brief Score actuel du joueur blanc.
     *
     * Représente le nombre de pions blancs présents sur le plateau à cet instant.
     */
    private int whiteScore;

    /**
     * @brief Numéro du coup correspondant à cet instantané.
     *
     * Par exemple, 1 pour le premier coup, 2 pour le deuxième, etc.
     */
    private int moveNumber;

    /**
     * @brief Construit un nouvel instantané à partir des informations fournies.
     *
     * Le constructeur copie l'état du plateau pour éviter toute modification
     * ultérieure liée à la référence d'origine. Il enregistre également le coup
     * joué, la taille du plateau, le joueur courant, les scores et le numéro du coup.
     *
     * @param move Coup joué pour arriver à cet état de la partie.
     * @param boardState État actuel du plateau sous forme de tableau 2D
     *                   des états de chaque case.
     * @param boardSize Taille du plateau (nombre de cases par côté).
     * @param currentPlayer Joueur devant jouer après cet instantané
     *                      (généralement noir ou blanc).
     * @param blackScore Score du joueur noir à cet instant.
     * @param whiteScore Score du joueur blanc à cet instant.
     * @param moveNumber Numéro du coup dans la partie correspondant à cet état.
     */
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

    /**
     * @brief Crée une copie profonde de l'état du plateau passé en paramètre.
     *
     * Cette méthode recopie les références des états de chaque case dans un
     * nouveau tableau 2D afin que l'instantané ne soit pas modifié si l'état
     * du plateau d'origine change par la suite.
     *
     * @param original Tableau 2D représentant l'état original du plateau.
     * @return Un nouveau tableau 2D contenant une copie des états de chaque case.
     */
    private Cell.CellState[][] copyBoardState(Cell.CellState[][] original) {
        Cell.CellState[][] copy = new Cell.CellState[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, original[i].length);
        }
        return copy;
    }

    /**
     * @brief Retourne le coup ayant conduit à cet instantané.
     *
     * @return L'objet {@link Move} associé à cet état de la partie.
     */
    public Move getMove() {
        return move;
    }

    /**
     * @brief Retourne une copie de l'état du plateau à cet instant.
     *
     * Un nouveau tableau 2D est retourné pour garantir que l'état stocké
     * dans l'instantané reste immuable depuis l'extérieur.
     *
     * @return Un tableau 2D de {@link Cell.CellState} représentant l'état du plateau.
     */
    public Cell.CellState[][] getBoardState() {
        return copyBoardState(boardState);
    }

    /**
     * @brief Retourne la taille du plateau associée à cet instantané.
     *
     * @return La taille du plateau (nombre de cases par côté).
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * @brief Retourne le joueur devant jouer après cet instantané.
     *
     * @return La couleur du joueur courant, sous forme de {@link Cell.CellState}.
     */
    public Cell.CellState getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @brief Retourne le score du joueur noir.
     *
     * @return Le nombre de pions noirs sur le plateau à cet instant.
     */
    public int getBlackScore() {
        return blackScore;
    }

    /**
     * @brief Retourne le score du joueur blanc.
     *
     * @return Le nombre de pions blancs sur le plateau à cet instant.
     */
    public int getWhiteScore() {
        return whiteScore;
    }

    /**
     * @brief Retourne le numéro du coup correspondant à cet instantané.
     *
     * @return Le numéro du coup dans la partie.
     */
    public int getMoveNumber() {
        return moveNumber;
    }

    /**
     * @brief Restaure l'état du plateau fourni à partir de cet instantané.
     *
     * Cette méthode parcourt la copie interne de l'état du plateau et
     * place les pions correspondants sur le plateau passé en paramètre.
     * Elle ne modifie pas le joueur courant ni les scores en dehors de ce plateau.
     *
     * @param board Plateau sur lequel l'état de l'instantané doit être restauré.
     */
    public void restoreToBoard(Board board) {
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                board.placePiece(x, y, boardState[x][y]);
            }
        }
    }
}