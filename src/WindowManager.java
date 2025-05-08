import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Objects;

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

    public void drawSelectionPanel(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Roboto", Font.BOLD, 22));

        String title = "Selecione o nível:";
        int rectWidth = 300;
        int rectHeight = 100;
        int centerX = (getWidth() - rectWidth) / 2;
        int upperCenterY = 80;

        // Retângulo principal com borda decorada
        g2d.setColor(new Color(30, 70, 30));
        g2d.fillRoundRect(centerX, upperCenterY, rectWidth, rectHeight, 20, 20);

        // Texto do título
        g2d.setColor(Color.WHITE);
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2d.drawString(title, centerX + (rectWidth - titleWidth) / 2, upperCenterY + 30);

        // Opções horizontalmente alinhadas
        String[] options = {"1 - Fácil", "2 - Médio", "3 - Difícil"};
        int spacing = rectWidth / options.length;

        for (int i = 0; i < options.length; i++) {
            String option = options[i];
            int textWidth = fm.stringWidth(option);
            int x = centerX + i * spacing + (spacing - textWidth) / 2;
            int y = upperCenterY + 70;
            g2d.drawString(option, x, y);
        }

        g2d.drawImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("./assets/pacmanUp.png"))).getImage(), centerX,  rows * tileSize /2, 300, 300, null);
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
            g.drawString("Pressione R para reiniciar", tileSize / 2, tileSize + 30);
        }
        else {
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