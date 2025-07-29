package JuegoPanel;
import javax.swing.*;
public class Penalty {
    public static void main(String[] args) {
        JFrame frame = new JFrame("⚽ Juego de Penales ⚽");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setResizable(false);
        frame.add(new JuegoPanel());
        frame.setVisible(true);
    }
}