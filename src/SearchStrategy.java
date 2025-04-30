import java.util.HashSet;

/**
 * Strategy interface for ghost movement algorithms
 */
public interface SearchStrategy {
    char nextDirection(Ghost ghost, PacMan target, HashSet<Block> obstacles);
}


// Novas strategies para execucao dos algoritmos serao implementadas depois:
// - BFS (breadth-first search)
// - DFS (depth-first search)
// - A* (A-star search)