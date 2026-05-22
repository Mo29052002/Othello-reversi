package model;

import java.util.*;

/**
 * @class GameState
 * @brief Représente l'état global d'une partie d'Othello/Reversi.
 *
 * Cette classe gère le plateau, les deux joueurs, le joueur courant,
 * le statut de fin de partie, le gagnant éventuel ainsi que l'historique
 * des coups joués.
 */
public class GameState {

    /**
     * @brief Plateau de jeu associé à cette partie.
     *
     * Contient la disposition actuelle des pions sur la grille.
     */
    private Board board;

    /**
     * @brief Premier joueur de la partie, associé aux pions noirs.
     */
    private Player player1; 

    /**
     * @brief Second joueur de la partie, associé aux pions blancs.
     */
    private Player player2; 

    /**
     * @brief Joueur devant jouer au tour courant.
     *
     * Il s'agit soit de player1 (noir), soit de player2 (blanc).
     */
    private Player currentPlayer;

    /**
     * @brief Indique si la partie est terminée.
     *
     * À true lorsque plus aucun coup valide n'est possible ou lorsque
     * les règles de fin de partie sont satisfaites.
     */
    private boolean gameOver;

    /**
     * @brief Gagnant de la partie lorsque celle-ci est terminée.
     *
     * Vaut null si la partie n'est pas terminée ou si aucun gagnant
     * n'a été déterminé (par exemple en cas d'égalité).
     */
    private Player winner;

    /**
     * @brief Historique des coups joués depuis le début de la partie.
     *
     * Chaque élément représente un coup, ce qui permet d'implémenter
     * des fonctionnalités d'annulation/reprise (undo/redo).
     */
    private List<Move> moveHistory; 

    /**
     * @brief Construit un nouvel état de jeu avec un plateau de taille donnée.
     *
     * Initialise le plateau, crée les deux joueurs (noir et blanc),
     * définit le joueur courant (noir commence), marque la partie comme
     * non terminée et prépare l'historique des coups.
     *
     * @param boardSize Taille du plateau (nombre de cases par côté).
     */
    public GameState(int boardSize) {
        board = new Board(boardSize);
        player1 = new Player(Cell.CellState.BLACK);
        player2 = new Player(Cell.CellState.WHITE);
        currentPlayer = player1; // Black starts
        gameOver = false;
        winner = null;
        moveHistory = new ArrayList<>();
    }

    /**
     * @brief Construit un nouvel état de jeu avec un plateau de taille par défaut.
     *
     * Utilise la taille standard de 8 cases par côté pour le plateau.
     * Ce constructeur est fourni pour la compatibilité avec un usage classique.
     */
   
    public GameState() {
        this(8);
    }

    /**
     * @brief Retourne le plateau de jeu associé à cet état.
     *
     * @return L'objet {@link Board} représentant le plateau actuel.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * @brief Retourne le premier joueur (pions noirs).
     *
     * @return L'objet {@link Player} représentant le joueur noir.
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     * @brief Retourne le second joueur (pions blancs).
     *
     * @return L'objet {@link Player} représentant le joueur blanc.
     */
    public Player getPlayer2() {
        return player2;
    }

    /**
     * @brief Retourne le joueur actuellement actif.
     *
     * @return L'objet {@link Player} qui doit jouer au tour courant.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @brief Définit le joueur courant à partir d'une couleur.
     *
     * Si la couleur passée correspond à celle de player1, celui-ci devient
     * le joueur courant ; sinon, c'est player2 qui devient le joueur courant.
     *
     * @param color Couleur du joueur à définir comme joueur courant
     *              (généralement {@link Cell.CellState#BLACK} ou
     *              {@link Cell.CellState#WHITE}).
     */
    public void setCurrentPlayer(Cell.CellState color) {
        currentPlayer = (player1.getColor() == color) ? player1 : player2;
    }

    /**
     * @brief Change le joueur courant pour l'autre joueur.
     *
     * Si le joueur courant est player1, il devient player2, et inversement.
     * Cette méthode est typiquement appelée après qu'un coup valide a été joué.
     */
    public void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    /**
     * @brief Ajoute un coup à l'historique de la partie.
     *
     * Permet d'enregistrer chaque coup joué pour pouvoir, par exemple,
     * annuler des coups ou rejouer la partie.
     *
     * @param move Coup à ajouter à la liste d'historique.
     */
    public void addMove(Move move) {
        moveHistory.add(move);
    }

    /**
     * @brief Indique si la partie est terminée.
     *
     * @return true si la partie est finie, false sinon.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * @brief Met à jour le statut de fin de partie.
     *
     * Cette méthode permet de marquer la partie comme terminée ou non
     * en fonction de la logique de jeu externe.
     *
     * @param gameOver true pour indiquer que la partie est terminée,
     *                 false pour indiquer qu'elle est encore en cours.
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * @brief Retourne le gagnant de la partie si elle est terminée.
     *
     * @return L'objet {@link Player} gagnant, ou null si aucun gagnant
     *         n'est défini (partie en cours ou égalité).
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * @brief Définit le gagnant de la partie.
     *
     * @param winner Joueur à déclarer gagnant de la partie,
     *               ou null pour indiquer qu'il n'y a pas de gagnant.
     */
    public void setWinner(Player winner) {
        this.winner = winner;
    }

    /**
     * @brief Retourne l'historique des coups joués.
     *
     * @return La liste des objets {@link Move} représentant tous les coups
     *         enregistrés depuis le début de la partie.
     */
    public List<Move> getMoveHistory() {
        return moveHistory;
    }

    /**
     * @brief Met à jour les scores des deux joueurs en fonction du plateau.
     *
     * Cette méthode recompte le nombre de pions noirs et blancs présents sur
     * le plateau et met à jour le score de chaque joueur en conséquence.
     */
    public void updateScores() {
        player1.setScore(board.countPieces(Cell.CellState.BLACK));
        player2.setScore(board.countPieces(Cell.CellState.WHITE));
    }
}