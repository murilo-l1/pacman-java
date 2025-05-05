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

    /*public void move(HashSet<Block> walls, int maxX, PacMan pacman, int tileSize) {
        // Caso fantasma preso na linha 9, tem que ser corrigido com lógica correspondente nessa row.
        if (y == tileSize * 9 && direction != 'U' && direction != 'D') {
            updateDirection('U');
        }

        // Move the ghost using current velocities
        x += velocityX;
        y += velocityY;

        // Check for collisions with walls or boundaries
        boolean collided = false;
        for (Block wall : walls) {
            if (collision(wall) || x <= 0 || x + width >= maxX) {
                x -= velocityX;
                y -= velocityY;
                collided = true;
                break;
            }
        }

        // If there was a collision it's time to change direction
        if (collided || random.nextInt(20) == 0) {
            char newDirection = searchStrategy.nextDirection(this, pacman, walls);
            updateDirection(newDirection);
        }
    }*/

    public void move(HashSet<Block> walls, int maxX, PacMan pacman, int tileSize) {

        if (searchStrategy instanceof BFSStrategy) {
            // 👇 Só muda de direção se estiver alinhado na grid (evita travamento em viradas)
            if (x % tileSize == 0 && y % tileSize == 0) {
                char newDirection = searchStrategy.nextDirection(this, pacman, walls);
                updateDirection(newDirection);
            }
        }/* else {
            // 👇 Mantém o comportamento ORIGINAL para os outros fantasmas
            // Move o fantasma com as velocidades atuais
            x += velocityX;
            y += velocityY;

            boolean collided = false;
            for (Block wall : walls) {
                if (collision(wall) || x <= 0 || x + width >= maxX) {
                    x -= velocityX;
                    y -= velocityY;
                    collided = true;
                    break;
                }
            }

            if (collided || random.nextInt(20) == 0) {
                char newDirection = searchStrategy.nextDirection(this, pacman, walls);
                updateDirection(newDirection);
            }

            return;  // encerra para não executar novamente o movimento
        }*/

        // 👇 Move (o azul) após decidir nova direção
        x += velocityX;
        y += velocityY;

        // Checa colisões normalmente
        boolean collided = false;
        for (Block wall : walls) {
            if (collision(wall) || x <= 0 || x + width >= maxX) {
                x -= velocityX;
                y -= velocityY;
                collided = true;
                break;
            }
        }
    }

    public boolean collision(Block other) {
        return x < other.getX() + other.getWidth() &&
                x + width > other.getX() &&
                y < other.getY() + other.getHeight() &&
                y + height > other.getY();
    }

}

