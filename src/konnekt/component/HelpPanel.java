package konnekt.component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HelpPanel extends JPanel {

    public HelpPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Help & Support");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JTextArea text = new JTextArea(
            "If you need assistance while using Konnekt, we are here to help.\n\n" +

            "You can find answers to common questions within the app or " +
            "contact our support team for further assistance.\n\n" +

            "Support Email: rajeshthapa.cs@gmail.com\n" +
            "Response time: within 24–48 hours."
        );

        text.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        text.setBorder(new EmptyBorder(20, 0, 0, 0));
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setBackground(Color.WHITE);

        add(title, BorderLayout.NORTH);
        add(text, BorderLayout.CENTER);
    }
}
