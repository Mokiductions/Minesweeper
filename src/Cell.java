import java.awt.*;

public class Cell {

    private int posX; // Posición X de la celda
    private int posY; // Posición Y de la celda
    private int size; // Tamaño de la celda

    private boolean show = false; // La celda está visible?
    private boolean flag = false; // La celda tiene bandera?

    private int value; // Valor de la celda (-1 = BOMBA)

    public Cell(int posX, int posY, int size) {
        this.posX = posX;
        this.posY = posY;
        this.size = size;
    } // End of Cell() constructor

    public void setValue(int value) {
        this.value = value;
    } // End of setValue()

    public int getValue() {
        return value;
    } // End of getValue()

    public Rectangle cellLimits() {
        return new Rectangle(posX, posY, size, size);
    } // End of cellLimits()

    public boolean isClicked(Point p) {
        return cellLimits().contains(p);
    } // End of isClicked()

    public void show() {
        show = true;
    } // End of show()

    public boolean isShowed() {
        return show;
    } // End of isShowed()

    public void flag() {
        flag = !flag;
    } // End of flag()

    public boolean hasFlag() {
        return flag;
    } // End of hasFlag()

    public void draw(Graphics g) {
        // Si la celda ha sido descubierta
        if (show) {
            if (value == -1) {
                // Si era una bomba
                g.setColor(Color.BLACK);
                g.fillRect(posX, posY, size, size);
                g.setColor(Color.WHITE);
                g.drawString("B", posX + 10, posY + 15);
            } else {
                // Si no es una bomba
                g.setColor(Color.CYAN);
                g.fillRect(posX, posY, size, size);
            }
            if (value > 0) {
                // Si el valor es mayor a 0, muestra el número
                g.setColor(Color.BLACK);
                g.drawString("" + value, posX + 10, posY + 15);
            }
        } else {
            // Si se ha colocado una bandera
            if (flag) {
                g.setColor(Color.PINK);
                g.fillRect(posX, posY, size, size);
            } else {
                g.setColor(Color.WHITE);
                g.fillRect(posX, posY, size, size);
            }
        }

        // Dibuja el borde del cuadrado
        g.setColor(Color.BLACK);
        g.drawRect(posX, posY, size, size);
    }
}
