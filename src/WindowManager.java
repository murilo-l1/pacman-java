import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

/**
 * Handles rendering and window-related operations
 */
public class WindowManager {
    private final int columns;
    private final int rows;
    private final int tileSize;
    private final JPanel gamePanel;

    public WindowManager(int columns, int rows, int tileSize, JPanel gamePanel) {
        this.columns = columns;
        this.rows = rows;
        this.tileSize = tileSize;
        this.gamePanel = gamePanel;
    }

    public void setupWindow(String title) {
        final JFrame window = new JFrame(title);
        window.setSize(columns * tileSize, rows * tileSize);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.add(gamePanel);
        window.pack();
        gamePanel.requestFocus();
        window.setVisible(true);
    }

    public void render(Graphics g, PacMan pacman, HashSet<Ghost> ghosts,
                       HashSet<Block> walls, HashSet<Block> foods,
                       GameStateManager gameState) {
        // Clear the screen
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, columns * tileSize, rows * tileSize);

        // Draw walls
        for (Block wall : walls) {
            wall.draw(g);
        }

        // Draw food
        g.setColor(Color.WHITE);
        for (Block food : foods) {
            g.fillRect(food.getX(), food.getY(), food.getWidth(), food.getHeight());
        }

        // Draw pacman
        pacman.draw(g);

        // Draw ghosts
        for (Ghost ghost : ghosts) {
            ghost.draw(g);
        }

        // Draw score and lives
        drawHUD(g, gameState);
    }

    private void drawHUD(Graphics g, GameStateManager gameState) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Verdana", Font.PLAIN, 20));

        if (gameState.isGameOver()) {
            g.drawString("GAME OVER " + gameState.getScore(), tileSize/2, tileSize/2);
        } else {
            g.drawString("x" + gameState.getLives() + " " + "SCORE: " + gameState.getScore(),
                    tileSize/2, tileSize/2);
        }
    }

    public int getWidth() {
        return columns * tileSize;
    }

    public Dimension getPanelDimension() {
        return new Dimension(columns * tileSize, rows * tileSize);
    }
}