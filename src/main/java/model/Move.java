package model;

import java.util.List;

/**
 * @class Move
 * @brief Représente un coup joué dans une partie d'Othello/Reversi.
 *
 * Un coup est défini par la position ciblée sur le plateau (coordonnées x, y),
 * le joueur qui effectue ce coup, ainsi que la liste des pions capturés
 * (retournés) à la suite de ce coup.
 */
public class Move {

    /**
     * @brief Coordonnée horizontale de la case visée par le coup.
     *
     * La valeur 0 correspond généralement à la première colonne du plateau.
     */
    private final int x;

    /**
     * @brief Coordonnée verticale de la case visée par le coup.
     *
     * La valeur 0 correspond généralement à la première ligne du plateau.
     */
    private final int y;

    /**
     * @brief Joueur qui effectue ce coup.
     *
     * Permet de connaître la couleur du pion posé (noir ou blanc).
     */
    private final Player player;

    /**
     * @brief Liste des positions des pions capturés par ce coup.
     *
     * Chaque élément du tableau représente une position [x, y] d'un pion
     * adverse qui sera retourné à la suite de ce coup.
     */
    private List<int[]> capturedPieces;

    /**
     * @brief Construit un coup à partir de la position et du joueur.
     *
     * Ce constructeur initialise un coup en indiquant la case ciblée
     * sur le plateau et le joueur qui joue ce coup.
     *
     * @param x Coordonnée horizontale de la case jouée.
     * @param y Coordonnée verticale de la case jouée.
     * @param player Joueur qui effectue ce coup.
     */
    public Move(int x, int y, Player player) {
        this.x = x;
        this.y = y;
        this.player = player;
    }

    /**
     * @brief Retourne la coordonnée horizontale de la case jouée.
     *
     * @return La valeur de la coordonnée x.
     */
    public int getX() {
        return x;
    }

    /**
     * @brief Retourne la coordonnée verticale de la case jouée.
     *
     * @return La valeur de la coordonnée y.
     */
    public int getY() {
        return y;
    }

    /**
     * @brief Retourne le joueur qui a effectué ce coup.
     *
     * @return L'objet {@link Player} associé à ce coup.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @brief Retourne la liste des pions capturés par ce coup.
     *
     * Chaque élément est un tableau de deux entiers [x, y] indiquant
     * la position d'un pion capturé sur le plateau.
     *
     * @return La liste des positions des pions capturés, ou null si
     *         aucune liste n'a encore été définie.
     */
    public List<int[]> getCapturedPieces() {
        return capturedPieces;
    }

    /**
     * @brief Définit la liste des pions capturés par ce coup.
     *
     * Cette méthode permet d'enregistrer, après calcul des captures,
     * l'ensemble des positions des pions adverses retournés.
     *
     * @param capturedPieces Liste des positions [x, y] des pions capturés.
     */
    public void setCapturedPieces(List<int[]> capturedPieces) {
        this.capturedPieces = capturedPieces;
    }
}