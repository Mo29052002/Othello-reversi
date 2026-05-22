package model;

import java.util.ArrayList;
import java.util.List;

/**
 * @class Rules
 * @brief Contient les règles et la logique de validation des coups pour Othello/Reversi.
 *
 * Cette classe fournit des méthodes statiques permettant de vérifier la validité
 * des coups, de calculer les pions capturés, de lister les coups possibles et
 * d'appliquer un coup sur le plateau de jeu.
 */
public class Rules {

    /**
     * @brief Directions de recherche pour détecter les captures.
     *
     * Chaque entrée du tableau représente un vecteur de déplacement (dx, dy)
     * permettant de parcourir le plateau dans les huit directions :
     * haut, bas, gauche, droite et les quatre diagonales.
     */

    private static final int[][] DIRECTIONS = {
        {-1, 0}, {1, 0}, {0, -1}, {0, 1},
        {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
    };

    /**
     * @brief Vérifie si un coup est valide pour un joueur donné.
     *
     * Un coup est considéré comme valide si :
     * - la position est à l'intérieur des limites du plateau ;
     * - la case ciblée est vide ;
     * - le placement du pion à cette position permet de capturer
     *   au moins un pion adverse dans une des directions.
     *
     * @param board Plateau de jeu sur lequel le coup est envisagé.
     * @param x Coordonnée horizontale de la case jouée.
     * @param y Coordonnée verticale de la case jouée.
     * @param player Joueur qui souhaite jouer ce coup.
     * @return true si le coup est valide pour ce joueur, false sinon.
     */
    public static boolean isValidMove(Board board, int x, int y, Player player) {
        if (!board.isValidPosition(x, y) || !board.getCell(x, y).isEmpty()) {
            return false;
        }
        
        return getCapturedPieces(board, x, y, player).size() > 0;
    }

    /**
     * @brief Calcule la liste des pions capturés si un coup est joué à une position donnée.
     *
     * Pour chaque direction possible, la méthode vérifie si une série de pions
     * adverses est encadrée par un pion du joueur, ce qui correspond à une capture
     * selon les règles d'Othello.
     *
     * @param board Plateau sur lequel le coup est envisagé.
     * @param x Coordonnée horizontale de la case jouée.
     * @param y Coordonnée verticale de la case jouée.
     * @param player Joueur qui joue le coup.
     * @return Une liste de positions [x, y] des pions adverses qui seraient capturés.
     */
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

    /**
     * @brief Vérifie les captures possibles dans une direction donnée.
     *
     * En partant de la case immédiatement voisine dans la direction (dx, dy),
     * la méthode collecte les positions des pions adverses successifs.
     * La capture est validée seulement si cette séquence est suivie d'un pion
     * de la couleur du joueur (et non d'une case vide ou du bord du plateau).
     *
     * @param board Plateau de jeu sur lequel la vérification est effectuée.
     * @param x Coordonnée horizontale de la case jouée.
     * @param y Coordonnée verticale de la case jouée.
     * @param dx Déplacement horizontal pour parcourir la direction.
     * @param dy Déplacement vertical pour parcourir la direction.
     * @param opponent Couleur de l'adversaire à capturer.
     * @return Une liste des positions [x, y] capturées dans cette direction
     *         si la capture est possible, ou null si aucune capture n'est valide.
     */
    private static List<int[]> checkDirection(Board board, int x, int y, int dx, int dy, Cell.CellState opponent) {
        List<int[]> captured = new ArrayList<>();
        int nx = x + dx;
        int ny = y + dy;

        while (board.isValidPosition(nx, ny)) {
            Cell cell = board.getCell(nx, ny);
            if (cell.getState() == opponent) {
                captured.add(new int[]{nx, ny});
            } else if (cell.getState() == Cell.CellState.EMPTY) {
                
                return null;
            } else {
               
                return captured;
            }
            nx += dx;
            ny += dy;
        }
    
        return null;
    }

    /**
     * @brief Retourne la couleur de l'adversaire d'une couleur donnée.
     *
     * @param color Couleur du joueur courant (BLACK ou WHITE).
     * @return La couleur opposée (WHITE si color est BLACK, et inversement).
     */
    public static Cell.CellState getOpponentColor(Cell.CellState color) {
        return (color == Cell.CellState.BLACK) ? Cell.CellState.WHITE : Cell.CellState.BLACK;
    }

    /**
     * @brief Calcule la liste de tous les coups valides pour un joueur donné.
     *
     * Parcourt l'ensemble des cases du plateau et ajoute à la liste chaque
     * position où un coup valide peut être joué par le joueur.
     *
     * @param board Plateau sur lequel les coups sont recherchés.
     * @param player Joueur pour lequel on cherche les coups valides.
     * @return Une liste d'objets {@link Move} représentant tous les coups possibles.
     */
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

    /**
     * @brief Indique si un joueur possède au moins un coup valide.
     *
     * @param board Plateau sur lequel les coups sont examinés.
     * @param player Joueur pour lequel on vérifie la présence de coups valides.
     * @return true si au moins un coup valide existe, false sinon.
     */
    public static boolean hasValidMoves(Board board, Player player) {
        return !getValidMoves(board, player).isEmpty();
    }

    /**
     * @brief Applique un coup sur le plateau et retourne les pions capturés.
     *
     * La méthode :
     * - place le pion du joueur à la position indiquée ;
     * - calcule toutes les pièces capturées par ce coup ;
     * - remplace ces pions par la couleur du joueur ;
     * - enregistre la liste des positions capturées dans l'objet {@link Move}.
     *
     * @param board Plateau sur lequel le coup doit être appliqué.
     * @param move Coup à appliquer, contenant la position et le joueur.
     */
    public static void applyMove(Board board, Move move) {
        board.placePiece(move.getX(), move.getY(), move.getPlayer().getColor());
        List<int[]> captured = getCapturedPieces(board, move.getX(), move.getY(), move.getPlayer());
        for (int[] pos : captured) {
            board.placePiece(pos[0], pos[1], move.getPlayer().getColor());
        }
        move.setCapturedPieces(captured);
    }
}