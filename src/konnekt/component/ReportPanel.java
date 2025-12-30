package konnekt.component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URI;

public class ReportPanel extends JPanel {

    private static final String GITHUB_ISSUE_URL =
            "https://github.com/konnekt-official/konnekt/issues/new";

    public ReportPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Report a Bug");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JTextArea reportArea = new JTextArea(
                "Please describe the issue you encountered.\n\n" +
                "Tip: Include steps to reproduce, expected behavior, and screenshots if possible."
        );
        reportArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        reportArea.setLineWrap(true);
        reportArea.setWrapStyleWord(true);

        JButton githubButton = new JButton("Report on GitHub");
        githubButton.setFocusPainted(false);
        githubButton.addActionListener(this::openGitHub);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(Color.WHITE);
        bottom.add(githubButton);

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(reportArea), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private void openGitHub(ActionEvent e) {
        try {
            Desktop.getDesktop().browse(new URI(GITHUB_ISSUE_URL));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to open GitHub.\nPlease check your internet connection.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
