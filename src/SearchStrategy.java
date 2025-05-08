import java.util.HashSet;

public interface SearchStrategy {
    char nextDirection(Ghost ghost, PacMan target, HashSet<Block> obstacles);
}