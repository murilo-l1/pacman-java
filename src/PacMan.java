import java.awt.*;
import java.util.HashSet;

/**
 * Represents the player character in the game
 */
public class PacMan extends Block {

    //ícones do pacman
    private final Image pacmanUp;
    private final Image pacmanDown;
    private final Image pacmanRight;
    private final Image pacmanLeft;

    public PacMan(final int x, final int y, final int size, final int moveSpeed,
                  final Image up, final Image down, final Image right, final Image left) {
        super(x, y, size, size, right, moveSpeed);

        this.pacmanUp = up;
        this.pacmanDown = down;
        this.pacmanRight = right;
        this.pacmanLeft = left;
    }

    @Override
    public void updateDirection(char direction) {
        char prevDirection = this.direction;

        this.direction = direction;
        updateVelocity();
        updateImage();
    }

    public void updateImage() {
        if (direction == 'U') {
            image = pacmanUp;
        } else if (direction == 'D') {
            image = pacmanDown;
        } else if (direction == 'R') {
            image = pacmanRight;
        } else if (direction == 'L') {
            image = pacmanLeft;
        }
    }

    public boolean move(HashSet<Block> walls) {
        x += velocityX;
        y += velocityY;

        for (final Block wall : walls) {
            if (collision(wall)) {
                x -= velocityX;
                y -= velocityY;
                return false;
            }
        }
        return true;
    }

    public boolean collision(Block other) {
        return x < other.getX() + other.getWidth() &&
                x + width > other.getX() &&
                y < other.getY() + other.getHeight() &&
                y + height > other.getY();
    }

    // quando o pacman colide com uma comida, removemos ela do hashset para nao ser renderizada no prox frame
    public boolean eatFood(HashSet<Block> foods) {
        Block foodEaten = null;
        for (final Block food : foods) {
            if (collision(food)) {
                foodEaten = food;
                break;
            }
        }

        if (foodEaten != null) {
            foods.remove(foodEaten);
            return true;
        }
        return false;
    }

}