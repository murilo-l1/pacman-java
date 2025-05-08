import java.util.*;

public class BFSStrategy implements SearchStrategy {

    private final int tileSize;
    private final int columns = 19;
    private final int rows = 21;

    private List<Node> lastPath;
    private long lastPathCalculationTime;
    private static final long PATH_RECALCULATION_DELAY = 500;

    public BFSStrategy(int tileSize) {
        this.tileSize = tileSize;
        this.lastPath = new ArrayList<>();
        this.lastPathCalculationTime = 0;
    }

    @Override
    public char nextDirection(Ghost ghost, PacMan target, HashSet<Block> obstacles) {
        int startX = ghost.getX() / tileSize;
        int startY = ghost.getY() / tileSize;
        int targetX = target.getX() / tileSize;
        int targetY = target.getY() / tileSize;

        long currentTime = System.currentTimeMillis();
        if (lastPath.isEmpty() || currentTime - lastPathCalculationTime > PATH_RECALCULATION_DELAY) {
            lastPath = findPath(startX, startY, targetX, targetY, obstacles);
            lastPathCalculationTime = currentTime;
        }

        if (lastPath.isEmpty() || lastPath.size() < 2) {
            return new RandomMovementStrategy().nextDirection(ghost, target, obstacles);
        }

        Node nextNode = lastPath.get(1); // o 0 é a posição atual

        if (nextNode.x < startX) return 'L';
        if (nextNode.x > startX) return 'R';
        if (nextNode.y < startY) return 'U';
        if (nextNode.y > startY) return 'D';

        return new RandomMovementStrategy().nextDirection(ghost, target, obstacles);
    }

    private List<Node> findPath(int startX, int startY, int targetX, int targetY, HashSet<Block> obstacles) {
        Queue<Node> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, Node> allNodes = new HashMap<>();

        Node startNode = new Node(startX, startY, null);
        queue.add(startNode);
        visited.add(startX + "," + startY);
        allNodes.put(startNode.getKey(), startNode);

        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}
        };

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current.x == targetX && current.y == targetY) {
                return reconstructPath(current);
            }

            for (int[] dir : directions) {
                int newX = current.x + dir[0];
                int newY = current.y + dir[1];

                if (newX < 0 || newX >= columns || newY < 0 || newY >= rows) continue;
                if (isObstacle(newX, newY, obstacles)) continue;

                String key = newX + "," + newY;
                if (visited.contains(key)) continue;

                Node neighbor = new Node(newX, newY, current);
                visited.add(key);
                queue.add(neighbor);
                allNodes.put(key, neighbor);
            }
        }

        return new ArrayList<>();
    }

    private List<Node> reconstructPath(Node goal) {
        List<Node> path = new ArrayList<>();
        Node current = goal;

        while (current != null) {
            path.add(0, current);
            current = current.parent;
        }

        return path;
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

    private static class Node {
        int x, y;
        Node parent;

        Node(int x, int y, Node parent) {
            this.x = x;
            this.y = y;
            this.parent = parent;
        }

        public String getKey() {
            return x + "," + y;
        }
    }
}
