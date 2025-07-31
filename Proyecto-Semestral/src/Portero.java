import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Portero {
    private int x, y;
    private final int ancho = 60, alto = 90;
    private Timer timerMovimiento;
    private final int xInicial, yInicial;
    private boolean regresandoAlCentro = false;
    private final JPanel parentPanel; // Reference to the panel that contains the portero

    public Portero(int x, int y, JPanel parentPanel) {
        this.x = x;
        this.y = y;
        this.xInicial = x;
        this.yInicial = y;
        this.parentPanel = parentPanel;
    }

    public void moverA(int nuevoX, int nuevoY) {
        detenerAnimacion();
        this.regresandoAlCentro = false;

        final int inicioX = x;
        final int inicioY = y;
        final int totalPasos = 15;
        int[] pasos = {0};

        timerMovimiento = new Timer(20, e -> {
            if (pasos[0] >= totalPasos) {
                detenerAnimacion();
                x = nuevoX;
                y = nuevoY;
                parentPanel.repaint(); // Final repaint after animation ends
            } else {
                float ratio = (float)pasos[0]/totalPasos;
                x = (int)(inicioX + (nuevoX - inicioX) * ratio);
                y = (int)(inicioY + (nuevoY - inicioY) * ratio);
                pasos[0]++;
                parentPanel.repaint(); // Request repaint on each step
            }
        });
        timerMovimiento.start();
    }

    public void regresarAlCentro() {
        detenerAnimacion();
        this.regresandoAlCentro = true;

        final int inicioX = x;
        final int inicioY = y;
        final int totalPasos = 15;
        int[] pasos = {0};

        timerMovimiento = new Timer(20, e -> {
            if (pasos[0] >= totalPasos) {
                detenerAnimacion();
                x = xInicial;
                y = yInicial;
                regresandoAlCentro = false;
                parentPanel.repaint(); // Final repaint after animation ends
            } else {
                float ratio = (float)pasos[0]/totalPasos;
                x = (int)(inicioX + (xInicial - inicioX) * ratio);
                y = (int)(inicioY + (yInicial - inicioY) * ratio);
                pasos[0]++;
                parentPanel.repaint(); // Request repaint on each step
            }
        });
        timerMovimiento.start();
    }

    public void detenerAnimacion() {
        if (timerMovimiento != null) {
            timerMovimiento.stop();
            timerMovimiento = null;
        }
    }

    public boolean estaAnimando() {
        return timerMovimiento != null && timerMovimiento.isRunning();
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getAncho() { return ancho; }
    public int getAlto() { return alto; }

    public void dibujar(Graphics g) {
        // Body (jersey)
        g.setColor(new Color(0, 0, 200)); // Blue jersey
        g.fillRect(x, y + alto/4, ancho, alto * 3/4); // Adjusted y to make room for head

        // Head
        g.setColor(new Color(255, 205, 150)); // Skin color
        g.fillOval(x + ancho/4, y, ancho/2, alto/3); // Oval head

        // Eyes
        g.setColor(Color.BLACK);
        g.fillOval(x + ancho/4 + 5, y + alto/8, 5, 5); // Left eye
        g.fillOval(x + ancho - ancho/4 - 10, y + alto/8, 5, 5); // Right eye

        // Arms (simplified)
        g.setColor(new Color(0, 0, 200)); // Same color as jersey
        g.fillRect(x - 15, y + alto/4 + 5, 15, alto/2); // Left arm
        g.fillRect(x + ancho, y + alto/4 + 5, 15, alto/2); // Right arm

        // Gloves
        g.setColor(Color.RED);
        g.fillOval(x - 25, y + alto/4, 25, 20); // Left glove
        g.fillOval(x + ancho, y + alto/4, 25, 20); // Right glove

        // Legs (simplified)
        g.setColor(new Color(150, 150, 150)); // Shorts/pants color
        g.fillRect(x + 5, y + alto - 20, ancho/2 - 5, 20); // Left leg
        g.fillRect(x + ancho/2, y + alto - 20, ancho/2 - 5, 20); // Right leg

        // Shoes
        g.setColor(Color.BLACK);
        g.fillRect(x + 5, y + alto - 10, ancho/2 - 5, 10); // Left shoe
        g.fillRect(x + ancho/2, y + alto - 10, ancho/2 - 5, 10); // Right shoe
    }
}