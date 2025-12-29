package konnekt.component;

import konnekt.controller.PostController;
import konnekt.model.dao.PostDao;
import konnekt.model.pojo.PostPojo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class FeedPanel extends JPanel {

    private final PostDao postDao = new PostDao();
    private final PostController postController = new PostController();

    private final JPanel postsContainer = new JPanel();
    private final JScrollPane scrollPane;

    private static final Font FONT = new Font("Verdana", Font.PLAIN, 13);

    public FeedPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        add(createInputPanel(), BorderLayout.NORTH);

        postsContainer.setLayout(new BoxLayout(postsContainer, BoxLayout.Y_AXIS));
        postsContainer.setBackground(new Color(245, 245, 245));

        scrollPane = new JScrollPane(postsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
        refreshFeed();
    }

    // ================= INPUT PANEL =================
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextArea input = new JTextArea(2, 1);
        input.setFont(FONT);
        input.setLineWrap(true);
        input.setWrapStyleWord(true);
        input.setText("What's on your mind?");
        input.setForeground(Color.GRAY);

        input.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (input.getText().equals("What's on your mind?")) {
                    input.setText("");
                    input.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (input.getText().isEmpty()) {
                    input.setText("What's on your mind?");
                    input.setForeground(Color.GRAY);
                }
            }
        });

        JScrollPane inputScroll = new JScrollPane(input);
        inputScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        JButton postBtn = new JButton("Post");
        postBtn.setFont(FONT);
        postBtn.setFocusable(false);
        postBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        postBtn.addActionListener(e -> {
            String text = input.getText().trim();
            if (!text.isEmpty() && !text.equals("What's on your mind?")) {
                postController.createPost(this, text);
                input.setText("What's on your mind?");
                input.setForeground(Color.GRAY);
                refreshFeed();

                SwingUtilities.invokeLater(() ->
                        scrollPane.getVerticalScrollBar().setValue(0)
                );
            }
        });

        panel.add(inputScroll, BorderLayout.CENTER);
        panel.add(postBtn, BorderLayout.EAST);
        return panel;
    }

    // ================= REFRESH FEED =================
    private void refreshFeed() {
        postsContainer.removeAll();

        List<PostPojo> posts = postDao.getAllPosts();
        for (PostPojo post : posts) {
            postsContainer.add(createPostCard(post));
            postsContainer.add(Box.createVerticalStrut(10));
        }

        postsContainer.revalidate();
        postsContainer.repaint();
        requestFocusInWindow();
    }

    // ================= POST CARD =================
    private JPanel createPostCard(PostPojo post) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(10, 10, 10, 10));

        // ---------- HEADER ROW ----------
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);

        JLabel avatar = new JLabel();
        ImageIcon icon = new ImageIcon(
                getClass().getResource("/konnekt/resources/images/default_profile.png")
        );
        avatar.setIcon(new ImageIcon(
                icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)
        ));
        avatar.setBorder(new EmptyBorder(0, 0, 0, 8));
        header.add(avatar, BorderLayout.WEST);

        JPanel nameRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        nameRow.setBackground(Color.WHITE);

        JLabel fullName = new JLabel(post.getFullName());
        fullName.setFont(new Font("Verdana", Font.BOLD, 13));

        JLabel username = new JLabel("@" + post.getUsername());
        username.setFont(FONT);
        username.setForeground(Color.BLUE);
        username.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel time = new JLabel("· " + timeAgo(post.getCreatedAt()));
        time.setFont(FONT);
        time.setForeground(Color.GRAY);

        nameRow.add(fullName);
        nameRow.add(username);
        nameRow.add(time);

        header.add(nameRow, BorderLayout.CENTER);
        card.add(header, BorderLayout.NORTH);

        // ---------- CONTENT ----------
        JTextArea content = new JTextArea(post.getContent());
        content.setFont(FONT);
        content.setLineWrap(true);
        content.setWrapStyleWord(true);
        content.setEditable(false);
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(6, 48, 6, 0));
        card.add(content, BorderLayout.CENTER);

        // ---------- ACTIONS ----------
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        actions.setBackground(Color.WHITE);
        actions.setBorder(new EmptyBorder(0, 48, 0, 0));

        JButton likeBtn = new JButton("Like (" + post.getLikes() + ")");
        JButton commentBtn = new JButton("Comment");

        likeBtn.setFont(FONT);
        commentBtn.setFont(FONT);

        likeBtn.setFocusable(false);
        commentBtn.setFocusable(false);

        likeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        commentBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        likeBtn.addActionListener(e -> {
            postController.likePost(post.getId());
            refreshFeed();
        });

        commentBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Open comments view")
        );

        actions.add(likeBtn);
        actions.add(commentBtn);
        card.add(actions, BorderLayout.SOUTH);

        return card;
    }

    // ================= TIME =================
    private String timeAgo(java.sql.Timestamp ts) {
        if (ts == null) return "";

        Duration d = Duration.between(ts.toInstant(), Instant.now());
        if (d.toMinutes() < 1) return "just now";
        if (d.toHours() < 1) return d.toMinutes() + "m";
        if (d.toDays() < 1) return d.toHours() + "h";
        return d.toDays() + "d";
    }
}
