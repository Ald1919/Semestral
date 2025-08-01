package Semestral; //nada más es disque la pantalla de seleccion; el menú para la dificultad

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuPanel extends JPanel {  //lo hereda, puede contener otros elementos, por eso lo extiende
    private final JFrame frame;
    private final Image backgroundImage;
    private final Sonido sonido;

    public MenuPanel(JFrame frame) {
        this.frame = frame;
        this.sonido = Penalty.getSonido();
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 20, 20, 20);

        JLabel titleLabel = new JLabel("JUEGO DE PENALES", SwingConstants.CENTER); //titulazo
        titleLabel.setFont(new Font("Arial", Font.BOLD, 80)); //su estilo
        titleLabel.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weighty = 0.2;
        add(titleLabel, gbc);

        JPanel buttonContainer = new JPanel(new GridBagLayout());
        buttonContainer.setOpaque(false);
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.gridwidth = GridBagConstraints.REMAINDER;
        buttonGbc.fill = GridBagConstraints.HORIZONTAL;
        buttonGbc.insets = new Insets(20, 150, 20, 150);

        Dimension buttonSize = new Dimension(500, 100);

        JButton easyButton = createDifficultyButton("Fácil", new Color(100, 255, 100), 0.2); //crea boton facil
        easyButton.setPreferredSize(buttonSize);
        buttonContainer.add(easyButton, buttonGbc);

        JButton mediumButton = createDifficultyButton("Medio", new Color(255, 255, 100), 0.5); //boton medio
        mediumButton.setPreferredSize(buttonSize);
        buttonContainer.add(mediumButton, buttonGbc);

        JButton hardButton = createDifficultyButton("Difícil", new Color(255, 100, 100), 0.8); //boton dificil
        hardButton.setPreferredSize(buttonSize);
        buttonContainer.add(hardButton, buttonGbc);

        JButton exitButton = createStyledButton("Salir", new Color(200, 200, 200)); //salir
        exitButton.setPreferredSize(buttonSize);
        exitButton.addActionListener(e -> {
            sonido.reproducirClick();
            System.exit(0);
        });
        buttonContainer.add(exitButton, buttonGbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weighty = 0.6;
        add(buttonContainer, gbc);

        gbc.gridwidth = 1;
        gbc.weighty = 0.0;

        backgroundImage = createBackgroundImage(); //el fondo fondo
    }

    private JButton createDifficultyButton(String text, Color color, double difficulty) { //entra a la dificultad
        JButton button = createStyledButton(text, color);
        button.addActionListener(e -> {
            sonido.reproducirClick();
            frame.setContentPane(new JuegoPanel(frame, difficulty));
            frame.revalidate();
            frame.repaint();
        });
        return button;
    }

    private JButton createStyledButton(String text, Color color) { //estética del boton
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 36));
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 4),
                BorderFactory.createEmptyBorder(15, 40, 15, 40)
        ));
        return button;
    }

    private Image createBackgroundImage() { //crea el fondo
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
    protected void paintComponent(Graphics g) { //nuestro dinujito
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
