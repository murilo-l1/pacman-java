import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;

/**
 * Responsible for loading the game board and initializing game entities
 */
public class BoardLoader {
    private final int tileSize;
    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image redGhostImage;
    private Image pinkGhostImage;
    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanRightImage;
    private Image pacmanLeftImage;

    private HashSet<Block> walls;
    private HashSet<Block> foods;
    private HashSet<Ghost> ghosts;
    private PacMan pacman;

    public BoardLoader(int tileSize, String wallAssetPath) {
        this.tileSize = tileSize;
        this.wallImage = new ImageIcon(Objects.requireNonNull(getClass().getResource(wallAssetPath))).getImage();
        loadImages();
    }

    private void loadImages() {
        blueGhostImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./assets/blueGhost.png"))).getImage();
        orangeGhostImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./assets/orangeGhost.png"))).getImage();
        redGhostImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./assets/redGhost.png"))).getImage();
        pinkGhostImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./assets/pinkGhost.png"))).getImage();

        pacmanUpImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./assets/pacmanUp.png"))).getImage();
        pacmanDownImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./assets/pacmanDown.png"))).getImage();
        pacmanRightImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./assets/pacmanRight.png"))).getImage();
        pacmanLeftImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./assets/pacmanLeft.png"))).getImage();
    }

    public void loadBoard(LevelType level) {
        String[] board = level.getBoard();
        walls = new HashSet<>();
        foods = new HashSet<>();
        ghosts = new HashSet<>();

        Random random = new Random();

        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length(); c++) {
                String row = board[r];
                char boardChar = row.charAt(c);

                int x = c * tileSize;
                int y = r * tileSize;

                switch (boardChar) {
                    case 'X':
                        Block wall = new Block(x, y, tileSize, tileSize, wallImage);
                        walls.add(wall);
                        break;
                    case 'b':
                        Ghost blueGhost = new Ghost(x, y, tileSize, tileSize / 4, blueGhostImage);
                        blueGhost.updateDirection('U'); // Initial direction
                        ghosts.add(blueGhost);
                        break;
                    case 'p':
                        Ghost pinkGhost = new Ghost(x, y, tileSize, tileSize / 4, pinkGhostImage);
                        pinkGhost.updateDirection('U'); // Initial direction
                        ghosts.add(pinkGhost);
                        break;
                    case 'o':
                        Ghost orangeGhost = new Ghost(x, y, tileSize, tileSize / 4, orangeGhostImage);
                        orangeGhost.updateDirection('U'); // Initial direction
                        ghosts.add(orangeGhost);
                        break;
                    case 'r':
                        Ghost redGhost = new Ghost(x, y, tileSize, tileSize / 4, redGhostImage);
                        redGhost.updateDirection('U'); // Initial direction
                        ghosts.add(redGhost);
                        break;
                    case 'P':
                        pacman = new PacMan(x, y, tileSize, tileSize / 4,
                                pacmanUpImage, pacmanDownImage,
                                pacmanRightImage, pacmanLeftImage);
                        break;
                    case ' ':
                        Block food = new Block(x + 14, y + 14, 4, 4, null);
                        foods.add(food);
                        break;
                }
            }
        }

        // Initialize random directions for ghosts
        for (Ghost ghost : ghosts) {
            char[] directions = {'U', 'D', 'L', 'R'};
            ghost.updateDirection(directions[random.nextInt(4)]);
        }
    }

    public HashSet<Block> getWalls() {
        return walls;
    }

    public HashSet<Block> getFoods() {
        return foods;
    }

    public HashSet<Ghost> getGhosts() {
        return ghosts;
    }

    public PacMan getPacman() {
        return pacman;
    }

    public void resetPositions() {
        if (pacman != null) {
            pacman.reset();
            pacman.setVelocityX(0);
            pacman.setVelocityY(0);
        }

        if (ghosts != null) {
            Random random = new Random();
            char[] directions = {'U', 'D', 'L', 'R'};

            for (Ghost ghost : ghosts) {
                ghost.reset();
                ghost.updateDirection(directions[random.nextInt(4)]);
            }
        }
    }
}

