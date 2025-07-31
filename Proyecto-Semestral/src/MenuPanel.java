import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage; 

public class MenuPanel extends JPanel {
    private final JFrame frame;
    private final Image backgroundImage; 

    public MenuPanel(JFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout()); 

        // --- Configuración de componentes y layout para pantalla completa ---
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; // Rellenar espacio disponible
        gbc.insets = new Insets(20, 20, 20, 20); // Márgenes externos para todos los componentes

        // 1. Título del juego
        JLabel titleLabel = new JLabel("JUEGO DE PENALES", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 80)); // Fuente más grande
        titleLabel.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3; // Ocupa las 3 columnas para el título
        gbc.weighty = 0.2; // Da más espacio vertical al título
        add(titleLabel, gbc);

        // 2. Panel de botones (para agruparlos)
        JPanel buttonContainer = new JPanel(new GridBagLayout());
        buttonContainer.setOpaque(false);
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.gridwidth = GridBagConstraints.REMAINDER; // Cada botón en su propia fila
        buttonGbc.fill = GridBagConstraints.HORIZONTAL;
        buttonGbc.insets = new Insets(20, 150, 20, 150); // Márgenes más grandes para los botones

        Dimension buttonSize = new Dimension(500, 100); // Tamaño preferido para los botones

        JButton easyButton = createDifficultyButton("Fácil", new Color(100, 255, 100), 0.2);
        easyButton.setPreferredSize(buttonSize);
        buttonContainer.add(easyButton, buttonGbc);

        JButton mediumButton = createDifficultyButton("Medio", new Color(255, 255, 100), 0.5);
        mediumButton.setPreferredSize(buttonSize);
        buttonContainer.add(mediumButton, buttonGbc);

        JButton hardButton = createDifficultyButton("Difícil", new Color(255, 100, 100), 0.8);
        hardButton.setPreferredSize(buttonSize);
        buttonContainer.add(hardButton, buttonGbc);

        JButton exitButton = createStyledButton("Salir", new Color(200, 200, 200));
        exitButton.setPreferredSize(buttonSize);
        exitButton.addActionListener(e -> System.exit(0));
        buttonContainer.add(exitButton, buttonGbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3; // Ocupa las 3 columnas
        gbc.weighty = 0.6; // Da la mayor parte del espacio vertical a los botones
        add(buttonContainer, gbc);

        // El fondo se crea y dibuja en paintComponent
        backgroundImage = createBackgroundImage();
    }

    private JButton createDifficultyButton(String text, Color color, double difficulty) {
        JButton button = createStyledButton(text, color);
        button.addActionListener(e -> {
            frame.setContentPane(new JuegoPanel(frame, difficulty));
            frame.revalidate();
            frame.repaint();
        });
        return button;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 36)); // Fuente de botón más grande
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 4), // Borde más grueso
                BorderFactory.createEmptyBorder(15, 40, 15, 40) // Más padding interno
        ));
        return button;
    }

    private Image createBackgroundImage() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        BufferedImage image = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        GradientPaint gradient = new GradientPaint(0, 0, new Color(0, 80, 0), 0, screenSize.height, Color.BLACK);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, screenSize.width, screenSize.height);

        g2d.setColor(new Color(0, 100, 0));
        for (int x = 0; x < screenSize.width; x += 30) {
            for (int y = 0; y < screenSize.height; y += 30) {
                g2d.fillRect(x, y, 2, 2);
            }
        }
        g2d.dispose();
        return image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        // No hay lógica para dibujar logos aquí, ya que están en Penalty
    }
}
