package konnekt.utils;

import javax.swing.*;
import java.awt.*;

public class Toast {

    public static void showToast(String message, int durationMillis) {
        // Create a small undecorated frame
        JWindow window = new JWindow();
        window.setBackground(new Color(0, 0, 0, 0));

        // Panel with background and rounded corners
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 200)); // semi-transparent black
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel label = new JLabel(message);
        label.setForeground(Color.WHITE);
        panel.add(label);
        window.add(panel);
        window.pack();

        // Position at bottom-right of screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width - window.getWidth() - 20;
        int y = screenSize.height - window.getHeight() - 50;
        window.setLocation(x, y);

        window.setVisible(true);

        // Auto-close after duration
        new Timer(durationMillis, e -> window.dispose()).start();
    }
}
