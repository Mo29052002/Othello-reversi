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
import java.util.List;
import model.Board;
import model.Cell;
import model.GameState;
import model.Move;
import model.Player;
import model.Rules;

public class GameController {
    private static final String SAVE_FILE_NAME = "othello-save.txt";

    private final OthelloApp app;
    private final File saveFile;

    private GameState gameState;
    private JFrame gameFrame;
    private JPanel boardPanel;
    private JLabel statusLabel;
    private JButton pauseButton;
    private int mode;
    private JTextArea HistoryArea;

    public GameController(OthelloApp app) {
        this.app = app;
        this.saveFile = new File(SAVE_FILE_NAME);
    }

    public void startGame(int mode) {
        this.mode = mode;
        if (mode == 3) {
            if (!loadGame()) {
                showAlert("Aucune partie chargée", "Aucune sauvegarde trouvée ou fichier invalide.");
                return;
            }
        } else {
            int boardSize = chooseBoardSize();
            if (boardSize == -1) {
                // User cancelled
                app.showMenuFromGame();
                return;
            }
            resetGame(boardSize);
        }
        buildGameFrame();
        app.setVisible(false);
        gameFrame.setVisible(true);
    }

    private void resetGame(int boardSize) {
        this.gameState = new GameState(boardSize);
        this.gameState.updateScores();
    }


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
            return -1; // Cancelled
        }
        return Integer.parseInt(choice);
    }

    private void buildGameFrame() {
        gameFrame = new JFrame("Othello / Reversi - Partie");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setSize(900, 800);
        gameFrame.setLocationRelativeTo(null);
        HistoryArea = new JTextArea(5, 20);
        HistoryArea.setEditable(false);
        HistoryArea.setFont(new Font("Monospaced",Font.PLAIN,12));
        JScrollPane scrollPane = new JScrollPane(HistoryArea);

        boardPanel = createBoardPanel();
        statusLabel = new JLabel(getStatusText(), SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> showPauseDialog());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(statusLabel, BorderLayout.CENTER);
        topPanel.add(pauseButton, BorderLayout.EAST);

        gameFrame.setLayout(new BorderLayout());
        gameFrame.add(topPanel, BorderLayout.NORTH);
        gameFrame.add(boardPanel, BorderLayout.CENTER);
        gameFrame.add(scrollPane, BorderLayout.EAST);
        refreshBoard();
    }

    private JPanel createBoardPanel() {
        int boardSize = gameState.getBoard().getSize();
        JPanel panel = new JPanel(new GridLayout(boardSize, boardSize, 2, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(34, 139, 34)); // Dark green

        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                JPanel cellPanel = createCellPanel(x, y);
                panel.add(cellPanel);
            }
        }

        return panel;
    }

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
        @Override
        public void mouseClicked(MouseEvent e) {
            handleCellClick(x, y);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            Player current = gameState.getCurrentPlayer();
            if (gameState.getBoard().getCell(x, y).isEmpty()
                    && Rules.isValidMove(gameState.getBoard(), x, y, current)) {
                panel.setBackground(Color.LIGHT_GRAY);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            panel.setBackground(baseColor);
        }
    });

    return panel;
}

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
        refreshBoard();
    }

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

    private void refreshBoard() {
        boardPanel.repaint();
        if (statusLabel != null) {
            statusLabel.setText(getStatusText());
        }
    }

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

    private String getStatusText() {
        if (gameState.isGameOver()) {
            return "Partie terminée";
        }
        String current = gameState.getCurrentPlayer().isBlack() ? "Noir" : "Blanc";
        return "Tour de " + current + " | Noir: " + gameState.getPlayer1().getScore() + " - Blanc: "
                + gameState.getPlayer2().getScore();
    }

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

    private void showAlert(String title, String message) {
        JOptionPane.showMessageDialog(gameFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
