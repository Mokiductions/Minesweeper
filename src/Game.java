import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Game extends JPanel implements Runnable {

    private int P_WIDTH = 300; // Ancho de la pantalla
    private int P_HEIGHT = 300; // Alto de la pantalla
    private int CELLS = 10; // Cantidad de celdas del tablero (siempre sera cuadrado)
    private int BOMBS = 1; // Cantidad de bombas en el tablero

    private Thread updater;
    private boolean running;

    private Cell[][] cells;

    private boolean gameOver = false;
    private boolean stop = false;

    private Graphics gr;
    private Image image = null;

    public Game(JFrame frame) {

        // Inicializa las celdas del tablero
        initCells();

        // Establece el fondo del panel
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(P_WIDTH, P_HEIGHT));

        // Pone el foco en el panel y llama al inicializador del escuchador de raton
        setFocusable(true);
        requestFocus();
        initMouseListener();
    }

    private void initCells() {
        ArrayList<Point> bombs = getBombsLocation();
        int cellSize = P_WIDTH / CELLS;
        cells = new Cell[CELLS][CELLS];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j] = new Cell(i * cellSize, j * cellSize, cellSize);
            }
        }
        for (Point p : bombs) {
            cells[p.x][p.y].setValue(-1);
        }
        initCellValues();
    }

    private ArrayList<Point> getBombsLocation() {
        ArrayList<Point> b = new ArrayList<>();
        Point p;
        for (int i = 0; i < BOMBS;) {
            p = new Point(ThreadLocalRandom.current().nextInt(0, CELLS),
                    ThreadLocalRandom.current().nextInt(0, CELLS));
            if(!b.contains(p)) {
                b.add(p);
                i++;
            }
        }
        return b;
    }

    private void initCellValues() {
        int value;
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                value = 0;
                if (cells[i][j].getValue() != -1) {
                    if (i < CELLS - 1  && cells[i + 1][j].getValue() == -1) {
                        value++;
                    }
                    if (i >= 1 && cells[i - 1][j].getValue() == -1) {
                        value++;
                    }
                    if (j < CELLS - 1 && cells[i][j + 1].getValue() == -1) {
                        value++;
                    }
                    if (j >= 1 && cells[i][j - 1].getValue() == -1) {
                        value++;
                    }
                    if (i < CELLS - 1 && j < CELLS - 1 && cells[i + 1][j + 1].getValue() == -1) {
                        value++;
                    }
                    if (i < CELLS - 1 && j >= 1 && cells[i + 1][j - 1].getValue() == -1) {
                        value++;
                    }
                    if (i >= 1 && j < CELLS - 1 && cells[i - 1][j + 1].getValue() == -1) {
                        value++;
                    }
                    if (i >= 1 && j >= 1 && cells[i - 1][j - 1].getValue() == -1) {
                        value++;
                    }
                    cells[i][j].setValue(value);
                }
            }
        }
    }

    private void initMouseListener() {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Nothing
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // Nothing
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Obtiene la celda en la que se ha hecho click
                Point p = new Point(e.getX(), e.getY());
                Point clickedCell = getClickedCell(p);
                Cell c = cells[clickedCell.x][clickedCell.y];

                // Gestion del click izquierdo
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (!c.hasFlag()) {
                        // Si la celda no tiene bandera, actua
                        if (c.getValue() == 0) {
                            // Si la celda contiene un 0, escanea las celdas adyacentes
                            scanForEmptyCells(clickedCell.x, clickedCell.y);
                        } else if (c.getValue() == -1) {
                            // Reproduce el sonido de la bomba.
                            //playBombSound();

                            // Destapa el tablero completamente


                            gameOver = true;
                        } else {
                            // La celda contiene un valor distinto a 0, la muestra
                            c.show();
                        }
                    }
                }

                // Gestion del click derecho
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (!c.isShowed()) {
                        // Si la celda no está mostrada, coloca bandera
                        c.flag();
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Nothing
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Nothing
            }
        });
    }

    private void showBoard() {
        for (Cell[] cellLine : cells) {
            for (Cell cell : cellLine) {
                if (!cell.isShowed()) {
                    cell.show();
                }
            }
        }
    }

    private void playBombSound() {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("PATH DE LA BOMBA.WAV").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch(Exception ex) {
            System.out.println("Error reproduciendo el archivo");
            ex.printStackTrace();
        }
    }

    /**
     * Devuelve la posicion X e Y de la celda clickada dentro del array
     * @param p Point - Punto del panel en el que ha hecho click el raton
     * @return Point - Posicion X e Y de la celda dentro del array
     */
    private Point getClickedCell(Point p) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                if (cells[i][j].isClicked(p)) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }

    private void scanForEmptyCells(int i, int j) {
        System.out.println(i + " " + j);
        if (!cells[i][j].isShowed()) {
            cells[i][j].show();
            // Eje horizontal
            if (i < CELLS - 1 && cells[i + 1][j].getValue() == 0) {
                scanForEmptyCells(i + 1, j);
            } else if (i < CELLS - 1) {
                cells[i + 1][j].show();
            }
            if (i >= 1 && cells[i - i][j].getValue() == 0) {
                scanForEmptyCells(i - 1, j);
            } else if (i >= 1) {
                cells[i - 1][j].show();
            }

            // Eje vertical
            if (j < CELLS - 1 && cells[i][j + 1].getValue() == 0) {
                scanForEmptyCells(i, j + 1);
            } else if (j < CELLS - 1) {
                cells[i][j + 1].show();
            }
            if (j >= 1 && cells[i][j - 1].getValue() == 0) {
                scanForEmptyCells(i, j - 1);
            } else if (j >= 1) {
                cells[i][j - 1].show();
            }

            // Diagonal creciente
            if (i < CELLS - 1 && j >= 1 && cells[i + 1][j - 1].getValue() == 0) {
                scanForEmptyCells(i + 1, j - 1);
            } else if(i < CELLS - 1 && j >= 1) {
                cells[i + 1][j - 1].show();
            }
            if (i >= 1 && j < CELLS - 1  && cells[i - 1][j + 1].getValue() == 0) {
                scanForEmptyCells(i - 1, j + 1);
            } else if(i >= 1 && j < CELLS - 1 ) {
                cells[i - 1][j + 1].show();
            }

            // Diagonal decreciente
            if (i >= 1 && j >= 1 && cells[i - 1][j - 1].getValue() == 0) {
                scanForEmptyCells(i - 1, j - 1);
            } else if(i >= 1 && j >= 1) {
                cells[i - 1][j - 1].show();
            }
            if (i < CELLS - 1 && j < CELLS - 1  && cells[i + 1][j + 1].getValue() == 0) {
                scanForEmptyCells(i + 1, j + 1);
            } else if(i < CELLS - 1 && j < CELLS - 1 ) {
                cells[i + 1][j + 1].show();
            }
        }
    }

    /**
     * Comprueba si queda alguna celda sin destapar que no sea bomba
     * @return
     */
    public boolean win() {
        boolean win = true;
        for (Cell[] cellLine : cells) {
            if (!win) {
                break;
            }
            for (Cell cell : cellLine) {
                if (cell.getValue() != 0 && !cell.isShowed()) {
                    win = false;
                    break;
                }
            }
        }
        return win;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        startGame();
    } // End of addNotify()


    private void startGame() {
        if (updater == null || !running) {
            updater = new Thread(this);
            updater.start();
        }
    } // End of startGame()

    /**
     * Comprueba si la partida ha finalizado o no.
     */
    private void gameUpdate() {
        if (gameOver) {
         // El usuario ha perdido
            System.out.println("perdiste, noob.");
            showBoard();
            stop = true;
        } else if (win()){
            // Comprobar si el usuario ha ganado (escanear el tablero en su totalidad)
            System.out.println("ganaste, proaso.");
            showBoard();
            stop = true;
        }
    } // End of gameUpdate()

    /**
     * Prepara el estado grafico del juego para ser pintado.
     */
    private void gameRender() {
        if (image == null) {
            image = createImage(P_WIDTH, P_HEIGHT);
            if (image == null) {
                System.out.println("Image is null");
                return;
            } else
                gr = image.getGraphics();
        }

        // Dibuja el tablero
        for (Cell[] cellLine : cells) {
            for (Cell cell : cellLine) {
                cell.draw(gr);
            }
        }
    } // End of gameRender()

    /**
     * Pinta la pantalla con el estado del juego.
     */
    private void paintScreen() {
        Graphics g;
        try {
            g = this.getGraphics();
            if ((g != null) && (image != null))
                g.drawImage(image, 0, 0, null);
            g.dispose();
        } catch (Exception e) {
            System.out.println("Error gr?fico: " + e);
        }
    } // End of paintScreen()

    @Override
    public void run() {
        running = true;
        while (running) {
            if (!stop) {
                gameUpdate();
                gameRender();
                paintScreen();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
