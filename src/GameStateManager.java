import java.util.HashSet;

/**
 * Manages the game state including score, lives, and game over conditions
 */
public class GameStateManager {
    private int score;
    private int lives;
    private boolean gameOver;
    private Level currentLevel;

    private long invulnerableUntil = 0;

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
        invulnerableUntil = 0; // Zera o tempo de invulnerabilidade ao resetar o jogo
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void checkCollisions(PacMan pacman, HashSet<Ghost> ghosts) {

        if (System.currentTimeMillis() < invulnerableUntil) {
            return;
        }

        for (Ghost ghost : ghosts) {
            if (pacman.collision(ghost)) {
                loseLife();
                return;
            }
        }
    }

    public void loseLife() {
        if (gameOver) {
            return;
        }

        lives--;
        if (lives <= 0) {
            lives = 0;
            gameOver = true;
        } else {
            invulnerableUntil = System.currentTimeMillis() + 2000;
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

    public static class Level {
        private final String[][] boards = new String[][]{
                board1, board2, board3
        };
        private int currentIndex = 0;


        //X = wall, O = skip, P = pac man, ' ' = food
        //Ghosts: b = blue, o = orange, p = pink, r = red
        private static final String[] board1 = {
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

        private static final String[] board2 = {
                "XXXXXXXXXXXXXXXXXXX",
                "X   X   X   X     X",
                "X X X XXX X XXX X X",
                "X X             X X",
                "X X XXX XXX XXX X X",
                "X X   X     X   X X",
                "XXX X XXXXXXX X XXX",
                "OOO X         X OOO",
                "XXX X XXrXX X X XXX",
                "O   X   bpo   X   O",
                "XXX X XXXXX X X XXX",
                "OOO X       X X OOO",
                "XXX XXXXXXX XXX X X",
                "X   X   X   X   X X",
                "X X XXX X XXX X X X",
                "X X   X   P   X X X",
                "X XXXXX XXXXXXX X X",
                "X     X   X     X X",
                "X XXXXX X XXXXXXX X",
                "X                 X",
                "XXXXXXXXXXXXXXXXXXX"
        };

        private static final String[] board3 = {
                "XXXXXXXXXXXXXXXXXXX",
                "X        X        X",
                "X XXXX X X X XXXX X",
                "X                 X",
                "X XX X XXXXX X XX X",
                "X    X   X   X    X",
                "XXXX X XXXXX X XXXX",
                "OOOX X       X XOOO",
                "XXXX X XXrXX X XXXX",
                "O    b   p o      O",
                "XXXX X XXXXX X XXXX",
                "OOOX X       X XOOO",
                "XXXX XXXXXXX X XXXX",
                "X     X           X",
                "X XXX X XXX X XXX X",
                "X X   X  P  X   X X",
                "X X X XXXXX X X X X",
                "X   X   X   X   X X",
                "X XXXXX X XXXXXXX X",
                "X                 X",
                "XXXXXXXXXXXXXXXXXXX"
        };

        public void next() {
            if (currentIndex < boards.length - 1) {
                currentIndex++;
            }
            else {
                currentIndex = 0;
            }
        }

        public String[] getBoard() {
            return boards[currentIndex];
        }

        public int currentIndex() {
            return currentIndex;
        }
    }

}