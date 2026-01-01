package konnekt.component;

import konnekt.view.NavigatorView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SettingsPanel extends JPanel {

    public SettingsPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(header(), BorderLayout.NORTH);
        add(menu(), BorderLayout.CENTER);
    }

    private JPanel header() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Settings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        panel.add(title, BorderLayout.WEST);
        return panel;
    }

    private JPanel menu() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 25, 25, 25));
        panel.setBackground(Color.WHITE);

        panel.add(item("Account", "ACCOUNT"));
        panel.add(item("Privacy Policy", "PRIVACY"));
        panel.add(item("Terms & Conditions", "TERMS"));
        panel.add(item("About", "ABOUT"));
        panel.add(item("Report a Bug", "REPORT"));
        panel.add(item("Help & Support", "HELP"));

        return panel;
    }

    private JPanel item(String text, String route) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        panel.setBorder(new EmptyBorder(12, 15, 12, 15));
        panel.setBackground(Color.WHITE);
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JLabel arrow = new JLabel("▶");
        arrow.setFont(new Font("Segoe UI", Font.BOLD, 16));

        panel.add(label, BorderLayout.WEST);
        panel.add(arrow, BorderLayout.EAST);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(245, 245, 245));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(Color.WHITE);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                NavigatorView.navigate(route);
            }
        });

        return panel;
    }
}
