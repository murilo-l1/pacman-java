import java.util.HashSet;

public class GreedyStrategy implements SearchStrategy {

    private final int tileSize;

    public GreedyStrategy(int tileSize) {
        this.tileSize = tileSize;
    }

    @Override
    public char nextDirection(Ghost ghost, PacMan target, HashSet<Block> obstacles) {
        int startX = ghost.getX() / tileSize;
        int startY = ghost.getY() / tileSize;
        int targetX = target.getX() / tileSize;
        int targetY = target.getY() / tileSize;

        // Direções: (dx, dy, direção correspondente)
        int[][] directions = {
                {-1, 0, 'L'},
                {1, 0, 'R'},
                {0, -1, 'U'},
                {0, 1, 'D'}
        };

        char bestDirection = ghost.getDirection();  // Por padrão, mantém direção atual
        int minDistance = Integer.MAX_VALUE;

        for (int[] dir : directions) {
            int newX = startX + dir[0];
            int newY = startY + dir[1];

            // Checa se é parede
            if (isObstacle(newX, newY, obstacles)) continue;

            // Calcula a distância de Manhattan (poderia ser Euclidiana também)
            int distance = Math.abs(newX - targetX) + Math.abs(newY - targetY);

            if (distance < minDistance) {
                minDistance = distance;
                bestDirection = (char) dir[2];
            }
        }

        return bestDirection;
    }

    private boolean isObstacle(int gridX, int gridY, HashSet<Block> obstacles) {
        int pixelX = gridX * tileSize;
        int pixelY = gridY * tileSize;
        for (Block wall : obstacles) {
            if (wall.getX() == pixelX && wall.getY() == pixelY) {
                return true;
            }
        }
        return false;
    }
}