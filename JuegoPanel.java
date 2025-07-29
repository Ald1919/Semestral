import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class JuegoPanel extends JPanel {
    private final JFrame frame;
    private final double dificultad;
    private final Balon balon;
    private final Portero portero;
    private final Marcador marcador;
    private final Rectangle[] zonas = new Rectangle[9];
    private String mensaje = "Haz clic en un área para patear";
    private boolean animando = false;
    private Timer timer;

    public JuegoPanel(JFrame frame, double dificultad) {
        this.frame = frame;
        this.dificultad = dificultad;
        setLayout(null);
        setBackground(new Color(0, 100, 0));

        definirZonas();

        balon = new Balon(350, 450);
        portero = new Portero(
                zonas[4].x + zonas[4].width/2 - 30,
                zonas[4].y + zonas[4].height/2 - 30
        );

        marcador = new Marcador();

        JButton volver = new JButton("Volver al Menú");
        volver.setBounds(20, 20, 150, 30);
        volver.setFont(new Font("Arial", Font.BOLD, 12));
        volver.addActionListener(e -> {
            if (timer != null) timer.stop();
            frame.setContentPane(new MenuPanel(frame));
            frame.revalidate();
        });
        add(volver);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (animando) return;

                Point click = e.getPoint();
                for (int i = 0; i < zonas.length; i++) {
                    if (zonas[i].contains(click)) {
                        animarDisparo(i);
                        break;
                    }
                }
            }
        });
    }

    private void definirZonas() {
        int baseX = 150;
        int baseY = 80;
        int ancho = 166;
        int alto = 83;

        for (int i = 0; i < 9; i++) {
            int fila = i / 3;
            int columna = i % 3;
            zonas[i] = new Rectangle(baseX + columna * ancho, baseY + fila * alto, ancho, alto);
        }
    }

    private void animarDisparo(int zonaIndex) {
        animando = true;
        Rectangle zonaDisparo = zonas[zonaIndex];
        Point destino = new Point(zonaDisparo.x + zonaDisparo.width/2, zonaDisparo.y + zonaDisparo.height/2);

        final Point posicionInicial = new Point(balon.getX(), balon.getY());

        timer = new Timer(10, new ActionListener() {
            int steps = 0;
            final int totalSteps = 30;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (steps >= totalSteps) {
                    ((Timer)e.getSource()).stop();
                    verificarResultado(zonaDisparo, zonaIndex);

                    Timer returnTimer = new Timer(10, new ActionListener() {
                        int returnSteps = 0;

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (returnSteps >= totalSteps) {
                                ((Timer)e.getSource()).stop();
                                resetearBalon();
                            } else {
                                float ratio = (float)returnSteps/totalSteps;
                                int currentX = (int)(destino.x - (destino.x - posicionInicial.x) * ratio);
                                int currentY = (int)(destino.y - (destino.y - posicionInicial.y) * ratio);
                                balon.setPosicion(currentX, currentY);
                                returnSteps++;
                            }
                            repaint();
                        }
                    });
                    returnTimer.start();
                } else {
                    float ratio = (float)steps/totalSteps;
                    int currentX = (int)(posicionInicial.x + (destino.x - posicionInicial.x) * ratio);
                    int currentY = (int)(posicionInicial.y + (destino.y - posicionInicial.y) * ratio);
                    balon.setPosicion(currentX, currentY);
                    steps++;
                }
                repaint();
            }
        });
        timer.start();
    }

    private void verificarResultado(Rectangle zonaDisparo, int zonaIndex) {
        Random rand = new Random();
        boolean atajada = false;

        // El portero decide si intenta atajar
        if (rand.nextDouble() < dificultad) {
            // Elige una zona al azar (con mayor probabilidad de acertar en dificultad alta)
            int zonaPortero;
            if (dificultad == 0.95) zonaPortero = rand.nextDouble() < 0.8 ? zonaIndex : rand.nextInt(9);
            else if (dificultad == 0.5) zonaPortero = rand.nextDouble() < 0.5 ? zonaIndex : rand.nextInt(9);
            else zonaPortero = rand.nextDouble() < 0.2 ? zonaIndex : rand.nextInt(9);

            Rectangle zonaAtajar = zonas[zonaPortero];
            portero.moverA(
                    zonaAtajar.x + zonaAtajar.width/2 - portero.getAncho()/2,
                    zonaAtajar.y + zonaAtajar.height/2 - portero.getAlto()/3,
                    zonaAtajar
            );

            // Ataja solo si fue a la zona correcta
            atajada = (zonaPortero == zonaIndex);
        } else {
            // El portero falla y va a una zona aleatoria
            int zonaAleatoria = rand.nextInt(9);
            portero.moverA(
                    zonas[zonaAleatoria].x + zonas[zonaAleatoria].width/2 - portero.getAncho()/2,
                    zonas[zonaAleatoria].y + zonas[zonaAleatoria].height/2 - portero.getAlto()/3,
                    zonas[zonaAleatoria]
            );
            atajada = false;
        }

        if (atajada) {
            marcador.incrementarAtajadas();
            mensaje = "¡Atajada del portero!";
        } else {
            marcador.incrementarGoles();
            mensaje = "¡GOOOOOOL!";
        }
    }

    private void resetearBalon() {
        balon.reset();
        animando = false;
        mensaje = "Haz clic en un área para patear";
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar cancha
        g.setColor(new Color(0, 100, 0));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(new Color(0, 120, 0));
        for (int i = 0; i < getWidth(); i += 20) {
            for (int j = 0; j < getHeight(); j += 20) {
                g.drawLine(i, j, i, j);
            }
        }

        // Dibujar áreas de tiro
        g.setColor(new Color(255, 255, 255, 50));
        for (Rectangle zona : zonas) {
            g.fillRect(zona.x, zona.y, zona.width, zona.height);
            g.setColor(Color.WHITE);
            g.drawRect(zona.x, zona.y, zona.width, zona.height);
            g.setColor(new Color(255, 255, 255, 50));
        }

        // Dibujar arco
        g.setColor(Color.WHITE);
        g.drawRect(150, 80, 500, 250);

        // Dibujar red
        g.setColor(new Color(200, 200, 200, 150));
        for (int i = 150; i <= 650; i += 15) {
            g.drawLine(i, 80, i, 330);
        }
        for (int j = 80; j <= 330; j += 15) {
            g.drawLine(150, j, 650, j);
        }

        // Dibujar elementos
        portero.dibujar(g);
        balon.dibujar(g);

        // Dibujar marcador
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        int marcadorX = 600;
        g.drawString("Goles: " + marcador.getGoles(), marcadorX, 30);
        g.drawString("Atajadas: " + marcador.getAtajadas(), marcadorX, 60);

        String dificultadTexto;
        if (dificultad <= 0.3) dificultadTexto = "Fácil (20%)";
        else if (dificultad <= 0.6) dificultadTexto = "Medio (50%)";
        else dificultadTexto = "Difícil (95%)";

        g.drawString("Dificultad: " + dificultadTexto, marcadorX, 90);

        // Mensaje
        Color msgColor = mensaje.contains("GOOOOOOL") ? Color.GREEN :
                mensaje.contains("Atajada") ? Color.RED : Color.WHITE;
        g.setColor(msgColor);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        int msgWidth = g.getFontMetrics().stringWidth(mensaje);
        g.drawString(mensaje, (getWidth() - msgWidth) / 2, 400);
    }
}