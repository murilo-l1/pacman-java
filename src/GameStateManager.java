import java.util.HashSet;

//Toma conta das vidas, pontucacao, caso de derrota
public class GameStateManager {
    private int score;
    private int lives;
    private boolean gameOver;
    private final LevelType currentLevel;

    private long invulnerableUntil = 0;

    public GameStateManager(LevelType level) {
        this.score = 0;
        this.lives = level.getLives();
        this.gameOver = false;
        this.currentLevel = level;
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

    //colisa fantasma e pacman: perde vida
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

    //incrementa score se colidiu pacman com comida
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

    public LevelType getCurrentLevel() {
        return currentLevel;
    }

}