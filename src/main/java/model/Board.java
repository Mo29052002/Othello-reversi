package model;

import java.util.ArrayList;
import java.util.List;

/**
 * @class Board
 * @brief Représente le plateau de jeu d'Othello/Reversi.
 *
 * Cette classe gère la grille de cellules ainsi que l'état global du plateau
 * (taille, disposition initiale des pions et opérations de base sur les cases).
 */
public class Board {

    /**
     * @brief Taille du plateau (nombre de cases par ligne et par colonne).
     *
     * Par exemple, pour un plateau classique d'Othello, la taille est de 8.
     */
    private int size;

    /**
     * @brief Grille bidimensionnelle représentant les cellules du plateau.
     *
     * Chaque entrée du tableau correspond à une cellule {@link Cell} située
     * aux coordonnées (x, y) sur le plateau.
     */
    private Cell[][] grid;

    /**
     * @brief Construit un plateau carré de la taille spécifiée.
     *
     * Initialise la grille de cellules et place la configuration initiale
     * des quatre pions au centre du plateau.
     *
     * @param size Taille du plateau (nombre de cases par côté).
     *             Doit être un entier positif supérieur ou égal à 4
     *             pour un plateau jouable.
     */
    public Board(int size) {
        this.size = size;
        grid = new Cell[size][size];
        initializeBoard();
    }

    /**
     * @brief Construit un plateau avec la taille par défaut de 8.
     *
     * Ce constructeur est fourni pour conserver la compatibilité avec
     * l'utilisation d'un plateau standard d'Othello/Reversi (8x8).
     */
    public Board() {
        this(8);
    }

    /**
     * @brief Initialise la grille de cellules du plateau.
     *
     * Crée chaque cellule avec ses coordonnées (x, y) et positionne
     * ensuite les quatre pions de départ au centre du plateau :
     * - deux pions blancs en diagonale
     * - deux pions noirs en diagonale
     * conformément aux règles classiques d'Othello.
     */
    private void initializeBoard() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                grid[x][y] = new Cell(x, y);
            }
        }
       
        int center = size / 2;
        grid[center - 1][center - 1].setState(Cell.CellState.WHITE);
        grid[center - 1][center].setState(Cell.CellState.BLACK);
        grid[center][center - 1].setState(Cell.CellState.BLACK);
        grid[center][center].setState(Cell.CellState.WHITE);
    }

    /**
     * @brief Retourne la cellule située aux coordonnées données.
     *
     * Si les coordonnées sont en dehors des limites du plateau, la méthode
     * retourne null pour signaler une position invalide.
     *
     * @param x Coordonnée horizontale de la cellule (colonne), de 0 à size - 1.
     * @param y Coordonnée verticale de la cellule (ligne), de 0 à size - 1.
     * @return La cellule {@link Cell} correspondante si la position est valide,
     *         ou null si les coordonnées sont en dehors du plateau.
     */
    public Cell getCell(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size) {
            return null;
        }
        return grid[x][y];
    }

    /**
     * @brief Vérifie si une paire de coordonnées appartient au plateau.
     *
     * Cette méthode permet de s'assurer qu'une position (x, y) désigne bien
     * une case existante dans la grille.
     *
     * @param x Coordonnée horizontale à tester.
     * @param y Coordonnée verticale à tester.
     * @return true si la position est comprise dans les limites du plateau,
     *         false sinon.
     */
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    /**
     * @brief Place un pion sur la case indiquée si la position est valide.
     *
     * La méthode modifie l'état de la cellule aux coordonnées (x, y) en lui
     * attribuant l'état spécifié (par exemple NOIR ou BLANC). Si la position
     * est invalide, aucun changement n'est effectué.
     *
     * @param x Coordonnée horizontale de la case où placer le pion.
     * @param y Coordonnée verticale de la case où placer le pion.
     * @param state Nouvel état de la cellule, représentant la couleur du pion
     *              (par exemple {@link Cell.CellState#BLACK} ou
     *              {@link Cell.CellState#WHITE}).
     */
    public void placePiece(int x, int y, Cell.CellState state) {
        if (isValidPosition(x, y)) {
            grid[x][y].setState(state);
        }
    }

    /**
     * @brief Retourne la grille complète de cellules du plateau.
     *
     * Cette méthode donne accès au tableau bidimensionnel interne
     * représentant l'état courant de chaque case du plateau.
     *
     * @return Le tableau 2D de cellules représentant la grille.
     */
    public Cell[][] getGrid() {
        return grid;
    }

    /**
     * @brief Compte le nombre de pions d'un certain état sur le plateau.
     *
     * Parcourt l'ensemble de la grille et incrémente un compteur pour
     * chaque cellule dont l'état correspond à celui passé en paramètre.
     *
     * @param state État des cellules à compter (par exemple NOIR ou BLANC).
     * @return Le nombre total de cellules ayant l'état spécifié sur le plateau.
     */
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

    /**
     * @brief Indique si le plateau est entièrement rempli.
     *
     * Le plateau est considéré comme plein lorsqu'aucune cellule n'est vide,
     * c'est-à-dire qu'il n'existe plus de case disponible pour poser un pion.
     *
     * @return true si toutes les cellules contiennent un pion, false dès
     *         qu'au moins une cellule est encore vide.
     */
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

    /**
     * @brief Retourne la taille du plateau.
     *
     * La taille correspond au nombre de cases par ligne (et par colonne)
     * du plateau carré.
     *
     * @return La taille du plateau (dimension d'un côté).
     */
    public int getSize() {
        return size;
    }
}