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

    public void actualizarPosicion(Point inicio, Point destino, float progreso) {
        this.x = (int)(inicio.x + (destino.x - inicio.x) * progreso);
        this.y = (int)(inicio.y + (destino.y - inicio.y) * progreso);
    }

    public void reset() {
        this.x = xInicial;
        this.y = yInicial;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void dibujar(Graphics g) {
        // Balón
        g.setColor(new Color(255, 255, 200));
        g.fillOval(x - radio, y - radio, radio * 2, radio * 2);
        g.setColor(Color.BLACK);
        g.drawOval(x - radio, y - radio, radio * 2, radio * 2);

        // Diseño del balón 
        g.setColor(Color.BLACK);
        int[] xPoints = new int[5];
        int[] yPoints = new int[5];
        double angleOffset = Math.PI / 10; // To make one point face up
        for (int i = 0; i < 5; i++) {
            xPoints[i] = (int)(x + radio * 0.7 * Math.cos(i * 2 * Math.PI / 5 + angleOffset));
            yPoints[i] = (int)(y + radio * 0.7 * Math.sin(i * 2 * Math.PI / 5 + angleOffset));
        }
        g.fillPolygon(xPoints, yPoints, 5); // Filled pentagon
    }
}
