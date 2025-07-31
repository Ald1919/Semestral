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
    private boolean animacionActiva = false;
    private Timer timerAnimacionBalon;
    private Timer timerMensaje;

    // Variables para las dimensiones de la pantalla
    private int screenWidth;
    private int screenHeight;

    // Variables para las dimensiones y posición del arco (portería)
    private int arcoX, arcoY, arcoAncho, arcoAlto;

    public JuegoPanel(JFrame frame, double dificultad) {
        this.frame = frame;
        this.dificultad = validateDifficulty(dificultad);
        setLayout(null);
        setDoubleBuffered(true);

        // Obtener las dimensiones de la pantalla al inicio
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.screenWidth = screenSize.width;
        this.screenHeight = screenSize.height;

        // Definir el tamaño y la posición del arco de forma proporcional
        arcoAncho = (int) (screenWidth * 0.6); // 60% del ancho de la pantalla
        arcoAlto = (int) (screenHeight * 0.4); // 40% del alto de la pantalla
        arcoX = (screenWidth - arcoAncho) / 2; // Centrar horizontalmente
        arcoY = (int) (screenHeight * 0.1); // Empezar un poco más abajo del borde superior

        definirZonas();

        // Posicionar el portero y el balón en relación al arco y la pantalla
        portero = new Portero(
                arcoX + arcoAncho / 2 - 40,
                arcoY + arcoAlto - 80,
                this
        );
        balon = new Balon(screenWidth / 2 - 25, (int) (screenHeight * 0.8));
        marcador = new Marcador();

        configurarInterfaz();
    }

    private double validateDifficulty(double dificultad) {
        if (dificultad < 0 || dificultad > 1) {
            throw new IllegalArgumentException("La dificultad debe estar entre 0 y 1");
        }
        return dificultad;
    }

    private void definirZonas() {
        // Definir las zonas relativas al arco
        int zonaAncho = arcoAncho / 3;
        int zonaAlto = arcoAlto / 3;

        for (int i = 0; i < 9; i++) {
            int fila = i / 3;
            int columna = i % 3;
            zonas[i] = new Rectangle(
                    arcoX + columna * zonaAncho,
                    arcoY + fila * zonaAlto,
                    zonaAncho,
                    zonaAlto
            );
        }
    }

    private void configurarInterfaz() {
        JButton backButton = new JButton("Volver al Menú");
        backButton.setBounds(20, 20, 180, 40);
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.addActionListener(e -> returnToMenu());
        add(backButton);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!animacionActiva && !portero.estaAnimando()) {
                    processShot(e.getPoint());
                }
            }
        });
    }

    private void processShot(Point clickPoint) {
        try {
            boolean zonaEncontrada = false;
            for (int i = 0; i < zonas.length; i++) {
                if (zonas[i].contains(clickPoint)) {
                    iniciarDisparo(i);
                    zonaEncontrada = true;
                    break;
                }
            }
            if (!zonaEncontrada) {
                mensaje = "¡Click fuera del arco!";
                startMessageTimer();
                repaint();
            }
        } catch (Exception ex) {
            showError("Error al procesar disparo: " + ex.getMessage());
        }
    }

    private void iniciarDisparo(int zonaIndex) {
        animacionActiva = true;
        Rectangle zonaDisparo = zonas[zonaIndex];
        Point destinoBalon = new Point(
                zonaDisparo.x + zonaDisparo.width / 2,
                zonaDisparo.y + zonaDisparo.height / 2
        );

        // Animación del portero
        Point destinoPortero = calcularMovimientoPortero(zonaDisparo, zonaIndex);
        portero.moverA(destinoPortero.x, destinoPortero.y);

        // Animación del balón
        timerAnimacionBalon = new Timer(20, new ActionListener() {
            float progreso = 0f;
            final Point inicio = new Point(balon.getX(), balon.getY());

            @Override
            public void actionPerformed(ActionEvent e) {
                if (progreso >= 1.0f) {
                    ((Timer) e.getSource()).stop();
                    finalizarDisparo(zonaDisparo);
                } else {
                    progreso = Math.min(1.0f, progreso + 0.04f);
                    balon.actualizarPosicion(inicio, destinoBalon, progreso);
                    repaint();
                }
            }
        });
        timerAnimacionBalon.start();
    }

    private Point calcularMovimientoPortero(Rectangle zonaDisparo, int zonaIndex) {
        Random rand = new Random();
        boolean intentaAtajar = rand.nextDouble() < dificultad;
        int zonaPortero;

        if (intentaAtajar) {
            if (rand.nextDouble() < getProbabilidadAcertar()) {
                zonaPortero = zonaIndex;
            } else {
                zonaPortero = rand.nextInt(9);
            }
        } else {
            zonaPortero = rand.nextInt(9);
        }

        Rectangle zonaDestino = zonas[zonaPortero];
        return new Point(
                zonaDestino.x + zonaDestino.width / 2 - portero.getAncho() / 2,
                zonaDestino.y + zonaDestino.height / 2 - portero.getAlto() / 3
        );
    }

    private double getProbabilidadAcertar() {
        if (dificultad >= 0.75) return 0.95;
        if (dificultad >= 0.45) return 0.50;
        return 0.20;
    }

    private void finalizarDisparo(Rectangle zonaDisparo) {
        Timer checkPorteroTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!portero.estaAnimando()) {
                    ((Timer) e.getSource()).stop();

                    boolean atajada = porteroEnZona(zonaDisparo);
                    actualizarMarcador(atajada);
                    startMessageTimer();

                    new Timer(1000, event -> {
                        ((Timer) event.getSource()).stop();
                        balon.reset();
                        portero.regresarAlCentro();
                        animacionActiva = false;
                        repaint();
                    }).start();
                }
            }
        });
        checkPorteroTimer.start();
    }

    private boolean porteroEnZona(Rectangle zonaDisparo) {
        Rectangle porteroRect = new Rectangle(
                portero.getX(),
                portero.getY(),
                portero.getAncho(),
                portero.getAlto()
        );

        Rectangle zonaDisparoCrecida = new Rectangle(zonaDisparo);
        zonaDisparoCrecida.grow(10, 10);

        return porteroRect.intersects(zonaDisparoCrecida);
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

    private void startMessageTimer() {
        if (timerMensaje != null && timerMensaje.isRunning()) {
            timerMensaje.stop();
        }
        timerMensaje = new Timer(2000, e -> {
            mensaje = "Haz clic en un área para patear";
            repaint();
            ((Timer) e.getSource()).stop();
        });
        timerMensaje.setRepeats(false);
        timerMensaje.start();
    }

    private void returnToMenu() {
        if (timerAnimacionBalon != null) timerAnimacionBalon.stop();
        if (portero != null) portero.detenerAnimacion();
        if (timerMensaje != null) timerMensaje.stop();
        frame.setContentPane(new MenuPanel(frame));
        frame.revalidate();
        frame.repaint();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        dibujarCancha(g);
        dibujarArco(g);
        dibujarZonas(g);
        dibujarElementos(g);
        dibujarMarcador(g);
        dibujarMensaje(g);
    }

    private void dibujarCancha(Graphics g) {
        g.setColor(new Color(0, 100, 0));
        g.fillRect(0, 0, screenWidth, screenHeight);

        g.setColor(new Color(0, 120, 0));
        for (int x = 0; x < screenWidth; x += 30) {
            for (int y = 0; y < screenHeight; y += 30) {
                g.fillRect(x, y, 2, 2);
            }
        }
    }

    private void dibujarArco(Graphics g) {
        g.setColor(Color.WHITE);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRect(arcoX, arcoY, arcoAncho, arcoAlto);
        g2d.setStroke(new BasicStroke(1));

        int hSpacing = arcoAncho / 15;
        int vSpacing = arcoAlto / 10;

        for (int x = arcoX; x <= arcoX + arcoAncho; x += hSpacing) g.drawLine(x, arcoY, x, arcoY + arcoAlto);
        for (int y = arcoY; y <= arcoY + arcoAlto; y += vSpacing) g.drawLine(arcoX, y, arcoX + arcoAncho, y);
    }

    private void dibujarZonas(Graphics g) {
        g.setColor(new Color(255, 255, 255, 30));
        for (Rectangle zona : zonas) {
            g.fillRect(zona.x, zona.y, zona.width, zona.height);
            g.setColor(Color.WHITE);
            g.drawRect(zona.x, zona.y, zona.width, zona.height);
            g.setColor(new Color(255, 255, 255, 30));
        }
    }

    private void dibujarElementos(Graphics g) {
        portero.dibujar(g);
        balon.dibujar(g);
    }

    private void dibujarMarcador(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));

        int marginX = 50;
        int marginY = 50;
        g.drawString("Goles: " + marcador.getGoles(), screenWidth - 200, marginY);
        g.drawString("Atajadas: " + marcador.getAtajadas(), screenWidth - 200, marginY + 30);

        String dificultadTexto;
        if (dificultad >= 0.75) {
            dificultadTexto = "Dificultad: Difícil";
        } else if (dificultad >= 0.45) {
            dificultadTexto = "Dificultad: Medio";
        } else {
            dificultadTexto = "Dificultad: Fácil";
        }
        g.drawString(dificultadTexto, screenWidth - 200, marginY + 60);
        // --- FIN DEL CAMBIO ---
    }

    private void dibujarMensaje(Graphics g) {
        Color msgColor = mensaje.contains("GOOOOOOL") ? Color.GREEN :
                mensaje.contains("Atajada") ? Color.RED : Color.WHITE;
        g.setColor(msgColor);
        g.setFont(new Font("Arial", Font.BOLD, 40));

        int msgWidth = g.getFontMetrics().stringWidth(mensaje);
        g.drawString(mensaje, (screenWidth - msgWidth) / 2, (int)(screenHeight * 0.75));
    }
}
