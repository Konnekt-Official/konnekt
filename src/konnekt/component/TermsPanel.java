package konnekt.component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TermsPanel extends JPanel {

    public TermsPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Terms & Conditions");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JTextArea text = new JTextArea(
            "By accessing or using Konnekt, you agree to be bound by these Terms and Conditions.\n\n" +

            "Users are expected to behave responsibly and respectfully when using the platform. " +
            "Any form of abuse, harassment, or misuse of the application is strictly prohibited.\n\n" +

            "Konnekt reserves the right to suspend or terminate accounts that violate " +
            "these terms without prior notice.\n\n" +

            "Continued use of the application indicates acceptance of all terms stated above."
        );

        text.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setBackground(Color.WHITE);

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(text), BorderLayout.CENTER);
    }
}