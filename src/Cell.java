import java.awt.*;

public class Cell {

    private int posX;
    private int posY;
    private int size;

    private boolean show = false;
    private boolean flag = false;

    private int value;

    public Cell(int posX, int posY, int size) {
        this.posX = posX;
        this.posY = posY;
        this.size = size;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private Rectangle cellLimits() {
        return new Rectangle(posX, posY, size, size);
    }

    public boolean isClicked(Point p) {
        return cellLimits().contains(p);
    }

    public void show() {
        show = true;
    }

    public boolean isShowed() {
        return show;
    }

    public void flag() {
        flag = !flag;
        System.out.println(flag);
    }

    public boolean hasFlag() {
        return flag;
    }

    public void draw(Graphics g) {
        // este if solo es para debug
        if (value == -1) {
            show();
            g.setColor(Color.BLACK);
            g.fillRect(posX, posY, size, size);
        }

        // Si la celda ha sido descubierta
        if(show) {
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
