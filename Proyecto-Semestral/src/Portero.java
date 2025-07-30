import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Portero {
    private int x, y;
    private final int ancho = 60, alto = 90;
    private Timer timerMovimiento;
    private final int xInicial, yInicial;
    private boolean regresandoAlCentro = false;

    public Portero(int x, int y) {
        this.x = x;
        this.y = y;
        this.xInicial = x;
        this.yInicial = y;
    }

    public void moverA(int nuevoX, int nuevoY) {
        if (timerMovimiento != null) timerMovimiento.stop();

        this.regresandoAlCentro = false;
        final int inicioX = x;
        final int inicioY = y;
        final int totalPasos = 15;
        int[] pasos = {0};

        timerMovimiento = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pasos[0] >= totalPasos) {
                    ((Timer)e.getSource()).stop();
                    x = nuevoX;
                    y = nuevoY;
                } else {
                    float ratio = (float)pasos[0]/totalPasos;
                    x = (int)(inicioX + (nuevoX - inicioX) * ratio);
                    y = (int)(inicioY + (nuevoY - inicioY) * ratio);
                    pasos[0]++;
                }
            }
        });
        timerMovimiento.start();
    }

    public void regresarAlCentro() {
        if (timerMovimiento != null) timerMovimiento.stop();

        this.regresandoAlCentro = true;
        final int inicioX = x;
        final int inicioY = y;
        final int totalPasos = 15;
        int[] pasos = {0};

        timerMovimiento = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pasos[0] >= totalPasos) {
                    ((Timer)e.getSource()).stop();
                    x = xInicial;
                    y = yInicial;
                    regresandoAlCentro = false;
                } else {
                    float ratio = (float)pasos[0]/totalPasos;
                    x = (int)(inicioX + (xInicial - inicioX) * ratio);
                    y = (int)(inicioY + (yInicial - inicioY) * ratio);
                    pasos[0]++;
                }
            }
        });
        timerMovimiento.start();
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getAncho() { return ancho; }
    public int getAlto() { return alto; }

    public void dibujar(Graphics g) {
        // Cuerpo
        g.setColor(new Color(0, 0, 200));
        g.fillRect(x, y + alto/3, ancho, alto*2/3);

        // Pantalón
        g.setColor(new Color(150, 150, 150));
        g.fillRect(x, y + alto/2, ancho, alto/2);

        // Cara
        g.setColor(new Color(255, 205, 150));
        g.fillOval(x + 10, y + 10, ancho - 20, alto/3);

        // Ojos
        g.setColor(Color.BLACK);
        g.fillOval(x + 20, y + 20, 8, 8);
        g.fillOval(x + ancho - 28, y + 20, 8, 8);

        // Brazos
        g.setColor(new Color(0, 0, 200));
        g.fillRect(x - 15, y + 20, 15, 20);
        g.fillRect(x + ancho, y + 20, 15, 20);

        // Guantes
        g.setColor(Color.RED);
        g.fillOval(x - 20, y + 15, 20, 15);
        g.fillOval(x + ancho, y + 15, 20, 15);

        // Medias y zapatos
        g.setColor(Color.WHITE);
        g.fillRect(x + 10, y + alto - 15, 15, 15);
        g.fillRect(x + ancho - 25, y + alto - 15, 15, 15);

        g.setColor(Color.BLACK);
        g.fillRect(x + 10, y + alto - 5, 15, 5);
        g.fillRect(x + ancho - 25, y + alto - 5, 15, 5);
    }
}