package Semestral;

import java.awt.*;
import java.awt.geom.Point2D;

public class Balon {
    private int x, y, diametro = 50;
    private int originalX, originalY;

    public Balon(int x, int y) {
        this.x = x;
        this.y = y;
        this.originalX = x;
        this.originalY = y;
    }

    public void actualizarPosicion(Point inicio, Point destino, float progreso) {
        double newX = inicio.x + (destino.x - inicio.x) * progreso;
        double newY = inicio.y + (destino.y - inicio.y) * progreso;

        double controlY = inicio.y - (destino.y - inicio.y) * 2;
        double newYArc = (1 - progreso) * (1 - progreso) * inicio.y + 2 * (1 - progreso) * progreso * controlY + progreso * progreso * destino.y;

        this.x = (int) newX;
        this.y = (int) newYArc;
    }

    public void dibujar(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, diametro, diametro);
        g.setColor(Color.BLACK);
        g.drawOval(x, y, diametro, diametro);
    }

    public void reset() {
        this.x = originalX;
        this.y = originalY;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}