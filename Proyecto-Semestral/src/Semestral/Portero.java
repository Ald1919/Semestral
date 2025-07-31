package Semestral;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Portero {
    private int x, y;
    private final int ancho = 80;
    private final int alto = 80;
    private final int velocidad = 10;
    private int destinoX, destinoY;
    private boolean animando = false;
    private final Timer timer;
    private final JComponent componenteParaRepintar;
    private final int originalX;
    private final int originalY;

    public Portero(int x, int y, JComponent componente) {
        this.x = x;
        this.y = y;
        this.originalX = x;
        this.originalY = y;
        this.componenteParaRepintar = componente;

        timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moverPaso();
            }
        });
    }

    public void moverA(int nuevoX, int nuevoY) {
        this.destinoX = nuevoX;
        this.destinoY = nuevoY;
        this.animando = true;
        timer.start();
    }

    private void moverPaso() {
        if (x == destinoX && y == destinoY) {
            animando = false;
            timer.stop();
            return;
        }

        if (x < destinoX) x = Math.min(x + velocidad, destinoX);
        else if (x > destinoX) x = Math.max(x - velocidad, destinoX);

        if (y < destinoY) y = Math.min(y + velocidad, destinoY);
        else if (y > destinoY) y = Math.max(y - velocidad, destinoY);

        componenteParaRepintar.repaint();
    }

    public void regresarAlCentro() {
        moverA(originalX, originalY);
    }

    public void detenerAnimacion() {
        timer.stop();
        animando = false;
    }

    public boolean estaAnimando() {
        return animando;
    }

    public void dibujar(Graphics g) {
        g.setColor(new Color(255, 165, 0));
        g.fillRect(x, y, ancho, alto);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getAncho() { return ancho; }
    public int getAlto() { return alto; }
}