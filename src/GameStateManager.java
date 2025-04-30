import java.util.HashSet;

/**
 * Manages the game state including score, lives, and game over conditions
 */
public class GameStateManager {
    private int score;
    private int lives;
    private boolean gameOver;
    private Level currentLevel;

    public GameStateManager() {
        this.score = 0;
        this.lives = 3;
        this.gameOver = false;
        this.currentLevel = new Level();
    }

    public void reset() {
        score = 0;
        lives = 3;
        gameOver = false;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void checkCollisions(PacMan pacman, HashSet<Ghost> ghosts) {
        for (Ghost ghost : ghosts) {
            if (pacman.collision(ghost)) {
                loseLife();
                return;
            }
        }
    }

    public void loseLife() {
        lives--;
        if (lives <= 0) {
            gameOver = true;
        }
    }

    public boolean eatFood(PacMan pacman, HashSet<Block> foods) {
        boolean eaten = pacman.eatFood(foods);
        if (eaten) {
            score += 10;
        }
        return eaten;
    }

    public boolean isLevelComplete(HashSet<Block> foods) {
        return foods.isEmpty();
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void setNextLevel() {
        // In a full implementation, this would load the next level
        currentLevel = new Level();
    }

    /**
     * Static class that represents boards layouts, changes by level the player's in
     */
    public static class Level {
        //X = wall, O = skip, P = pac man, ' ' = food
        //Ghosts: b = blue, o = orange, p = pink, r = red
        private String[] board = {
                "XXXXXXXXXXXXXXXXXXX",
                "X        X        X",
                "X XX XXX X XXX XX X",
                "X                 X",
                "X XX X XXXXX X XX X",
                "X    X       X    X",
                "XXXX XXXX XXXX XXXX",
                "OOOX X       X XOOO",
                "XXXX X XXrXX X XXXX",
                "O       bpo        ",
                "XXXX X XXXXX X XXXX",
                "OOOX X       X XOOO",
                "XXXX X XXXXX X XXXX",
                "X        X        X",
                "X XX XXX X XXX XX X",
                "X  X   X  P  X   X  X",
                "XX X X XXXXX X X XX",
                "X    X   X   X    X",
                "X XXXXXX X XXXXXX X",
                "X                 X",
                "XXXXXXXXXXXXXXXXXXX"
        };

        public String[] getBoard() {
            return board;
        }
    }
}