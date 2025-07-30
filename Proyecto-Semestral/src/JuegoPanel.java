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
    private Timer timerAnimacion;

    public JuegoPanel(JFrame frame, double dificultad) {
        this.frame = frame;
        this.dificultad = dificultad;
        setLayout(null);
        setBackground(new Color(0, 100, 0));
        setDoubleBuffered(true);

        definirZonas();

        balon = new Balon(350, 450);
        portero = new Portero(
                zonas[4].x + zonas[4].width/2 - 30,
                zonas[4].y + zonas[4].height/2 - 30
        );

        marcador = new Marcador();

        JButton volver = new JButton("Volver al Menú");
        volver.setBounds(20, 20, 150, 30);
        volver.addActionListener(e -> {
            if (timerAnimacion != null) timerAnimacion.stop();
            frame.setContentPane(new MenuPanel(frame));
            frame.revalidate();
        });
        add(volver);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!animando) {
                    Point click = e.getPoint();
                    for (int i = 0; i < zonas.length; i++) {
                        if (zonas[i].contains(click)) {
                            iniciarDisparo(i);
                            break;
                        }
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

    private void iniciarDisparo(int zonaIndex) {
        animando = true;
        Rectangle zonaDisparo = zonas[zonaIndex];
        Point destinoBalon = new Point(
                zonaDisparo.x + zonaDisparo.width/2,
                zonaDisparo.y + zonaDisparo.height/2
        );
        Point inicioBalon = new Point(balon.getX(), balon.getY());

        configurarMovimientoPortero(zonaDisparo, zonaIndex);

        timerAnimacion = new Timer(20, new ActionListener() {
            float progreso = 0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (progreso >= 1.0f) {
                    ((Timer)e.getSource()).stop();

                    boolean atajada = porteroEnZona(zonaDisparo);
                    actualizarMarcador(atajada);

                    // Esperar y resetear
                    new Timer(1000, ev -> {
                        balon.reset();
                        portero.regresarAlCentro();
                        animando = false;
                        mensaje = "Haz clic en un área para patear";
                        repaint();
                    }).start();

                } else {
                    progreso += 0.04f; // Velocidad ligeramente aumentada
                    balon.actualizarPosicion(inicioBalon, destinoBalon, progreso);
                }
                repaint();
            }
        });
        timerAnimacion.start();
    }

    private boolean porteroEnZona(Rectangle zonaDisparo) {
        Rectangle porteroRect = new Rectangle(portero.getX(), portero.getY(),
                portero.getAncho(), portero.getAlto());
        return porteroRect.intersects(zonaDisparo);
    }

    private void configurarMovimientoPortero(Rectangle zonaDisparo, int zonaIndex) {
        Random rand = new Random();
        boolean intentaAtajar = rand.nextDouble() < dificultad;
        int zonaPortero;

        if (intentaAtajar) {
            if (dificultad >= 0.7) zonaPortero = rand.nextDouble() < 0.8 ? zonaIndex : rand.nextInt(9);
            else if (dificultad >= 0.4) zonaPortero = rand.nextDouble() < 0.5 ? zonaIndex : rand.nextInt(9);
            else zonaPortero = rand.nextDouble() < 0.3 ? zonaIndex : rand.nextInt(9);
        } else {
            zonaPortero = rand.nextInt(9);
        }

        Rectangle zonaDestino = zonas[zonaPortero];
        Point destinoPortero = new Point(
                zonaDestino.x + zonaDestino.width/2 - portero.getAncho()/2,
                zonaDestino.y + zonaDestino.height/2 - portero.getAlto()/3
        );

        portero.moverA(destinoPortero.x, destinoPortero.y);
    }

    private void actualizarMarcador(boolean atajada) {
        if (atajada) {
            marcador.incrementarAtajadas();
            mensaje = "¡Atajada del portero!";
        } else {
            marcador.incrementarGoles();
            mensaje = "¡GOOOOOOL!";
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar cancha
        g.setColor(new Color(0, 100, 0));
        g.fillRect(0, 0, getWidth(), getHeight());

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

        // Dibujar elementos
        portero.dibujar(g);
        balon.dibujar(g);

        // Dibujar marcador
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Goles: " + marcador.getGoles(), 600, 30);
        g.drawString("Atajadas: " + marcador.getAtajadas(), 600, 60);

        String dificultadTexto;
        if (dificultad <= 0.3) dificultadTexto = "Fácil (20%)";
        else if (dificultad <= 0.6) dificultadTexto = "Medio (50%)";
        else dificultadTexto = "Difícil (80%)";

        g.drawString("Dificultad: " + dificultadTexto, 600, 90);

        // Mensaje
        Color msgColor = mensaje.contains("GOOOOOOL") ? Color.GREEN :
                mensaje.contains("Atajada") ? Color.RED : Color.WHITE;
        g.setColor(msgColor);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        int msgWidth = g.getFontMetrics().stringWidth(mensaje);
        g.drawString(mensaje, (getWidth() - msgWidth) / 2, 400);
    }
}