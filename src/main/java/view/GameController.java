package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Board;
import model.Cell;
import model.GameSnapshot;
import model.GameState;
import model.Move;
import model.Player;
import model.Rules;

/**
 * @class GameController
 * @brief Contrôleur principal d'une partie d'Othello/Reversi.
 *
 * Cette classe assure la liaison entre l'interface graphique et la logique
 * du jeu. Elle gère l'initialisation d'une partie, les interactions utilisateur,
 * la mise à jour de l'affichage, l'historique des coups, ainsi que les
 * fonctionnalités de sauvegarde, chargement, annulation et rétablissement.
 */
public class GameController {

    /**
     * @brief Nom du fichier utilisé pour la sauvegarde de la partie.
     *
     * Ce fichier texte contient les informations minimales nécessaires
     * pour recharger l'état d'une partie.
     */
    private static final String SAVE_FILE_NAME = "othello-save.txt";

    /**
     * @brief Référence vers l'application principale.
     *
     * Cet objet permet au contrôleur de revenir au menu principal ou
     * d'interagir avec la fenêtre principale de lancement.
     */
    private final OthelloApp app;

    /**
     * @brief Fichier physique de sauvegarde associé à la partie.
     *
     * Il est construit à partir de la constante {@link #SAVE_FILE_NAME}.
     */
    private final File saveFile;

    /**
     * @brief État courant de la partie.
     *
     * Contient le plateau, les joueurs, le joueur courant, les scores,
     * l'historique des coups et l'état de fin de partie.
     */
    private GameState gameState;

    /**
     * @brief Fenêtre principale de la partie en cours.
     *
     * Cette fenêtre contient le plateau, les boutons d'action, le statut
     * de la partie et l'historique des coups.
     */
    private JFrame gameFrame;

    /**
     * @brief Panneau graphique représentant le plateau de jeu.
     */
    private JPanel boardPanel;

    /**
     * @brief Libellé affichant l'état courant de la partie.
     *
     * Il indique par exemple le joueur dont c'est le tour ou le score actuel.
     */
    private JLabel statusLabel;

    /**
     * @brief Bouton permettant d'ouvrir le menu de pause.
     */
    private JButton pauseButton;

    /**
     * @brief Bouton permettant d'annuler le dernier coup joué.
     */
    private JButton undoButton;

    /**
     * @brief Bouton permettant de refaire un coup précédemment annulé.
     */
    private JButton redoButton;

    /**
     * @brief Bouton permettant de jouer en saisissant les coordonnées au clavier.
     */
    private JButton playwithkeyboardButton;

    /**
     * @brief Mode de jeu courant.
     *
     * Exemples :
     * - 1 : joueur contre joueur ;
     * - 2 : joueur contre ordinateur ;
     * - 3 : chargement d'une partie sauvegardée.
     */
    private int mode;

    /**
     * @brief Zone de texte affichant l'historique des coups joués.
     */
    private JTextArea HistoryArea;

    /**
     * @brief Liste des instantanés de partie utilisée pour l'annulation.
     *
     * Chaque instantané représente un état successif du jeu.
     */
    private List<GameSnapshot> snapshots = new ArrayList<>();

    /**
     * @brief Liste des instantanés annulés utilisée pour le rétablissement.
     *
     * Cette liste permet de refaire les coups après une annulation.
     */
    private List<GameSnapshot> redoSnapshots = new ArrayList<>();

    /**
     * @brief Construit un contrôleur de partie associé à l'application principale.
     *
     * Initialise la référence vers l'application et prépare le fichier
     * de sauvegarde utilisé pour enregistrer ou charger une partie.
     *
     * @param app Application principale contenant le menu et la fenêtre d'accueil.
     */
    public GameController(OthelloApp app) {
        this.app = app;
        this.saveFile = new File(SAVE_FILE_NAME);
    }

    /**
     * @brief Démarre une nouvelle partie selon le mode choisi.
     *
     * Si le mode correspond au chargement d'une partie, la méthode tente
     * de restaurer une sauvegarde existante. Sinon, elle demande la taille
     * du plateau puis initialise une nouvelle partie. Ensuite, elle construit
     * la fenêtre de jeu et l'affiche.
     *
     * @param mode Mode de jeu à lancer.
     */
    public void startGame(int mode) {
        this.mode = mode;
        if (mode == 3) {
            if (!loadGame()) {
                showAlert("Aucune partie chargée", "Aucune sauvegarde trouvée ou fichier invalide.");
                return;
            }
            snapshots.clear();
            saveSnapshot(null);
        } else {
            int boardSize = chooseBoardSize();
            if (boardSize == -1) {
            
                app.showMenuFromGame();
                return;
            }
            resetGame(boardSize);
        }
        buildGameFrame();
        app.setVisible(false);
        gameFrame.setVisible(true);
    }

