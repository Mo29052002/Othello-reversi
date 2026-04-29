package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OthelloApp extends JFrame {
    private GameController controller;

    public OthelloApp() {
        setTitle("Othello / Reversi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        showMenu();
    }

    private void showMenu() {
        getContentPane().removeAll();

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel title = new JLabel("Othello / Reversi", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton pvpButton = new JButton("Mode 1 : 1 vs 1 (joueur contre joueur)");
        JButton aiButton = new JButton("Mode 2 : 1 vs ordi");
        JButton loadButton = new JButton("Mode 3 : Charger une partie");
        JButton exitButton = new JButton("Quitter");

        pvpButton.addActionListener(e -> startGame(1));
        aiButton.addActionListener(e -> startGame(2));
        loadButton.addActionListener(e -> startGame(3));
        exitButton.addActionListener(e -> System.exit(0));

        menuPanel.add(title);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        menuPanel.add(pvpButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(aiButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(loadButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(exitButton);

        add(menuPanel);
        revalidate();
        repaint();
    }

    private void startGame(int mode) {
        this.controller = new GameController(this);
        controller.startGame(mode);
    }

    public void showMenuFromGame() {
        showMenu();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new OthelloApp().setVisible(true);
        });
    }
}
