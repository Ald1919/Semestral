package JuegoPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.Random;

public class JuegoPanel extends JPanel implements MouseListener, ActionListener, KeyListener {
    // Imágenes
    private Image fondo, porteroImg, balonImg;

    // Estado del juego
    private String resultado = "Selecciona dificultad";
    private int arqueroZona = -1, jugadorZona = -1;
    private int goles = 0, atajadas = 0, penalesRestantes = 5;
    private int dificultad = 0; // 0:Fácil, 1:Medio, 2:Difícil
    private boolean juegoIniciado = false;

    // Dimensiones
    private final int zonasCols = 3, zonasRows = 3;
    private final int zonaWidth = 100, zonaHeight = 100;
    private final int arcoX = 200, arcoY = 100;

    // Animación
    private double balonX = 400, balonY = 400;
    private double balonDestinoX, balonDestinoY;
    private boolean animandoBalon = false;
    private Timer timer;

    // Constructor
    public JuegoPanel() {
        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);
        cargarImagenes();
        timer = new Timer(20, this);
        crearMenuDificultad();
    }

    private void cargarImagenes() {
        fondo = loadImage("/cancha.png");
        porteroImg = loadImage("/portero.png");
        balonImg = loadImage("/balon.png");
    }

    private Image loadImage(String path) {
        URL url = getClass().getResource(path);
        return url != null ? new ImageIcon(url).getImage() : null;
    }

    // Menú de dificultad
    private void crearMenuDificultad() {
        String[] opciones = {"Fácil (30% atajada)", "Medio (50% atajada)", "Difícil (70% atajada)"};
        int seleccion = JOptionPane.showOptionDialog(
                this,
                "Selecciona la dificultad:",
                "Juego de Penales",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccion != -1) {
            dificultad = seleccion;
            resultado = "Haz clic para patear (Penales: " + penalesRestantes + ")";
            juegoIniciado = true;
            penalesRestantes = 5;
            goles = 0;
            atajadas = 0;
            repaint();
        }
    }

    // Reinicio con ESC
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            crearMenuDificultad();
        }
    }

    // Dibujado
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Fondo
        if (fondo != null) {
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(new Color(34, 139, 34));
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // Zonas del arco
        g.setColor(Color.WHITE);
        for (int fila = 0; fila < zonasRows; fila++) {
            for (int col = 0; col < zonasCols; col++) {
                g.drawRect(
                        arcoX + col * zonaWidth,
                        arcoY + fila * zonaHeight,
                        zonaWidth,
                        zonaHeight
                );
            }
        }

        // Portero
        if (arqueroZona != -1) {
            int fila = arqueroZona / zonasCols;
            int col = arqueroZona % zonasCols;
            int x = arcoX + col * zonaWidth + zonaWidth / 2 - 25;
            int y = arcoY + fila * zonaHeight + zonaHeight / 2 - 25;
            if (porteroImg != null) {
                g.drawImage(porteroImg, x, y, 50, 50, this);
            } else {
                g.setColor(Color.RED);
                g.fillOval(x, y, 50, 50);
            }
        }

        // Balón
        if (balonImg != null) {
            g.drawImage(balonImg, (int) balonX - 25, (int) balonY - 25, 50, 50, this);
        } else {
            g.setColor(Color.WHITE);
            g.fillOval((int) balonX - 15, (int) balonY - 15, 30, 30);
        }

        // Textos
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(resultado, 50, 30);
        g.drawString("Goles: " + goles, 50, 60);
        g.drawString("Atajadas: " + atajadas, 50, 90);
        g.drawString("Penales: " + penalesRestantes, 50, 120);
    }

    // Lógica del juego
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!juegoIniciado || animandoBalon || penalesRestantes <= 0) return;

        for (int fila = 0; fila < zonasRows; fila++) {
            for (int col = 0; col < zonasCols; col++) {
                int zonaX = arcoX + col * zonaWidth;
                int zonaY = arcoY + fila * zonaHeight;
                if (e.getX() >= zonaX && e.getX() <= zonaX + zonaWidth &&
                        e.getY() >= zonaY && e.getY() <= zonaY + zonaHeight) {

                    jugadorZona = fila * zonasCols + col;
                    arqueroZona = calcularZonaArquero();
                    penalesRestantes--;

                    balonDestinoX = zonaX + zonaWidth / 2;
                    balonDestinoY = zonaY + zonaHeight / 2;
                    animandoBalon = true;
                    timer.start();
                    resultado = "Pateando...";
                    repaint();
                    return;
                }
            }
        }
    }

    private int calcularZonaArquero() {
        Random rand = new Random();
        double probabilidad = switch (dificultad) {
            case 0 -> 0.3; // 30% de atajar
            case 1 -> 0.5; // 50%
            case 2 -> 0.7; // 70%
            default -> 0.3;
        };
        return rand.nextDouble() < probabilidad ? jugadorZona : rand.nextInt(9);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        double dx = balonDestinoX - balonX;
        double dy = balonDestinoY - balonY;
        double distancia = Math.sqrt(dx * dx + dy * dy);

        if (distancia < 10) {
            balonX = balonDestinoX;
            balonY = balonDestinoY;
            animandoBalon = false;
            timer.stop();

            if (jugadorZona == arqueroZona) {
                resultado = "¡Atajada! (Penales: " + penalesRestantes + ")";
                atajadas++;
            } else {
                resultado = "¡Gol! (Penales: " + penalesRestantes + ")";
                goles++;
            }

            if (penalesRestantes <= 0) {
                resultado = "Fin. Goles: " + goles + " | Atajadas: " + atajadas;
            }
            repaint();
        } else {
            balonX += dx / distancia * 10;
            balonY += dy / distancia * 10;
            repaint();
        }
    }

    // Métodos no utilizados (requeridos por interfaces)
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}