    /**
     * @brief Réinitialise complètement la partie avec une nouvelle taille de plateau.
     *
     * Cette méthode recrée un nouvel état de jeu, met à jour les scores,
     * vide l'historique des instantanés et enregistre un premier état initial.
     *
     * @param boardSize Taille du plateau à créer.
     */
    private void resetGame(int boardSize) {
        this.gameState = new GameState(boardSize);
        this.gameState.updateScores();
        snapshots.clear();
        saveSnapshot(null);
    }

    /**
     * @brief Enregistre un instantané complet de l'état courant de la partie.
     *
     * Cette méthode copie l'état du plateau, le joueur courant, les scores,
     * le numéro du coup et éventuellement le coup ayant conduit à cet état.
     * L'instantané est ajouté à l'historique pour permettre l'annulation.
     *
     * @param move Coup ayant conduit à l'état courant, ou null pour un état initial.
     */
    private void saveSnapshot(Move move) {
        int size = gameState.getBoard().getSize();
        Cell.CellState[][] boardState = new Cell.CellState[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                boardState[x][y] = gameState.getBoard().getCell(x, y).getState();
            }
        }
        GameSnapshot snapshot = new GameSnapshot(
                move,
                boardState,
                size,
                gameState.getCurrentPlayer().getColor(),
                gameState.getPlayer1().getScore(),
                gameState.getPlayer2().getScore(),
                gameState.getMoveHistory().size()
        );
        snapshots.add(snapshot);
    }

    /**
     * @brief Demande à l'utilisateur de choisir la taille du plateau.
     *
     * Une boîte de dialogue propose plusieurs tailles prédéfinies.
     * Si l'utilisateur annule, la méthode retourne -1.
     *
     * @return La taille choisie du plateau, ou -1 si l'utilisateur annule.
     */
    private int chooseBoardSize() {
        String[] options = {"4", "6", "8", "10", "12"};
        String choice = (String) JOptionPane.showInputDialog(
            app,
            "Choisissez la taille du plateau :",
            "Taille du plateau",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            "8"
        );
        if (choice == null) {
            return -1;
        }
        return Integer.parseInt(choice);
    }

    /**
     * @brief Construit l'interface graphique complète de la partie.
     *
     * Cette méthode crée la fenêtre de jeu, le plateau, la barre d'état,
     * les boutons d'action et la zone d'historique, puis positionne tous
     * les composants dans la fenêtre.
     */
    private void buildGameFrame() {
        gameFrame = new JFrame("Othello / Reversi - Partie");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setSize(900, 800);
        gameFrame.setLocationRelativeTo(null);

        HistoryArea = new JTextArea(5, 20);
        HistoryArea.setEditable(false);
        HistoryArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(HistoryArea);

        boardPanel = createBoardPanel();
        statusLabel = new JLabel(getStatusText(), SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> showPauseDialog());

        undoButton = new JButton("REVENIR EN ARRIÈRE");
        undoButton.addActionListener(e -> undoMove());

        redoButton = new JButton("AVANCER");
        redoButton.addActionListener(e -> redoMove());

        playwithkeyboardButton = new JButton("Jouer avec le clavier");
        playwithkeyboardButton.addActionListener(e -> playWithKeyboard());

        JPanel leftPanel = new JPanel(new FlowLayout());
        leftPanel.add(undoButton);
        leftPanel.add(redoButton);
        leftPanel.add(playwithkeyboardButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(statusLabel, BorderLayout.CENTER);
        topPanel.add(pauseButton, BorderLayout.EAST);
        topPanel.add(leftPanel, BorderLayout.WEST);

        gameFrame.setLayout(new BorderLayout());
        gameFrame.add(topPanel, BorderLayout.NORTH);
        gameFrame.add(boardPanel, BorderLayout.CENTER);
        gameFrame.add(scrollPane, BorderLayout.EAST);
        refreshBoard();
    }

    /**
     * @brief Annule le dernier coup joué.
     *
     * La méthode restaure l'instantané précédent, place l'instantané annulé
     * dans la pile de rétablissement, met à jour les scores, l'historique
     * des coups et rafraîchit l'affichage.
     */
    private void undoMove() {
        if (snapshots.size() <= 1) {
            showAlert("Impossible", "Aucun coup à annuler.");
            return;
        }

        GameSnapshot undoneSnapshot = snapshots.remove(snapshots.size() - 1);
        redoSnapshots.add(undoneSnapshot);
        GameSnapshot previousSnapshot = snapshots.get(snapshots.size() - 1);
        previousSnapshot.restoreToBoard(gameState.getBoard());
        gameState.setCurrentPlayer(previousSnapshot.getCurrentPlayer());
        gameState.updateScores();
        gameState.setGameOver(false);
        gameState.setWinner(null);

        if (!gameState.getMoveHistory().isEmpty()) {
            gameState.getMoveHistory().remove(gameState.getMoveHistory().size() - 1);
        }
        updateMoveHistory();
        refreshBoard();
    }

    /**
     * @brief Rejoue le dernier coup annulé.
     *
     * La méthode récupère le dernier instantané annulé, le restaure sur
     * le plateau, réactualise les informations de partie puis le replace
     * dans la liste des instantanés actifs.
     */
    private void redoMove() {
        if (redoSnapshots.isEmpty()) {
            showAlert("Impossible", "Aucun coup à refaire.");
            return;
        }
        GameSnapshot toRedo = redoSnapshots.remove(redoSnapshots.size() - 1);
        toRedo.restoreToBoard(gameState.getBoard());
        gameState.setCurrentPlayer(toRedo.getCurrentPlayer());
        gameState.updateScores();
        gameState.setGameOver(false);
        gameState.setWinner(null);
        snapshots.add(toRedo);
        if (toRedo.getMove() != null) {
            gameState.getMoveHistory().add(toRedo.getMove());
        }
        updateMoveHistory();
        refreshBoard();
    }

    /**
     * @brief Crée le panneau graphique représentant l'ensemble du plateau.
     *
     * Le panneau est constitué d'une grille de sous-panneaux, un par case,
     * chacun étant chargé de dessiner et gérer les interactions de sa cellule.
     *
     * @return Le panneau contenant toutes les cases du plateau.
     */
    private JPanel createBoardPanel() {
        int boardSize = gameState.getBoard().getSize();
        JPanel panel = new JPanel(new GridLayout(boardSize, boardSize, 2, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(34, 139, 34));

        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                JPanel cellPanel = createCellPanel(x, y);
                panel.add(cellPanel);
            }
        }

        return panel;
    }

    /**
     * @brief Crée le panneau graphique correspondant à une case du plateau.
     *
     * Ce panneau dessine visuellement la case et le pion éventuel présent.
     * Il gère également les interactions souris : survol, clic gauche pour
     * jouer et clic droit pour annuler ou jouer selon l'action définie.
     *
     * @param x Coordonnée horizontale de la case.
     * @param y Coordonnée verticale de la case.
     * @return Le panneau graphique représentant la case demandée.
     */
    private JPanel createCellPanel(int x, int y) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Cell cell = gameState.getBoard().getCell(x, y);
                if (!cell.isEmpty()) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(cell.isBlack() ? Color.BLACK : Color.WHITE);

                    int diameter = Math.min(getWidth(), getHeight()) - 10;
                    int centerX = (getWidth() - diameter) / 2;
                    int centerY = (getHeight() - diameter) / 2;

                    g2d.fillOval(centerX, centerY, diameter, diameter);
                    g2d.setColor(Color.DARK_GRAY);
                    g2d.drawOval(centerX, centerY, diameter, diameter);
                }
            }
        };

        Color baseColor = new Color(100, 0, 0);
        panel.setBackground(baseColor);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setPreferredSize(new Dimension(60, 60));

        panel.addMouseListener(new MouseAdapter() {

            /**
             * @brief Met en surbrillance une case valide lors du survol de la souris.
             *
             * @param e Événement de survol de souris.
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                Player current = gameState.getCurrentPlayer();
                if (gameState.getBoard().getCell(x, y).isEmpty()
                        && Rules.isValidMove(gameState.getBoard(), x, y, current)) {
                    panel.setBackground(Color.LIGHT_GRAY);
                }
            }

            /**
             * @brief Restaure la couleur normale de la case lorsque la souris la quitte.
             *
             * @param e Événement de sortie de souris.
             */
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(baseColor);
            }

            /**
             * @brief Gère l'appui sur un bouton de la souris sur une case.
             *
             * - Clic droit : joue le coup si la case est valide.
             * - Clic gauche : délègue le traitement au gestionnaire classique de clic.
             *
             * @param e Événement d'appui souris.
             */
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    Player current = gameState.getCurrentPlayer();
                    if (gameState.getBoard().getCell(x, y).isEmpty()
                            && Rules.isValidMove(gameState.getBoard(), x, y, current)) {
                        applyPlayerMove(x, y);
                    }
                } else {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        handleCellClick(x, y);
                    }
                }
            }

            /**
             * @brief Gère le relâchement du bouton droit de la souris.
             *
             * Dans cette implémentation, le relâchement du clic droit déclenche
             * une annulation du dernier coup.
             *
             * @param e Événement de relâchement souris.
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    undoMove();
                }
            }
        });

        return panel;
    }

    /**
     * @brief Traite le clic utilisateur sur une case du plateau.
     *
     * La méthode vérifie si la partie est terminée, puis si le coup est valide.
     * Si le coup est autorisé, il est appliqué. En mode contre ordinateur,
     * le tour de l'IA est ensuite exécuté.
     *
     * @param x Coordonnée horizontale de la case cliquée.
     * @param y Coordonnée verticale de la case cliquée.
     */
    private void handleCellClick(int x, int y) {
        if (gameState.isGameOver()) {
            showAlert("Partie terminée", "La partie est terminée. Recommence pour jouer à nouveau.");
            return;
        }

        Player current = gameState.getCurrentPlayer();
        if (!Rules.isValidMove(gameState.getBoard(), x, y, current)) {
            showAlert("Coup invalide", "Ce coup n'est pas autorisé.");
            return;
        }

        applyPlayerMove(x, y);
        if (mode == 2 && !gameState.isGameOver()) {
            performAiTurn();
        }
    }

    /**
     * @brief Applique le coup du joueur courant sur le plateau.
     *
     * La méthode crée l'objet représentant le coup, applique les règles,
     * enregistre ce coup dans l'historique, met à jour les scores, vérifie
     * l'état de la partie, enregistre un instantané et rafraîchit l'affichage.
     *
     * @param x Coordonnée horizontale du coup joué.
     * @param y Coordonnée verticale du coup joué.
     */
    private void applyPlayerMove(int x, int y) {
        Player current = gameState.getCurrentPlayer();
        Move move = new Move(x, y, current);
        Rules.applyMove(gameState.getBoard(), move);
        gameState.addMove(move);
        updateMoveHistory();
        gameState.updateScores();
        updateGameStatus();
        if (!gameState.isGameOver()) {
            gameState.switchPlayer();
            if (!Rules.hasValidMoves(gameState.getBoard(), gameState.getCurrentPlayer())) {
                gameState.switchPlayer();
                if (!Rules.hasValidMoves(gameState.getBoard(), gameState.getCurrentPlayer())) {
                    gameState.setGameOver(true);
                }
            }
        }
        saveSnapshot(move);
        redoSnapshots.clear();
        refreshBoard();
    }

    /**
     * @brief Met à jour le texte affiché dans la zone d'historique des coups.
     *
     * Chaque ligne contient le numéro du coup, la couleur du joueur
     * et les coordonnées jouées.
     */
    private void updateMoveHistory() {
        StringBuilder historyText = new StringBuilder();
        List<Move> moves = gameState.getMoveHistory();
        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            String player = move.getPlayer().isBlack() ? "Noir" : "Blanc";
            historyText.append(String.format("%d. %s: (%d, %d)\n", i + 1, player, move.getX(), move.getY()));
        }
        HistoryArea.setText(historyText.toString());
    }

    /**
     * @brief Exécute le tour de l'intelligence artificielle.
     *
     * Dans cette version, l'ordinateur choisit simplement le premier coup
     * valide disponible. Après application du coup, les scores et l'état
     * de la partie sont mis à jour.
     */
    private void performAiTurn() {
        if (gameState.isGameOver()) {
            return;
        }
        Player aiPlayer = gameState.getCurrentPlayer();
        if (aiPlayer.isBlack()) {
            return;
        }
        List<Move> validMoves = Rules.getValidMoves(gameState.getBoard(), aiPlayer);
        if (validMoves.isEmpty()) {
            gameState.switchPlayer();
            refreshBoard();
            return;
        }
        Move aiMove = validMoves.get(0);
        Rules.applyMove(gameState.getBoard(), aiMove);
        gameState.updateScores();
        updateGameStatus();
        if (!gameState.isGameOver()) {
            gameState.switchPlayer();
            if (!Rules.hasValidMoves(gameState.getBoard(), gameState.getCurrentPlayer())) {
                gameState.switchPlayer();
                if (!Rules.hasValidMoves(gameState.getBoard(), gameState.getCurrentPlayer())) {
                    gameState.setGameOver(true);
                }
            }
        }
        refreshBoard();
    }

    /**
     * @brief Met à jour l'état global de la partie et détecte la fin du jeu.
     *
     * Si la partie est terminée ou si le plateau est plein, la méthode
     * détermine le vainqueur, met à jour l'affichage puis montre une boîte
     * d'information contenant le résultat final et les scores.
     */
    private void updateGameStatus() {
        if (gameState.isGameOver() || gameState.getBoard().isFull()) {
            gameState.setGameOver(true);
            int blackScore = gameState.getPlayer1().getScore();
            int whiteScore = gameState.getPlayer2().getScore();
            if (blackScore > whiteScore) {
                gameState.setWinner(gameState.getPlayer1());
            } else if (whiteScore > blackScore) {
                gameState.setWinner(gameState.getPlayer2());
            }
            refreshBoard();
            String winnerText = "Match nul";
            if (gameState.getWinner() != null) {
                winnerText = gameState.getWinner().isBlack() ? "Noir gagne" : "Blanc gagne";
            }
            showAlert("Fin de partie", winnerText + " \nScore Noir: "
                    + blackScore + " - Blanc: " + whiteScore);
        }
        if (statusLabel != null) {
            statusLabel.setText(getStatusText());
        }
    }

    /**
     * @brief Rafraîchit l'affichage du plateau et du texte de statut.
     *
     * Cette méthode force le redessin du plateau et met à jour le texte
     * affiché en haut de la fenêtre.
     */
    private void refreshBoard() {
        boardPanel.repaint();
        if (statusLabel != null) {
            statusLabel.setText(getStatusText());
        }
    }

    /**
     * @brief Affiche la boîte de dialogue de pause.
     *
     * Cette boîte permet de reprendre la partie, recommencer, sauvegarder
     * ou revenir au menu principal.
     */
    private void showPauseDialog() {
        JDialog pauseDialog = new JDialog(gameFrame, "Menu Pause", true);
        pauseDialog.setSize(300, 250);
        pauseDialog.setLocationRelativeTo(gameFrame);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("Menu Pause", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton resumeButton = new JButton("Reprendre");
        JButton restartButton = new JButton("Recommencer");
        JButton saveButton = new JButton("Sauvegarder la partie");
        JButton quitButton = new JButton("Retour au menu");

        resumeButton.addActionListener(e -> pauseDialog.dispose());
        restartButton.addActionListener(e -> {
            gameFrame.dispose();
            resetGame(gameState.getBoard().getSize());
            buildGameFrame();
            gameFrame.setVisible(true);
            pauseDialog.dispose();
        });
        saveButton.addActionListener(e -> {
            if (saveGame()) {
                showAlert("Sauvegarde", "Partie sauvegardée dans " + saveFile.getAbsolutePath());
            } else {
                showAlert("Erreur", "Impossible de sauvegarder la partie.");
            }
        });
        quitButton.addActionListener(e -> {
            pauseDialog.dispose();
            gameFrame.dispose();
            app.showMenu();
            app.setVisible(true);
        });

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(resumeButton);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(restartButton);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(saveButton);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(quitButton);

        pauseDialog.add(panel);
        pauseDialog.setVisible(true);
    }

    /**
     * @brief Ouvre une boîte de dialogue permettant de jouer au clavier.
     *
     * L'utilisateur saisit les coordonnées x et y du coup à jouer.
     * Si le coup est valide, il est appliqué au plateau.
     */
    public void playWithKeyboard() {
        JDialog keyboardDialog = new JDialog(gameFrame, "Jouer avec le clavier", true);
        keyboardDialog.setSize(300, 250);
        keyboardDialog.setLocationRelativeTo(gameFrame);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout());

        JLabel keyboardLabel = new JLabel("entrer les coordonnées pour jouer ", SwingConstants.CENTER);
        keyboardLabel.setFont(new Font("Arial", Font.BOLD, 18));
        keyboardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField inputField1 = new JTextField(10);
        JTextField inputField2 = new JTextField(10);

        JButton submitButton = new JButton("Valider");
        submitButton.addActionListener(e -> {
            int x = Integer.parseInt(inputField1.getText());
            int y = Integer.parseInt(inputField2.getText());
            Player current = gameState.getCurrentPlayer();
            if (!Rules.isValidMove(gameState.getBoard(), x, y, current)) {
                showAlert("Coup invalide", "Ce coup n'est pas autorisé. veuillez entrer des coordonnées valides.");
                return;
            } else {
                applyPlayerMove(x, y);
                gameState.updateScores();
                keyboardDialog.dispose();
            }
        });

        panel2.add(inputField1);
        panel2.add(inputField2);

        panel.add(keyboardLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(panel2);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(submitButton);

        keyboardDialog.add(panel);
        keyboardDialog.setVisible(true);
    }

    /**
     * @brief Construit le texte représentant l'état courant de la partie.
     *
     * Si la partie est terminée, un texte simple l'indique.
     * Sinon, le texte précise le joueur courant ainsi que les scores.
     *
     * @return Une chaîne décrivant l'état courant de la partie.
     */
    private String getStatusText() {
        if (gameState.isGameOver()) {
            return "Partie terminée";
        }
        String current = gameState.getCurrentPlayer().isBlack() ? "Noir" : "Blanc";
        return "Tour de " + current + " | Noir: " + gameState.getPlayer1().getScore() + " - Blanc: "
                + gameState.getPlayer2().getScore();
    }

    /**
     * @brief Sauvegarde l'état courant de la partie dans un fichier texte.
     *
     * Le fichier contient le mode de jeu, la taille du plateau, le joueur
     * courant et l'état complet de chaque case du plateau.
     *
     * @return true si la sauvegarde s'est bien déroulée, false sinon.
     */
    private boolean saveGame() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {
            writer.write(Integer.toString(mode));
            writer.newLine();
            writer.write(Integer.toString(gameState.getBoard().getSize()));
            writer.newLine();
            writer.write(gameState.getCurrentPlayer().getColor().name());
            writer.newLine();
            int boardSize = gameState.getBoard().getSize();
            for (int x = 0; x < boardSize; x++) {
                for (int y = 0; y < boardSize; y++) {
                    writer.write(gameState.getBoard().getCell(x, y).getState().name());
                    writer.write(" ");
                }
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * @brief Charge une partie depuis le fichier de sauvegarde.
     *
     * La méthode lit les informations de base de la partie, recrée un état
     * de jeu cohérent, reconstruit le plateau et restaure le joueur courant.
     *
     * @return true si le chargement a réussi, false sinon.
     */
    private boolean loadGame() {
        if (!saveFile.exists()) {
            return false;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(saveFile))) {
            int fileMode = Integer.parseInt(reader.readLine());
            int boardSize = Integer.parseInt(reader.readLine());
            String currentColor = reader.readLine();
            GameState loadedState = new GameState(boardSize);
            for (int x = 0; x < boardSize; x++) {
                String[] line = reader.readLine().split(" ");
                for (int y = 0; y < boardSize; y++) {
                    Cell.CellState state = Cell.CellState.valueOf(line[y]);
                    loadedState.getBoard().placePiece(x, y, state);
                }
            }
            loadedState.updateScores();
            Player current = currentColor.equals("BLACK") ? loadedState.getPlayer1() : loadedState.getPlayer2();
            while (loadedState.getCurrentPlayer() != current) {
                loadedState.switchPlayer();
            }
            this.mode = fileMode;
            this.gameState = loadedState;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @brief Affiche une boîte de dialogue d'information à l'utilisateur.
     *
     * Cette méthode centralise l'affichage des messages d'information,
     * d'erreur ou de notification dans l'interface.
     *
     * @param title Titre de la boîte de dialogue.
     * @param message Message à afficher à l'utilisateur.
     */
    public void showAlert(String title, String message) {
        JOptionPane.showMessageDialog(gameFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}