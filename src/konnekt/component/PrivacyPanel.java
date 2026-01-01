package konnekt.component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PrivacyPanel extends JPanel {

    public PrivacyPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Privacy Policy");
        title.setFont(new Font("Verdana", Font.BOLD, 22));

        JTextArea text = new JTextArea(
                "Your privacy is extremely important to us. Konnekt is designed to protect "
                + "your personal information and provide a safe and trusted platform.\n\n"
                + "We collect only the information necessary to operate the application, "
                + "such as account details, profile data, and interactions within the app.\n\n"
                + "Konnekt does not sell, rent, or share your personal data with third parties. "
                + "All stored information is secured using standard security practices.\n\n"
                + "By using Konnekt, you agree to the collection and use of information in "
                + "accordance with this Privacy Policy."
        );

        text.setFont(new Font("Verdana", Font.PLAIN, 14));
        text.setBorder(new EmptyBorder(20, 0, 0, 0));
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setBackground(Color.WHITE);

        add(title, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(text);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }
}
