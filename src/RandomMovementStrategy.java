import java.util.HashSet;
import java.util.Random;

public class RandomMovementStrategy implements SearchStrategy {
    private final Random random = new Random();
    private final char[] directions = {'U', 'D', 'L', 'R'};


    @Override
    public char nextDirection(final Ghost ghost, final PacMan target, final HashSet<Block> obstacles) {
        return directions[random.nextInt(4)];
    }
}
