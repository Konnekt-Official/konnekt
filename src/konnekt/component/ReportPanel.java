package konnekt.component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

public class ReportPanel extends JPanel {

    private static final String GITHUB_ISSUE_URL =
            "https://github.com/konnekt-official/konnekt/issues/new";

    public ReportPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // Title
        JLabel title = new JLabel("Report a Bug");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        // Report instructions text
        JTextArea reportArea = new JTextArea(
                "Please describe the issue you encountered.\n\n" +
                "Tip: Include steps to reproduce, expected behavior, and screenshots if possible.\n\n" +
                "Providing detailed information helps us fix the issue faster and improve the platform for everyone."
        );
        reportArea.setFont(new Font("Verdana", Font.PLAIN, 14));
        reportArea.setLineWrap(true);
        reportArea.setWrapStyleWord(true);
        reportArea.setEditable(false);
        reportArea.setBackground(Color.WHITE);
        reportArea.setBorder(new EmptyBorder(20, 0, 0, 0)); // gap below title

        // ScrollPane without border
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // GitHub link label
        JLabel githubLink = new JLabel("<html><a href=''>Report on GitHub</a></html>");
        githubLink.setFont(new Font("Verdana", Font.PLAIN, 14));
        githubLink.setForeground(Color.BLUE);
        githubLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        githubLink.setBorder(new EmptyBorder(10, 0, 0, 0)); // gap above link
        githubLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(GITHUB_ISSUE_URL));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            ReportPanel.this,
                            "Unable to open GitHub.\nPlease check your internet connection.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(githubLink, BorderLayout.SOUTH);
    }
}
