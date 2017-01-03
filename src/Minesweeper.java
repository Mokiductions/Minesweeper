import javax.swing.*;
import java.awt.*;

public class Minesweeper extends JFrame {

    private final int width = 200;
    private final int height = 200;
    private int cells = 10;
    private int bombs = 10;

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
        askForOptions();
        Game game = new Game(width, height, cells, bombs);
        c.add(game, BorderLayout.CENTER);
    } // End of initGUI()

    /**
     * Pide las opciones del juego al usuario, lado del tablero, bombas.
     */
    private void askForOptions() {
        // Dimension del tablero (lado, variable cells)
        cells = askForCells();

        // Cantidad de bombas
        bombs = askForBombs();
    } // End of askForOptions()

    /**
     * Pide al usuario cuantas celdas quiere por lado del tablero.
     *
     * @return Integer - Numero introducido por el usuario
     */
    private int askForCells() {
        int i = 10;
        String s = JOptionPane.showInputDialog("Numero de celdas (lado del cuadrado)");
        try {
            i = Integer.parseInt(s);
        } catch (Exception e) {
            askForCells();
        }
        return i;
    } // End of askForCells()

    /**
     * Pide al usuario cuantas bombas quiere en el tablero.
     *
     * @return Integer - Numero introducido por el usuario
     */
    private int askForBombs() {
        int i = 10;
        String s = JOptionPane.showInputDialog("Numero de bombas");
        try {
            i = Integer.parseInt(s);
        } catch (Exception e) {
            askForOptions();
        }
        return i;
    } // End of askForBombs()

    /**
     * Metodo main, crea el primer hilo de ejecucion del juego.
     *
     * @param args - not used
     */
    public static void main(String[] args) {
        new Minesweeper();
    }
}
