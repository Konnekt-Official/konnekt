package konnekt.component;

import konnekt.controller.CommentController;
import konnekt.model.dao.CommentDao;
import konnekt.model.dao.PostDao;
import konnekt.model.pojo.CommentPojo;
import konnekt.model.pojo.PostPojo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class CommentPanel extends JPanel {

    private final JPanel commentList = new JPanel();
    private final CommentDao commentDao = new CommentDao();
    private final CommentController controller = new CommentController();
    private final PostDao postDao = new PostDao();

    private int postId;
    private PostPojo post;

    private static final Font USER_FONT = new Font("Verdana", Font.BOLD, 12);
    private static final Font CONTENT_FONT = new Font("Verdana", Font.PLAIN, 13);
    private static final ImageIcon DEFAULT_PROFILE = new ImageIcon(
            CommentPanel.class.getResource("/konnekt/resources/default_profile.png")
    );

    public CommentPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        commentList.setLayout(new BoxLayout(commentList, BoxLayout.Y_AXIS));
        commentList.setBackground(new Color(245, 245, 245));

        add(new JScrollPane(commentList), BorderLayout.CENTER);
        add(createInputPanel(), BorderLayout.SOUTH);
    }

    public void loadPost(int postId) {
        this.postId = postId;
        this.post = postDao.getPostById(postId);
        refresh();
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextArea input = new JTextArea(2, 1);
        input.setFont(CONTENT_FONT);
        input.setLineWrap(true);
        input.setWrapStyleWord(true);

        JButton send = new JButton("Comment");
        send.setFont(CONTENT_FONT);
        send.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        send.addActionListener(e -> {
            if (!input.getText().trim().isEmpty()) {
                controller.addComment(postId, input.getText());
                input.setText("");
                refresh();
            }
        });

        panel.add(new JScrollPane(input), BorderLayout.CENTER);
        panel.add(send, BorderLayout.EAST);
        return panel;
    }

    private void refresh() {
        commentList.removeAll();

        // Post card
        if (post != null) {
            commentList.add(createPostCard(post));
            commentList.add(Box.createVerticalStrut(15));
        }

        // Comments
        List<CommentPojo> comments = commentDao.getCommentsByPost(postId);
        for (CommentPojo c : comments) {
            // commentList.add(commentCard(c));
            commentList.add(Box.createVerticalStrut(8));
        }

        commentList.revalidate();
        commentList.repaint();
    }

    private JPanel createPostCard(PostPojo post) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        p.setBackground(Color.WHITE);

        // Header: Poster info
        JLabel header = new JLabel(post.getFullName() + " @" + post.getUsername());
        header.setFont(new Font("Verdana", Font.BOLD, 14));
        header.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        header.setForeground(Color.BLUE);
        header.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // show profile panel of user
            }
        });

        // Post content
        JTextArea content = new JTextArea(post.getContent());
        content.setFont(USER_FONT);
        content.setLineWrap(true);
        content.setWrapStyleWord(true);
        content.setEditable(false);
        content.setOpaque(false);

        // Footer: Likes and comments
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        footer.setBackground(Color.WHITE);

        JButton likeBtn = new JButton("Like (" + post.getLikes() + ")");
        likeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        likeBtn.addActionListener(e -> {
            postDao.likePost(post.getId());
            post.setLikes(post.getLikes() + 1);
            likeBtn.setText("Like (" + post.getLikes() + ")");
        });

        JLabel commentCountLbl = new JLabel("Comments: " + post.getCommentCount());

        footer.add(likeBtn);
        footer.add(commentCountLbl);

        // Add everything
        p.add(header, BorderLayout.NORTH);
        p.add(content, BorderLayout.CENTER);
        p.add(footer, BorderLayout.SOUTH);

        return p;
    }

    private JPanel createCommentCard(CommentPojo c) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(250, 250, 250));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1, true),
                new EmptyBorder(8, 8, 8, 8)
        ));

        // ---------- HEADER ----------
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        headerPanel.setOpaque(false);

        JLabel profilePic = new JLabel(DEFAULT_PROFILE);
        profilePic.setPreferredSize(new Dimension(30, 30));

        JLabel username = new JLabel(c.getFullName() + " @" + c.getUsername());
        username.setFont(USER_FONT);
        username.setForeground(new Color(0, 102, 204));
        username.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        username.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(panel,
                        "Switching to " + c.getUsername() + "'s profile");
            }
        });

        JLabel timeLabel = new JLabel("· " + timeAgo(c.getCreatedAt()));
        timeLabel.setFont(new Font("Verdana", Font.PLAIN, 11));
        timeLabel.setForeground(Color.DARK_GRAY);

        headerPanel.add(profilePic);
        headerPanel.add(username);
        headerPanel.add(timeLabel);

        // ---------- CONTENT ----------
        JTextArea content = new JTextArea(c.getContent());
        content.setFont(CONTENT_FONT);
        content.setLineWrap(true);
        content.setWrapStyleWord(true);
        content.setEditable(false);
        content.setOpaque(false);

        // ---------- COMMENT ACTIONS ----------
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        actionPanel.setOpaque(false);

        JButton likeBtn = new JButton("Like (" + c.getLikes() + ")");
        likeBtn.setFont(new Font("Verdana", Font.PLAIN, 11));
        likeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        likeBtn.addActionListener(e -> {
            controller.likeComment(c.getId());
            c.setLikes(c.getLikes() + 1);
            likeBtn.setText("Like (" + c.getLikes() + ")");
        });

        actionPanel.add(likeBtn);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);

        return panel;
    }

    private String timeAgo(java.sql.Timestamp ts) {
        Duration d = Duration.between(ts.toInstant(), Instant.now());
        if (d.toMinutes() < 1) {
            return "now";
        }
        if (d.toHours() < 1) {
            return d.toMinutes() + "m";
        }
        if (d.toDays() < 1) {
            return d.toHours() + "h";
        }
        return d.toDays() + "d";
    }
}
