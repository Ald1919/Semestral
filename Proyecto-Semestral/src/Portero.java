import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Portero {
    private int x, y;
    private final int ancho = 60, alto = 90;
    private Timer timerMovimiento;
    private final int xInicial, yInicial;
    private Rectangle zonaActual;
    private int pasosAnimacion;

    public Portero(int x, int y) {
        this.x = x;
        this.y = y;
        this.xInicial = x;
        this.yInicial = y;
        this.zonaActual = null;
        this.pasosAnimacion = 0;
    }

    public void moverA(int nuevoX, int nuevoY, Rectangle zonaDestino) {
        if (timerMovimiento != null) {
            timerMovimiento.stop();
        }

        this.zonaActual = zonaDestino;
        final int inicioX = x;
        final int inicioY = y;
        final int totalPasos = 15;
        this.pasosAnimacion = 0;

        timerMovimiento = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pasosAnimacion >= totalPasos) {
                    ((Timer)e.getSource()).stop();
                    x = nuevoX;
                    y = nuevoY;

                    // Regresar al centro después de 1 segundo
                    new Timer(1000, ev -> regresarAlCentro()).start();
                } else {
                    float ratio = (float)pasosAnimacion/totalPasos;
                    x = (int)(inicioX + (nuevoX - inicioX) * ratio);
                    y = (int)(inicioY + (nuevoY - inicioY) * ratio);
                    pasosAnimacion++;
                }
            }
        });
        timerMovimiento.start();
    }

    public void regresarAlCentro() {
        if (timerMovimiento != null) {
            timerMovimiento.stop();
        }

        final int inicioX = x;
        final int inicioY = y;
        final int totalPasos = 15;
        this.pasosAnimacion = 0;

        timerMovimiento = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pasosAnimacion >= totalPasos) {
                    ((Timer)e.getSource()).stop();
                    x = xInicial;
                    y = yInicial;
                    zonaActual = null;
                } else {
                    float ratio = (float)pasosAnimacion/totalPasos;
                    x = (int)(inicioX + (xInicial - inicioX) * ratio);
                    y = (int)(inicioY + (yInicial - inicioY) * ratio);
                    pasosAnimacion++;
                }
            }
        });
        timerMovimiento.start();
    }

    public boolean estaEnZona(Rectangle zona) {
        return zonaActual != null && zonaActual.equals(zona);
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