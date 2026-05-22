package model;

/**
 * @class Player
 * @brief Représente un joueur dans une partie d'Othello/Reversi.
 *
 * Chaque joueur est associé à une couleur (noir ou blanc) et possède un score
 * correspondant au nombre de pions de sa couleur présents sur le plateau.
 */
public class Player {

    /**
     * @brief Couleur des pions associés à ce joueur.
     *
     * La couleur est représentée par une valeur de {@link Cell.CellState},
     * généralement BLACK pour le joueur noir ou WHITE pour le joueur blanc.
     */
    private final Cell.CellState color;

    /**
     * @brief Score courant du joueur.
     *
     * Le score représente le nombre de pions de la couleur du joueur
     * actuellement présents sur le plateau.
     */
    private int score;

    /**
     * @brief Construit un joueur avec la couleur spécifiée.
     *
     * Le score initial est fixé à 2, correspondant à la configuration
     * de départ standard d'Othello (deux pions par joueur).
     *
     * @param color Couleur des pions de ce joueur (BLACK ou WHITE).
     */
    public Player(Cell.CellState color) {
        this.color = color;
        this.score = 2;
    }

    /**
     * @brief Retourne la couleur de ce joueur.
     *
     * @return La couleur du joueur sous forme de {@link Cell.CellState}.
     */
    public Cell.CellState getColor() {
        return color;
    }

    /**
     * @brief Retourne le score courant du joueur.
     *
     * @return Le score du joueur (nombre de pions de sa couleur).
     */
    public int getScore() {
        return score;
    }

    /**
     * @brief Met à jour le score du joueur avec une nouvelle valeur.
     *
     * Cette méthode permet de synchroniser le score avec l'état du plateau
     * après recomptage des pions.
     *
     * @param score Nouveau score à affecter au joueur.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * @brief Incrémente le score du joueur d'une unité.
     *
     * Utile lorsqu'un nouveau pion de la couleur du joueur est ajouté
     * sur le plateau.
     */
    public void incrementScore() {
        score++;
    }

    /**
     * @brief Décrémente le score du joueur d'une unité.
     *
     * Utile lorsqu'un pion de la couleur du joueur est retiré ou retourné.
     */
    public void decrementScore() {
        score--;
    }

    /**
     * @brief Indique si ce joueur joue avec les pions noirs.
     *
     * @return true si la couleur du joueur est BLACK, false sinon.
     */
    public boolean isBlack() {
        return color == Cell.CellState.BLACK;
    }

    /**
     * @brief Indique si ce joueur joue avec les pions blancs.
     *
     * @return true si la couleur du joueur est WHITE, false sinon.
     */
    public boolean isWhite() {
        return color == Cell.CellState.WHITE;
    }
}