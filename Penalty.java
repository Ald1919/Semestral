import javax.swing.*;

public class Penalty {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("⚽ Juego de Penales ⚽");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new MenuPanel(frame));
            frame.setVisible(true);
        });
    }
}