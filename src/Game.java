import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

/**
 * Main game class that manages the game loop and user input
 */
public class Game extends JPanel implements ActionListener, KeyListener {
    private final int columns = 19;
    private final int rows = 21;
    private final int tileSize = 32;

    private boolean levelSelected = false;
    private LevelType selectedLevel = null;

    private BoardLoader boardLoader;
    private final WindowManager windowManager;
    private GameStateManager gameStateManager;

    private HashSet<Block> walls;
    private HashSet<Block> foods;
    private HashSet<Ghost> ghosts;
    private PacMan pacman;

    private final Timer gameLoop;

    public Game() {
        // Initialize managers
        windowManager = new WindowManager(columns, rows, tileSize, this);

        // Set up panel
        setPreferredSize(windowManager.getPanelDimension());
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        // Start game loop
        gameLoop = new Timer(50, this); // 20 FPS
    }

    private void loadBoard() {
        // Load the current level
        boardLoader.loadBoard(gameStateManager.getCurrentLevel());

        // Get the entities
        walls = boardLoader.getWalls();
        foods = boardLoader.getFoods();
        ghosts = boardLoader.getGhosts();
        pacman = boardLoader.getPacman();

    }

    @Override
    public void paintComponent(Graphics g) {
        if (levelSelected) {
            super.paintComponent(g);
            windowManager.render(g, pacman, ghosts, walls, foods, gameStateManager);
        }
        else {
            windowManager.drawSelectionPanel(g);
        }
    }

    private void updateGame() {
        if (gameStateManager.isGameOver()) {
            return;
        }

        pacman.move(walls);

        int previousLives = gameStateManager.getLives();  // Guarda as vidas antes de checar colisão

        gameStateManager.checkCollisions(pacman, ghosts);

        if (gameStateManager.isGameOver()) {
            return;
        }

        // Se perdeu uma vida e ainda não é game over
        if (gameStateManager.getLives() < previousLives && !gameStateManager.isGameOver()) {
            boardLoader.resetPositions();
        }

        // Move ghosts
        for (Ghost ghost : ghosts) {
            ghost.move(walls, columns * tileSize, pacman, tileSize);
        }

        // Eat food and update score
        gameStateManager.eatFood(pacman, foods);

        if (gameStateManager.isLevelComplete(foods)) {
            JOptionPane.showMessageDialog(this, "PARABÉNS " + "seus pontos: " + gameStateManager.getScore());
            loadBoard();
            boardLoader.resetPositions();
        }
    }

    private void resetGame() {
        loadBoard();
        boardLoader.resetPositions();
        gameStateManager.reset();
        gameLoop.start();
    }

    // Game loop update
    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();

        if (gameStateManager.isGameOver()) {
            gameLoop.stop();
        }
    }

    // KeyListener methods
    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!levelSelected) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_1 -> selectedLevel = LevelType.LEVEL_1;
                case KeyEvent.VK_2 -> selectedLevel = LevelType.LEVEL_2;
                case KeyEvent.VK_3 -> selectedLevel = LevelType.LEVEL_3;
            }

            if (selectedLevel != null) {
                levelSelected = true;

                gameStateManager = new GameStateManager(selectedLevel);
                boardLoader = new BoardLoader(tileSize, selectedLevel.getWallAsset());

                loadBoard();

                gameLoop.start();
            }
        }

        //tecla mestra - cheat
        if (e.getKeyCode() == KeyEvent.VK_7) {
            foods.clear();
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_R && gameStateManager.isGameOver()) {
            resetGame();
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.updateDirection('U');
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.updateDirection('D');
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.updateDirection('R');
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.updateDirection('L');
        }

        pacman.updateImage();
    }
}

