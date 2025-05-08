import java.awt.*;
import java.util.HashSet;
import java.util.Random;

/**
 * Represents an enemy ghost that chases the player
 */
public class Ghost extends Block {
    private final Random random = new Random();
    private SearchStrategy searchStrategy;

    public Ghost(final int x, final int y, final int size, final int moveSpeed, final Image image) {
        super(x, y, size, size, image, moveSpeed);
        this.searchStrategy = new RandomMovementStrategy();
    }

    public void setSearchStrategy(SearchStrategy strategy) {
        this.searchStrategy = strategy;
    }

    @Override
    public void updateDirection(char direction) {
        char prevDirection = this.direction;

        this.direction = direction;
        updateVelocity();
    }

    public void move(HashSet<Block> walls, int maxX, PacMan pacman, int tileSize) {

        // Só muda de direção se estiver alinhado na grid (evita travamento em viradas)
        if (x % tileSize == 0 && y % tileSize == 0) {
            char newDirection = searchStrategy.nextDirection(this, pacman, walls);
            updateDirection(newDirection);
        }

        x += velocityX;
        y += velocityY;

        for (Block wall : walls) {
            if (collision(wall)) {
                x -= velocityX;
                y -= velocityY;
                break;
            }
        }

        if (x + width < 0) {
            // Saiu totalmente pela esquerda → teleporta para a direita
            x = 608;
        } else if (x > 608) {
            // Saiu totalmente pela direita → teleporta para a esquerda
            x = -width;
        }
    }

    public boolean collision(Block other) {
        return x < other.getX() + other.getWidth() &&
                x + width > other.getX() &&
                y < other.getY() + other.getHeight() &&
                y + height > other.getY();
    }

}

