import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuPanel extends JPanel {
    private final JFrame frame;
    private final Image background;

    public MenuPanel(JFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);

        JButton facil = createStyledButton("Fácil", new Color(100, 255, 100));
        JButton medio = createStyledButton("Medio", new Color(255, 255, 100));
        JButton dificil = createStyledButton("Difícil", new Color(255, 100, 100));
        JButton salir = createStyledButton("Salir", new Color(200, 200, 200));

        facil.addActionListener(e -> {
            frame.setContentPane(new JuegoPanel(frame, 0.2));
            frame.revalidate();
        });

        medio.addActionListener(e -> {
            frame.setContentPane(new JuegoPanel(frame, 0.5));
            frame.revalidate();
        });

        dificil.addActionListener(e -> {
            frame.setContentPane(new JuegoPanel(frame, 0.8));
            frame.revalidate();
        });

        salir.addActionListener(e -> System.exit(0));

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.add(facil, gbc);
        buttonPanel.add(medio, gbc);
        buttonPanel.add(dificil, gbc);
        buttonPanel.add(salir, gbc);

        JLabel title = new JLabel("JUEGO DE PENALES", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.WHITE);

        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        background = createBackgroundImage();
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }

    private Image createBackgroundImage() {
        Image image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        GradientPaint gradient = new GradientPaint(0, 0, new Color(0, 80, 0), 0, 600, Color.BLACK);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, 800, 600);

        g2d.setColor(new Color(0, 100, 0));
        for (int i = 0; i < 800; i += 20) {
            for (int j = 0; j < 600; j += 20) {
                g2d.drawLine(i, j, i, j);
            }
        }

        g2d.dispose();
        return image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }
}