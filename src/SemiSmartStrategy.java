import java.util.*;

public class SemiSmartStrategy implements SearchStrategy {

    private final int tileSize;
    private final Random random = new Random();

    public SemiSmartStrategy(int tileSize) {
        this.tileSize = tileSize;
    }

    @Override
    public char nextDirection(Ghost ghost, PacMan target, HashSet<Block> obstacles) {
        int ghostX = ghost.getX() / tileSize;
        int ghostY = ghost.getY() / tileSize;
        int targetX = target.getX() / tileSize;
        int targetY = target.getY() / tileSize;

        // Direções: (dx, dy, direção correspondente)
        int[][] directions = {
                {-1, 0, 'L'},
                {1, 0, 'R'},
                {0, -1, 'U'},
                {0, 1, 'D'}
        };

        List<int[]> possibleMoves = new ArrayList<>();

        // Filtra as direções possíveis (não pode bater em parede)
        for (int[] dir : directions) {
            int newX = ghostX + dir[0];
            int newY = ghostY + dir[1];

            if (!isObstacle(newX, newY, obstacles)) {
                possibleMoves.add(dir);
            }
        }

        if (possibleMoves.isEmpty()) {
            // Sem movimentos válidos -> continua na mesma direção
            return ghost.getDirection();
        }

        // Decide: 50% guloso, 50% aleatorio
        if (random.nextDouble() < 0.5) {
            // Escolhe a direção que minimiza a distância para o Pac-Man
            int[] bestMove = possibleMoves.getFirst();
            int bestDistance = distance(ghostX + bestMove[0], ghostY + bestMove[1], targetX, targetY);

            for (int[] move : possibleMoves) {
                int newDist = distance(ghostX + move[0], ghostY + move[1], targetX, targetY);
                if (newDist < bestDistance) {
                    bestDistance = newDist;
                    bestMove = move;
                }
            }
            return (char) bestMove[2];
        } else {
            // Escolhe aleatório entre os possíveis
            int[] randomMove = possibleMoves.get(random.nextInt(possibleMoves.size()));
            return (char) randomMove[2];
        }
    }

    // Distância Manhattan (mais simples para grid)
    private int distance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    // Checa se o bloco é obstáculo
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