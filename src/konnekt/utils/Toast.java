package konnekt.utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Toast extends JWindow {

    private static Toast activeToast;
    private float opacity = 0f;

    public static void showToast(
            ImageIcon avatar,
            String fullName,
            String actionText,
            String messageText,
            String time,
            Runnable onClick
    ) {
        if (activeToast != null) {
            activeToast.dispose();
        }
        activeToast = new Toast(
                avatar, fullName, actionText, messageText, time, onClick
        );
        activeToast.setVisible(true);
    }

    private Toast(
            ImageIcon avatar,
            String fullName,
            String actionText,
            String messageText,
            String time,
            Runnable onClick
    ) {
        setAlwaysOnTop(true);
        setBackground(new Color(0, 0, 0, 0));

        JPanel root = new JPanel();
        root.setBackground(new Color(30, 30, 30));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));

        // ---------- TITLE ----------
        JLabel title = new JLabel("KONNEKT");
        title.setFont(new Font("Verdana", Font.BOLD, 18));
        title.setForeground(new Color(144, 238, 144));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(title);

        root.add(Box.createVerticalStrut(8));

        // ---------- CONTENT ----------
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel avatarLabel = new JLabel(makeRoundedAvatar(avatar, 44));
        row.add(avatarLabel);
        row.add(Box.createHorizontalStrut(10));

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setOpaque(false);

        JLabel nameAction = new JLabel("<html><b>" + fullName + "</b> " + actionText + "</html>");
        nameAction.setFont(new Font("Verdana", Font.PLAIN, 14));
        nameAction.setForeground(Color.WHITE);

        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Verdana", Font.ITALIC, 11));
        timeLabel.setForeground(Color.GRAY);

        text.add(nameAction);
        text.add(timeLabel);

        // MESSAGE ONLY
        if (messageText != null && !messageText.isBlank()) {
            text.add(Box.createVerticalStrut(4));
            JLabel msg = new JLabel("<html>" + messageText + "</html>");
            msg.setFont(new Font("Verdana", Font.PLAIN, 13));
            msg.setForeground(new Color(220, 220, 220));
            text.add(msg);
        }

        row.add(text);
        root.add(row);

        add(root);
        pack();

        // ---------- POSITION ----------
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screen.width - getWidth() - 20, screen.height - getHeight() - 60);

        // ---------- CLICK ----------
        root.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        root.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (onClick != null) {
                    onClick.run();
                }
                fadeOut();
            }
        });

        fadeIn();

        // ---------- AUTO CLOSE ----------
        new Timer(10000, e -> fadeOut()).start();
    }

    // ---------- ANIMATION ----------
    private void fadeIn() {
        Timer t = new Timer(25, null);
        t.addActionListener(e -> {
            opacity += 0.05f;
            if (opacity >= 1f) {
                opacity = 1f;
                t.stop();
            }
            setOpacity(opacity);
        });
        t.start();
    }

    private void fadeOut() {
        Timer t = new Timer(25, null);
        t.addActionListener(e -> {
            opacity -= 0.05f;
            if (opacity <= 0f) {
                t.stop();
                dispose();
            }
            setOpacity(opacity);
        });
        t.start();
    }

    // ---------- ROUNDED AVATAR ----------
    private ImageIcon makeRoundedAvatar(ImageIcon src, int size) {
        Image img = src.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        BufferedImage mask = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = mask.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
        g2.drawImage(img, 0, 0, null);
        g2.dispose();

        return new ImageIcon(mask);
    }
}
