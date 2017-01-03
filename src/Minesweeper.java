import javax.swing.*;
import java.awt.*;

public class Minesweeper extends JFrame {

    public Minesweeper() {
        super("Minesweeper!");
        initGUI();
        // Deshabilita el redimensionamiento manual de la ventana
        setResizable(false);
        pack();
        // Hace que la ejecucion pare al cerrar la ventana
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // Centra la ventana en la pantalla
        setLocationRelativeTo(null);
        // Hace la ventana visible
        setVisible(true);
    } // End of Game() constructor

    /**
     * Metodo donde se inicializa el JPanel que sera contenido en el JFrame.
     */
    private void initGUI() {
        Container c = getContentPane();
        c.setLayout(new BorderLayout(0, 0));
        Game game = new Game();
        c.add(game, BorderLayout.CENTER);
    } // End of initGUI()

    /**
     * Metodo main, crea el primer hilo de ejecucion del juego.
     *
     * @param args - not used
     */
    public static void main(String[] args) {
        new Minesweeper();
    }
}
