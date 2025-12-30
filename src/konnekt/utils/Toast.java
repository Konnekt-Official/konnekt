package konnekt.utils;

import konnekt.view.NavigatorView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Toast extends JWindow {

    private final int duration;

    /**
     * @param message the message to show
     * @param durationMillis how long to show in milliseconds
     * @param onClick Runnable to execute when toast is clicked (can be null)
     */
    public Toast(String message, int durationMillis, Runnable onClick) {
        this.duration = durationMillis;

        JPanel panel = new JPanel();
        panel.setBackground(new Color(50, 50, 50, 220)); // semi-transparent dark
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(message);
        label.setForeground(Color.WHITE);
        panel.add(label);

        add(panel);
        pack();

        // Position bottom-right
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width - getWidth() - 50;
        int y = screenSize.height - getHeight() - 50;
        setLocation(x, y);

        if (onClick != null) {
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onClick.run();
                    setVisible(false);
                    dispose();
                }
            });
        }
    }

    public void showToast() {
        setVisible(true);

        // Auto-hide after duration
        new javax.swing.Timer(duration, e -> {
            setVisible(false);
            dispose();
        }).start();
    }

    // Convenience method without onClick
    public static void show(String message, int durationMillis) {
        new Toast(message, durationMillis, null).showToast();
    }

    // Convenience method with onClick
    public static void show(String message, int durationMillis, Runnable onClick) {
        new Toast(message, durationMillis, onClick).showToast();
    }
}