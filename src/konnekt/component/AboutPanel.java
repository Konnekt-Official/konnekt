package konnekt.component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AboutPanel extends JPanel {

    public AboutPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("About Konnekt");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JTextArea text = new JTextArea(
            "Konnekt is a Java-based social media and messaging platform developed as part of the " +
            "ST4005CMD – Integrative Project at Softwarica College of IT and E-Commerce.\n\n" +

            "The platform is designed to provide a modern social experience by combining " +
            "real-time communication with social networking features.\n\n" +

            "Key features of Konnekt include:\n" +
            "• Real-time messaging (one-to-one and group chats)\n" +
            "• Audio and video calling\n" +
            "• Social feed with posts, comments, and likes\n" +
            "• User profiles, settings, and notifications\n" +
            "• Followers and following system\n" +
            "• Community and membership features\n" +
            "• Short videos (Shorts) and content sharing\n" +
            "• Admin panel for platform management\n\n" +

            "Konnekt focuses on usability, performance, and scalability while serving as an " +
            "academic and practical implementation of a full-featured social platform."
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
