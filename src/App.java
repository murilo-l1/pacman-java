import javax.swing.*;

public class App {

    /*
    * Murilo Lusvarghi Garcia
    * Paulo Henrique Korovski Gavlak
    * */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int columns = 19;
            int rows = 21;
            int tileSize = 32;

            final JFrame window = new JFrame("Pac Man");
            window.setSize(columns * tileSize, rows * tileSize);
            window.setResizable(false);
            window.setLocationRelativeTo(null);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            final Game game = new Game();
            window.add(game);
            window.pack();
            game.requestFocus();
            window.setVisible(true);
        });
    }
}