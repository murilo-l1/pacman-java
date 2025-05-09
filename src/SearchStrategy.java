import java.util.HashSet;

//Padrao de Projeto Strategy (todos algoritmos de busca implementam o nextDirection)
public interface SearchStrategy {
    char nextDirection(Ghost ghost, PacMan target, HashSet<Block> obstacles);
}