import java.util.*;

public class BFSStrategy implements SearchStrategy {

    private final int tileSize;
    private final int columns = 19; // Número de colunas do mapa
    private final int rows = 21;    // Número de linhas do mapa

    public BFSStrategy(int tileSize) {
        this.tileSize = tileSize;
    }

    @Override
    public char nextDirection(Ghost ghost, PacMan target, HashSet<Block> obstacles) {
        int startX = ghost.getX() / tileSize;
        int startY = ghost.getY() / tileSize;
        int targetX = target.getX() / tileSize;
        int targetY = target.getY() / tileSize;

        System.out.println("Fantasma: (" + startX + ", " + startY + ") -> Pac-Man: (" + targetX + ", " + targetY + ")");

        // Estrutura para BFS
        Queue<Node> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        // Começa da posição atual
        queue.add(new Node(startX, startY, ""));
        visited.add(startX + "," + startY);

        // Direções: (dx, dy, direção correspondente)
        int[][] directions = {
                {-1, 0, 'L'},
                {1, 0, 'R'},
                {0, -1, 'U'},
                {0, 1, 'D'}
        };

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            // Se achou o Pac-Man
            /*if (current.x == targetX && current.y == targetY) {
                // Retorna o primeiro movimento do caminho
                return current.path.charAt(0);
            }*/
            if (Math.abs(current.x - targetX) <= 0 && Math.abs(current.y - targetY) <= 0) {
                System.out.println("ACHEI O PACMAN na BFS em: (" + current.x + ", " + current.y + ")");
                return current.path.charAt(0);
            }


            // Explora os vizinhos
            for (int[] dir : directions) {
                int newX = current.x + dir[0];
                int newY = current.y + dir[1];
                char move = (char) dir[2];

                // 🚨 Limita para não sair do mapa
                if (newX < 0 || newX >= columns || newY < 0 || newY >= rows) {
                    continue;  // Ignora posições fora do mapa
                }

                String key = newX + "," + newY;

                if (visited.contains(key)) continue;
                if (isObstacle(newX, newY, obstacles)) continue;

                visited.add(key);
                queue.add(new Node(newX, newY, current.path + move));
            }
            System.out.println("Visitando: (" + current.x + ", " + current.y + ") | Caminho: " + current.path);
        }

        // Se não encontrou caminho, faz movimento aleatório
        return new RandomMovementStrategy().nextDirection(ghost, target, obstacles);
    }

    // Checa se a célula é uma parede
    private boolean isObstacle(int gridX, int gridY, HashSet<Block> obstacles) {
        int pixelX = gridX * tileSize;
        int pixelY = gridY * tileSize;
        for (Block wall : obstacles) {
            if (wall.getX() == pixelX && wall.getY() == pixelY) {
                return true;
            }
        }
        System.out.println("Checando obstáculo em: (" + gridX + ", " + gridY + ")");
        return false;
    }

    // Classe interna para guardar posição + caminho
    private static class Node {
        int x, y;
        String path;

        Node(int x, int y, String path) {
            this.x = x;
            this.y = y;
            this.path = path;
        }
    }
}
