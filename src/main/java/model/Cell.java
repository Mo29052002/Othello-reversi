package model;

/**
 * @class Cell
 * @brief Représente une case du plateau de jeu Othello/Reversi.
 *
 * Chaque case est définie par ses coordonnées (x, y) sur le plateau et par
 * un état indiquant si elle est vide, occupée par un pion noir ou par un pion blanc.
 */
public class Cell {

    /**
     * @brief Coordonnée horizontale de la case sur le plateau.
     *
     * La valeur 0 correspond généralement à la première colonne du plateau.
     */
    private final int x;

    /**
     * @brief Coordonnée verticale de la case sur le plateau.
     *
     * La valeur 0 correspond généralement à la première ligne du plateau.
     */
    private final int y;

    /**
     * @brief État courant de la case.
     *
     * Cet état indique si la case est vide ou contient un pion noir ou blanc.
     */
    private CellState state;

    /**
     * @enum CellState
     * @brief Définit les différents états possibles d'une case.
     *
     * - EMPTY : la case est libre, aucun pion n'y est posé ;
     * - BLACK : la case est occupée par un pion noir ;
     * - WHITE : la case est occupée par un pion blanc.
     */
    public enum CellState {
        EMPTY,  
        BLACK,  
        WHITE   
    }

    /**
     * @brief Construit une case aux coordonnées spécifiées, initialement vide.
     *
     * Le constructeur positionne la case à la colonne x et à la ligne y du plateau,
     * et initialise son état à EMPTY.
     *
     * @param x Coordonnée horizontale de la case (colonne).
     * @param y Coordonnée verticale de la case (ligne).
     */
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.state = CellState.EMPTY;
    }

    /**
     * @brief Retourne la coordonnée horizontale de la case.
     *
     * Cette coordonnée correspond à la colonne de la case sur le plateau.
     *
     * @return La valeur de la coordonnée x de la case.
     */
    public int getX() {
        return x;
    }

    /**
     * @brief Retourne la coordonnée verticale de la case.
     *
     * Cette coordonnée correspond à la ligne de la case sur le plateau.
     *
     * @return La valeur de la coordonnée y de la case.
     */
    public int getY() {
        return y;
    }

    /**
     * @brief Retourne l'état courant de la case.
     *
     * L'état indique si la case est vide ou occupée par un pion noir ou blanc.
     *
     * @return L'état de la case sous forme de valeur de l'énumération {@link CellState}.
     */
    public CellState getState() {
        return state;
    }

    /**
     * @brief Modifie l'état de la case.
     *
     * Permet de mettre à jour la case pour indiquer qu'elle est vide ou
     * occupée par un pion d'une certaine couleur.
     *
     * @param state Nouvel état de la case (EMPTY, BLACK ou WHITE).
     */
    public void setState(CellState state) {
        this.state = state;
    }

    /**
     * @brief Indique si la case est vide.
     *
     * Une case vide ne contient aucun pion.
     *
     * @return true si l'état de la case est EMPTY, false sinon.
     */
    public boolean isEmpty() {
        return state == CellState.EMPTY;
    }

    /**
     * @brief Indique si la case contient un pion noir.
     *
     * @return true si l'état de la case est BLACK, false sinon.
     */
    public boolean isBlack() {
        return state == CellState.BLACK;
    }

    /**
     * @brief Indique si la case contient un pion blanc.
     *
     * @return true si l'état de la case est WHITE, false sinon.
     */
    public boolean isWhite() {
        return state == CellState.WHITE;
    }
}