import java.awt.*;

public class Balon {
    private int x, y;
    private final int radio = 15;
    private final int xInicial, yInicial;

    public Balon(int x, int y) {
        this.x = x;
        this.y = y;
        this.xInicial = x;
        this.yInicial = y;
    }

    public void setPosicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void reset() {
        this.x = xInicial;
        this.y = yInicial;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void dibujar(Graphics g) {
        g.setColor(new Color(255, 255, 200));
        g.fillOval(x - radio, y - radio, radio * 2, radio * 2);
        g.setColor(Color.BLACK);
        g.drawOval(x - radio, y - radio, radio * 2, radio * 2);

        int smallRad = radio / 2;
        Polygon pentagon = new Polygon();
        for (int i = 0; i < 5; i++) {
            pentagon.addPoint(
                    (int)(x + smallRad * Math.cos(i * 2 * Math.PI / 5)),
                    (int)(y + smallRad * Math.sin(i * 2 * Math.PI / 5))
            );
        }
        g.drawPolygon(pentagon);
    }
}