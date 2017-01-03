import java.awt.*;

public class Cell {

    private int posX; // Posici�n X de la celda
    private int posY; // Posici�n Y de la celda
    private int size; // Tama�o de la celda

    private boolean show = false; // La celda est� visible?
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
        // este if solo es para debug
//        if (value == -1) {
//            show();
//            g.setColor(Color.BLACK);
//            g.fillRect(posX, posY, size, size);
//        }

        // Si la celda ha sido descubierta
        if(show) {
            g.setColor(Color.CYAN);
            g.fillRect(posX, posY, size, size);
            g.setColor(Color.BLACK);
            g.drawString("" + value, posX + 10, posY + 10);
        } else {
            // Si se ha colocado una bandera
            if (flag) {
                g.setColor(Color.PINK);
                g.fillRect(posX, posY, size, size);
                g.setColor(Color.BLACK);
                g.drawString("F", posX + 10, posY + 10);
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
