import java.awt.*;
import java.util.HashSet;
import java.util.Random;

//Fantasma, materializa Block e tem uma estratégia pré-definida de busca
public class Ghost extends Block {
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
        this.direction = direction;
        updateVelocity();
    }

    public void move(HashSet<Block> walls, PacMan pacman, int tileSize) {
        // Quando está no meio do tile, recebe qual posicao deve ir (do alg) e evita travamento em viradas
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
    }

    public boolean collision(Block other) {
        return x < other.getX() + other.getWidth() &&
                x + width > other.getX() &&
                y < other.getY() + other.getHeight() &&
                y + height > other.getY();
    }

}

