import java.awt.*;
import java.util.HashSet;

//Materializa o pacman, move-o, checa colisoes com parede, comida e fantasma e trata caso sim.
public class PacMan extends Block {

    // Ícones do pacman
    private final Image pacmanUp;
    private final Image pacmanDown;
    private final Image pacmanRight;
    private final Image pacmanLeft;

    // Armazena para onde ele vai se mover
    private char nextDirection = ' ';

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
        // Agora só guardamos a direção desejada para aplicar depois
        this.nextDirection = direction;
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
        // Tentamos mudar para a direção desejada (se não for a mesma e for possível)
        if (nextDirection != direction && canMove(nextDirection, walls)) {
            direction = nextDirection;      // aplicamos a nova direção
            updateVelocity();               // atualizamos a velocidade para a nova direção
            updateImage();                  // atualizamos a imagem do Pac-Man
        }

        // Move na direção atual
        x += velocityX;
        y += velocityY;

        // Verifica se bateu numa parede
        for (final Block wall : walls) {
            if (collision(wall)) {
                // desfaz o movimento
                x -= velocityX;
                y -= velocityY;
                return false;
            }
        }

        if (x + width < 0) {
            // Saiu totalmente pela esquerda → teleporta para a direita
            x = 608;
        } else if (x > 608) {
            // Saiu totalmente pela direita → teleporta para a esquerda
            x = -width;
        }

        return true;
    }

    // Verifica se é possível se mover na direção informada sem colidir
    private boolean canMove(char direction, HashSet<Block> walls) {
        // Fazemos um "teste" de movimento (não move de verdade)
        int testX = x;
        int testY = y;

        if (direction == 'U') {
            testY -= moveSpeed;
        } else if (direction == 'D') {
            testY += moveSpeed;
        } else if (direction == 'L') {
            testX -= moveSpeed;
        } else if (direction == 'R') {
            testX += moveSpeed;
        } else {
            return false; // direção inválida
        }

        // Verifica se esse novo ponto colidiria com alguma parede
        for (final Block wall : walls) {
            if (testX < wall.getX() + wall.getWidth() &&
                    testX + width > wall.getX() &&
                    testY < wall.getY() + wall.getHeight() &&
                    testY + height > wall.getY()) {
                return false; // bateu numa parede ⇾ não pode mover
            }
        }
        return true; // está livre ⇾ pode mover
    }

    public boolean collision(Block other) {
        return x < other.getX() + other.getWidth() &&
                x + width > other.getX() &&
                y < other.getY() + other.getHeight() &&
                y + height > other.getY();
    }

    // Quando o pacman colide com uma comida, removemos ela do hashset para não ser renderizada no próximo frame
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
