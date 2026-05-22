package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @class OthelloApp
 * @brief Représente la fenêtre principale de l'application Othello / Reversi.
 *
 * Cette classe hérite de {@link JFrame} et constitue le point d'entrée
 * graphique de l'application. Elle gère l'affichage du menu principal,
 * l'initialisation de la fenêtre ainsi que le lancement d'une nouvelle partie
 * via le contrôleur du jeu.
 */
public class OthelloApp extends JFrame {

    /**
     * @brief Contrôleur principal de l'application.
     *
     * Cet objet assure la liaison entre l'interface graphique et la logique
     * métier du jeu. Il est initialisé lors du démarrage d'une partie.
     */
    private GameController controller;

    /**
     * @brief Construit la fenêtre principale de l'application.
     *
     * Ce constructeur initialise les propriétés générales de la fenêtre :
     * titre, comportement à la fermeture, dimensions et position à l'écran.
     * Il affiche ensuite le menu principal de l'application.
     */
    public OthelloApp() {
        setTitle("Othello / Reversi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        showMenu();
    }

    /**
     * @brief Affiche le menu principal de l'application.
     *
     * Cette méthode vide le contenu actuel de la fenêtre, crée un panneau
     * de menu vertical, ajoute le titre et les boutons d'action disponibles
     * (joueur contre joueur, chargement de partie, quitter), puis rafraîchit
     * l'affichage de la fenêtre.
     */
    public void showMenu() {
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

    /**
     * @brief Lance une nouvelle partie selon le mode choisi.
     *
     * Cette méthode crée une nouvelle instance du contrôleur du jeu,
     * associée à cette fenêtre, puis demande au contrôleur de démarrer
     * la partie selon le mode sélectionné.
     *
     * @param mode Mode de jeu à lancer.
     *             Par exemple :
     *             - 1 pour joueur contre joueur ;
     *             - 3 pour charger une partie.
     */
    private void startGame(int mode) {
        this.controller = new GameController(this);
        controller.startGame(mode);
    }

    /**
     * @brief Réaffiche le menu principal depuis l'écran de jeu.
     *
     * Cette méthode peut être appelée lorsqu'une partie se termine ou
     * lorsqu'un retour au menu principal est demandé depuis l'interface du jeu.
     */
    public void showMenuFromGame() {
        showMenu();
    }

    /**
     * @brief Point d'entrée principal de l'application.
     *
     * Cette méthode démarre l'interface graphique sur le thread événementiel
     * de Swing à l'aide de {@link SwingUtilities#invokeLater(Runnable)},
     * puis crée et affiche la fenêtre principale de l'application.
     *
     * @param args Arguments de la ligne de commande transmis au programme.
     *             Ils ne sont pas utilisés dans cette application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new OthelloApp().setVisible(true);
        });
    }
}