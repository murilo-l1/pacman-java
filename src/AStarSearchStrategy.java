import java.util.*;

public class AStarSearchStrategy implements SearchStrategy {
    private final int tileSize;
    private final int columns = 19;
    private final int rows = 21;

    private List<Node> lastPath; //ultimo caminho encontrado,
    private long lastPathCalculationTime;
    private static final long PATH_RECALCULATION_DELAY = 250; // Recalcula o caminho a cada 250ms, quanto menor, mais desgracado é

    public AStarSearchStrategy(int tileSize) {
        this.tileSize = tileSize;
        this.lastPathCalculationTime = 0;
        this.lastPath = new ArrayList<>();
    }

    @Override
    public char nextDirection(Ghost ghost, PacMan target, HashSet<Block> obstacles) {
        int ghostX = ghost.getX() / tileSize;
        int ghostY = ghost.getY() / tileSize;
        int targetX = target.getX() / tileSize;
        int targetY = target.getY() / tileSize;

        // Verifica se precisa recalcular o caminho (passou tempo suficiente ou não há caminho)
        long currentTime = System.currentTimeMillis();
        if (lastPath.isEmpty() || currentTime - lastPathCalculationTime > PATH_RECALCULATION_DELAY) {
            // Converte obstáculos para um grid de células ocupadas
            boolean[][] occupiedCells = new boolean[columns][rows];
            for (Block wall : obstacles) {
                int wallX = wall.getX() / tileSize;
                int wallY = wall.getY() / tileSize;
                if (wallX >= 0 && wallX < columns && wallY >= 0 && wallY < rows) {
                    occupiedCells[wallX][wallY] = true;
                }
            }

            // Calcula o novo caminho usando A*
            lastPath = findPath(ghostX, ghostY, targetX, targetY, occupiedCells);
            lastPathCalculationTime = currentTime;
        }

        // Se não há caminho, move-se aleatoriamente
        if (lastPath.isEmpty() || lastPath.size() < 2) {
            return getRandomDirection();
        }

        // O próximo nó no caminho (o primeiro é a posição atual do fantasma)
        Node nextNode = lastPath.size() > 1 ? lastPath.get(1) : lastPath.get(0);

        // Determina a direção para o próximo nó
        if (nextNode.x < ghostX) return 'L';
        if (nextNode.x > ghostX) return 'R';
        if (nextNode.y < ghostY) return 'U';
        if (nextNode.y > ghostY) return 'D';

        // Se não conseguir determinar direção (improvável), move aleatoriamente
        return getRandomDirection();
    }

    private char getRandomDirection() {
        char[] directions = {'U', 'D', 'L', 'R'};
        return directions[new Random().nextInt(4)];
    }

    private List<Node> findPath(int startX, int startY, int goalX, int goalY, boolean[][] occupiedCells) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(); //nós que devem ser analisados, ordenados pelo menor custo em f()
        Set<String> closedSet = new HashSet<>(); //nós já visitados, para que só processe aquilo que não sabemos o custo dado por f()
        Map<String, Node> allNodes = new HashMap<>();

        // Cria o nó inicial (distancia 0 do fantasma ao nó sendo calculado, g() faz o calculo heuristico dad)
        Node startNode = new Node(startX, startY);
        startNode.g = 0;
        startNode.h = calculateHeuristic(startX, startY, goalX, goalY);
        startNode.f = startNode.g + startNode.h;

        openSet.add(startNode);
        allNodes.put(startNode.getKey(), startNode);

        //enquanto nao exploramos todos os caminhos até o pacman, continue processando os adjacentes
        while (!openSet.isEmpty()) {
            Node current = openSet.poll(); //nó que vai ser processado é armazenado e sai da lista a serem visitados

            // Se chegou ao objetivo, reconstrói o caminho
            if (current.x == goalX && current.y == goalY) {
                return reconstructPath(current);
            }

            closedSet.add(current.getKey());

            // Explora os vizinhos do nó sendo processado (cima, baixo, dir, esq)
            int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
            for (int[] dir : directions) {
                int newX = current.x + dir[0];
                int newY = current.y + dir[1];

                // Verifica se está dentro dos limites do grid
                if (newX < 0 || newX >= columns || newY < 0 || newY >= rows) {
                    continue;
                }

                // Verifica se a célula é parede
                if (occupiedCells[newX][newY]) {
                    continue;
                }

                //verifica se já foi visitado
                String neighborKey = newX + "," + newY;
                if (closedSet.contains(neighborKey)) {
                    continue;
                }

                // Calcula o custo do caminho até este vizinho
                double tentativeG = current.g + 1; // Custo para mover para um vizinho adjacente é 1

                Node neighbor = allNodes.getOrDefault(neighborKey, new Node(newX, newY));
                if (!allNodes.containsKey(neighborKey)) {
                    allNodes.put(neighborKey, neighbor);
                }

                if (!openSet.contains(neighbor) || tentativeG < neighbor.g) {
                    neighbor.parent = current;
                    neighbor.g = tentativeG;
                    neighbor.h = calculateHeuristic(newX, newY, goalX, goalY);
                    neighbor.f = neighbor.g + neighbor.h;

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    } else {
                        // Se o nó já está no openSet, atualiza o valor de F
                        openSet.remove(neighbor);
                        openSet.add(neighbor);
                    }
                }
            }
        }

        // Se não encontrou caminho, retorna lista vazia
        return new ArrayList<>();
    }

    // calculo de h (heuristica) pela distancia de Manhattam
    private double calculateHeuristic(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }


    private List<Node> reconstructPath(Node goal) {
        List<Node> path = new ArrayList<>();
        Node current = goal;

        while (current != null) {
            path.addFirst(current);
            current = current.parent;
        }

        return path;
    }

    // classe de no ⇾ pontos cartesianos, funcoes g, h e f: (g + h)
    private static class Node implements Comparable<Node> {
        int x, y;
        double g; // Distância do fantasma até o nó sendo processado
        double h; // Distância do pacman até o nó sendo processado
        double f; // Função de avaliação f = g + h
        Node parent;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
            this.g = 0;
            this.h = 0;
            this.f = 0;
            this.parent = null;
        }

        public String getKey() {
            return x + "," + y;
        }

        @Override
        public int compareTo(Node other) {
            return Double.compare(this.f, other.f);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Node node = (Node) obj;
            return x == node.x && y == node.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}