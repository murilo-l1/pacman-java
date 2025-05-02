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

    private final BoardLoader boardLoader;
    private final WindowManager windowManager;
    private final GameStateManager gameStateManager;

    private HashSet<Block> walls;
    private HashSet<Block> foods;
    private HashSet<Ghost> ghosts;
    private PacMan pacman;

    private final Timer gameLoop;

    public Game() {
        // Initialize managers
        boardLoader = new BoardLoader(tileSize);
        windowManager = new WindowManager(columns, rows, tileSize, this);
        gameStateManager = new GameStateManager();

        // Set up panel
        setPreferredSize(windowManager.getPanelDimension());
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        // Load initial board and entities
        loadBoard();

        // Start game loop
        gameLoop = new Timer(50, this); // 20 FPS
        gameLoop.start();
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
        super.paintComponent(g);
        windowManager.render(g, pacman, ghosts, walls, foods, gameStateManager);
    }

    /*private void updateGame() {
        // Skip update if game is over
        if (gameStateManager.isGameOver()) {
            return;
        }

        // Move pacman
        pacman.move(walls);

        // Check for ghost collisions
        gameStateManager.checkCollisions(pacman, ghosts);
        if (gameStateManager.isGameOver()) {
            return;
        }

        // Move ghosts
        for (Ghost ghost : ghosts) {
            ghost.move(walls, columns * tileSize, pacman, tileSize);
        }

        // Eat food and update score
        gameStateManager.eatFood(pacman, foods);

        // Check if level is complete
        if (gameStateManager.isLevelComplete(foods)) {
            gameStateManager.setNextLevel();
            loadBoard();
            boardLoader.resetPositions();
        }
    }*/
    private void updateGame() {
        // Skip update if game is over
        if (gameStateManager.isGameOver()) {
            return;
        }

        // Move pacman
        pacman.move(walls);

        // Check for ghost collisions
        int previousLives = gameStateManager.getLives();  // Guarda as vidas antes de checar colisão
        System.out.println("Antes da colisão: vidas = " + previousLives);

        gameStateManager.checkCollisions(pacman, ghosts);

        // 🚨 Adicione este println aqui para ver o que aconteceu depois da colisão:
        System.out.println("Depois da colisão: vidas = " + gameStateManager.getLives() +
                " | Game Over? " + gameStateManager.isGameOver());

        if (gameStateManager.isGameOver()) {
            return;
        }

        // Se perdeu uma vida (vidas diminuíram) e ainda não é game over
        if (gameStateManager.getLives() < previousLives && !gameStateManager.isGameOver()) {
            boardLoader.resetPositions();  // Reseta posições dos fantasmas e do Pac-Man
        }

        // Move ghosts
        for (Ghost ghost : ghosts) {
            ghost.move(walls, columns * tileSize, pacman, tileSize);
        }

        // Eat food and update score
        gameStateManager.eatFood(pacman, foods);

        // Check if level is complete
        if (gameStateManager.isLevelComplete(foods)) {
            gameStateManager.setNextLevel();
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
        // Not used
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Reset game with R key
        /*if (gameStateManager.isGameOver() || e.getKeyCode() == KeyEvent.VK_R) {
            resetGame();
            return;
        }*/
        if (e.getKeyCode() == KeyEvent.VK_R && gameStateManager.isGameOver()) {
            resetGame();
            return;
        }

        // Move pacman with arrow keys
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.updateDirection('U');
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.updateDirection('D');
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.updateDirection('R');
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.updateDirection('L');
        }

        // Update pacman's image based on direction
        pacman.updateImage();
    }
}

