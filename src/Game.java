import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Game extends JPanel implements Runnable {

    private int P_WIDTH = 300; // Ancho de la pantalla
    private int P_HEIGHT = 300; // Alto de la pantalla
    private int CELLS = 10;
    private int BOMBS = 10;

    private Thread updater;
    private boolean running;

    private Cell[][] cells;

    private Graphics gr;
    private Image image = null;

    public Game(JFrame frame) {

        initCells();

        // Establece el fondo del panel
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(P_WIDTH, P_HEIGHT));

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
                Point p = new Point(e.getX(), e.getY());
                Point clickedCell = getClickedCell(p);
                Cell c = cells[clickedCell.x][clickedCell.y];
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (!c.hasFlag()) {
                        if (c.getValue() == 0) {
                            scanForEmptyCells(clickedCell.x, clickedCell.y);
                        } else if (c.getValue() == -1) {
                            System.out.println("BOMBA");
                        } else {
                            System.out.println("Valor:" + c.getValue());
                            c.show();
                        }
                    }
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    System.out.println("Clicked right");
                    if (!c.isShowed()) {
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

    private void gameUpdate() {

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
            gameUpdate();
            gameRender();
            paintScreen();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
