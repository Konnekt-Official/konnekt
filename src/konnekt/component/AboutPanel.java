package konnekt.component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AboutPanel extends JPanel {

    public AboutPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // Title
        JLabel title = new JLabel("About Konnekt");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        // Text body
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
        text.setBorder(new EmptyBorder(20, 0, 0, 0)); // adds gap below title

        // ScrollPane without border
        JScrollPane scrollPane = new JScrollPane(text);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // GitHub link
        JLabel githubLink = new JLabel("<html><a href=''>GitHub Repository</a></html>");
        githubLink.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        githubLink.setForeground(Color.BLUE);
        githubLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        githubLink.setBorder(new EmptyBorder(10, 0, 0, 0)); // gap above link
        githubLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new java.net.URI("https://github.com/konnekt-official/konnekt"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(githubLink, BorderLayout.SOUTH);
    }
}
