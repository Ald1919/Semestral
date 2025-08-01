package Semestral;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.net.URL;

public class Penalty {
    private static Image logoUniversidad;
    private static Image logoFacultad;
    private static Sonido sonido;

    public static void main(String[] args) {
        sonido = new Sonido();
        sonido.iniciarMusicaFondo();
        loadLogos();
        SwingUtilities.invokeLater(() -> showPresentationScreen());
    }

    public static Sonido getSonido() {
        return sonido;
    }

    private static void loadLogos() {
        try {
            URL utpLogoURL = Penalty.class.getClassLoader().getResource("utp_logo.png");
            URL fiscLogoURL = Penalty.class.getClassLoader().getResource("fisc_logo.png");

            if (utpLogoURL != null) {
                logoUniversidad = new ImageIcon(utpLogoURL).getImage();
            } else {
                System.err.println("No se pudo encontrar 'utp_logo.png' en el classpath.");
            }

            if (fiscLogoURL != null) {
                logoFacultad = new ImageIcon(fiscLogoURL).getImage();
            } else {
                System.err.println("No se pudo encontrar 'fisc_logo.png' en el classpath.");
            }

            if (utpLogoURL == null || fiscLogoURL == null) {
                System.err.println("Advertencia: Una o ambas imágenes de logos no se cargaron. Verifica la carpeta 'resources'.");
            }

        } catch (Exception e) {
            System.err.println("Error inesperado al cargar imágenes: " + e.getMessage());
        }
    }

    private static void showPresentationScreen() {
        JFrame presentationFrame = new JFrame("Presentación - Juego de Penales");
        presentationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        presentationFrame.setSize(screenSize.width, screenSize.height);
        presentationFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        presentationFrame.setUndecorated(true);

        presentationFrame.setLocationRelativeTo(null);

        JPanel presentationPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                GradientPaint gradient = new GradientPaint(0, 0, new Color(0, 80, 0), 0, getHeight(), Color.BLACK);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setColor(Color.WHITE);

                int topMargin = 50;
                int sideMargin = 50;
                int logoBaseWidth = 200;

                if (logoUniversidad != null) {
                    int currentLogoWidth = logoUniversidad.getWidth(this);
                    int currentLogoHeight = logoUniversidad.getHeight(this);
                    if (currentLogoWidth > 0 && currentLogoHeight > 0) {
                        int scaledHeight = (int) ((double) logoBaseWidth / currentLogoWidth * currentLogoHeight);
                        g2d.drawImage(logoUniversidad, sideMargin, topMargin, logoBaseWidth, scaledHeight, this);
                    }
                }
                if (logoFacultad != null) {
                    int currentLogoWidth = logoFacultad.getWidth(this);
                    int currentLogoHeight = logoFacultad.getHeight(this);
                    if (currentLogoWidth > 0 && currentLogoHeight > 0) {
                        int scaledHeight = (int) ((double) logoBaseWidth / currentLogoWidth * currentLogoHeight);
                        g2d.drawImage(logoFacultad, getWidth() - logoBaseWidth - sideMargin, topMargin, logoBaseWidth, scaledHeight, this);
                    }
                }

                g2d.setFont(new Font("Arial", Font.BOLD, 48));
                String universidad = "UNIVERSIDAD TECNOLÓGICA DE PANAMÁ";
                int uniWidth = g2d.getFontMetrics().stringWidth(universidad);
                g2d.drawString(universidad, (getWidth() - uniWidth) / 2, 250);

                g2d.setFont(new Font("Arial", Font.BOLD, 36));
                String facultad = "FACULTAD DE INGENIERÍA DE SISTEMAS COMPUTACIONALES";
                int facWidth = g2d.getFontMetrics().stringWidth(facultad);
                g2d.drawString(facultad, (getWidth() - facWidth) / 2, 310);

                g2d.setFont(new Font("Arial", Font.BOLD, 28));
                int textStartX = getWidth() / 8;
                g2d.drawString("Carrera: LICENCIATURA EN INGENIERÍA DE SOFTWARE", textStartX, 400);
                g2d.drawString("Proyecto: Juego de Penales", textStartX, 440);
                g2d.drawString("Curso: PROGRAMACIÓN DE SOFTWARE I", textStartX, 480);

                g2d.drawString("Integrantes:", textStartX, 540);
                g2d.setFont(new Font("Arial", Font.PLAIN, 24));
                g2d.drawString("1. FELIX CHUNG - 8-1035-1686", textStartX + 20, 575);
                g2d.drawString("2. ALEXIS HE - 8-1032-619", textStartX + 20, 610);
                g2d.drawString("3. LIYI HE - 8-1010-1496", textStartX + 20, 645);
                g2d.drawString("4. ALDAHIR VERGARA - 8-1026-2487", textStartX + 20, 680);

                g2d.setFont(new Font("Arial", Font.BOLD, 28));
                g2d.drawString("Profesor: RODRIGO YÁNGÜEZ", textStartX, getHeight() - 100);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                g2d.drawString("Fecha: " + dateFormat.format(new Date()), textStartX, getHeight() - 60);
            }
        };

        JButton startButton = new JButton("Iniciar Juego");
        startButton.setFont(new Font("Arial", Font.BOLD, 30));
        startButton.setBackground(new Color(0, 150, 0));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        startButton.addActionListener(e -> {
            sonido.reproducirClick();
            presentationFrame.dispose();
            startGame();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        buttonPanel.add(startButton);
        presentationPanel.add(buttonPanel, BorderLayout.SOUTH);

        presentationFrame.add(presentationPanel);
        presentationFrame.setVisible(true);
    }

    private static void startGame() {
        JFrame gameFrame = new JFrame("⚽ Juego de Penales ⚽");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        gameFrame.setSize(screenSize.width, screenSize.height);
        gameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        gameFrame.setUndecorated(true);

        gameFrame.setLocationRelativeTo(null);
        gameFrame.setContentPane(new MenuPanel(gameFrame));
        gameFrame.setVisible(true);
    }
}
