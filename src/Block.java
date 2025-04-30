import java.awt.*;

/**
 * Base class for all game entities (Pacman, Ghosts, Walls, Food)
 */
public class Block {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Image image;

    protected int startX;
    protected int startY;
    protected char direction = 'R'; // Direcao padrao
    protected int velocityX = 0;
    protected int velocityY = 0;
    protected int moveSpeed;

    public Block(final int x, final int y, final int width, final int height, final Image image, final int moveSpeed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
        this.startX = x;
        this.startY = y;
        this.moveSpeed = moveSpeed;
    }

    public Block(final int x, final int y, final int width, final int height, final Image image) {
        this(x, y, width, height, image, 0);
    }

    public void updateDirection(char direction) {
        char prevDirection = this.direction;

        this.direction = direction;
        updateVelocity();

        this.x += this.velocityX;
        this.y += this.velocityY;

        // This will be handled in the subclasses that need collision detection
        this.x -= this.velocityX;
        this.y -= this.velocityY;
        this.direction = prevDirection;
        updateVelocity();
    }

    protected void updateVelocity() {
        if (this.direction == 'U') {
            this.velocityX = 0;
            this.velocityY = -moveSpeed;
        } else if (this.direction == 'D') {
            this.velocityX = 0;
            this.velocityY = moveSpeed;
        } else if (this.direction == 'L') {
            this.velocityX = -moveSpeed;
            this.velocityY = 0;
        } else if (this.direction == 'R') {
            this.velocityX = moveSpeed;
            this.velocityY = 0;
        }
    }

    public void reset() {
        this.x = this.startX;
        this.y = this.startY;
    }

    public void move() {
        x += velocityX;
        y += velocityY;
    }

    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public char getDirection() {
        return direction;
    }

    public int getVelocityX() {
        return velocityX;
    }

    public int getVelocityY() {
        return velocityY;
    }

    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }
}