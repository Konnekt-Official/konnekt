package konnekt.component;

import konnekt.controller.CommentController;
import konnekt.model.dao.CommentDao;
import konnekt.model.dao.PostDao;
import konnekt.model.pojo.CommentPojo;
import konnekt.model.pojo.PostPojo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class CommentPanel extends JPanel {

    private final JPanel container = new JPanel();
    private final JScrollPane scrollPane;
    private final CommentDao commentDao = new CommentDao();
    private final CommentController controller = new CommentController();
    private final PostDao postDao = new PostDao();

    private int postId;
    private PostPojo post;

    private static final Font FONT = new Font("Verdana", Font.PLAIN, 13);
    private static final Font NAME_FONT = new Font("Verdana", Font.BOLD, 13);
    private static final Font COMMENT_FONT = new Font("Verdana", Font.PLAIN, 12);
    private static final ImageIcon DEFAULT_PROFILE;

    static {
        URL url = CommentPanel.class.getClassLoader()
                .getResource("konnekt/resources/images/default_profile.png");
        if (url != null) {
            DEFAULT_PROFILE = new ImageIcon(new ImageIcon(url).getImage()
                    .getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        } else {
            DEFAULT_PROFILE = new ImageIcon();
            System.err.println("Default profile picture not found!");
        }
    }

    public CommentPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(245, 245, 245));

        scrollPane = new JScrollPane(container);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
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
        panel.setBackground(Color.WHITE);

        JTextArea input = new JTextArea(2, 1);
        input.setFont(FONT);
        input.setLineWrap(true);
        input.setWrapStyleWord(true);

        JButton send = new JButton("Comment");
        send.setFont(FONT);
        send.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        send.setFocusable(false);

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
        container.removeAll();

        if (post != null) {
            container.add(createPostCard(post));
            container.add(Box.createVerticalStrut(10));
        }

        List<CommentPojo> comments = commentDao.getCommentsByPost(postId);
        for (CommentPojo c : comments) {
            container.add(createCommentCard(c));
            container.add(Box.createVerticalStrut(8));
        }

        container.revalidate();
        container.repaint();
    }

    private JPanel createPostCard(PostPojo post) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(10, 10, 10, 10));

        // ---------- HEADER ----------
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        header.setBackground(Color.WHITE);

        JLabel avatar = new JLabel(DEFAULT_PROFILE);
        header.add(avatar);

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        namePanel.setBackground(Color.WHITE);

        JLabel fullName = new JLabel(post.getFullName());
        fullName.setFont(NAME_FONT);

        JLabel username = new JLabel("@" + post.getUsername());
        username.setFont(FONT);
        username.setForeground(Color.BLUE);
        username.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel time = new JLabel("· " + timeAgo(post.getCreatedAt()));
        time.setFont(FONT);
        time.setForeground(Color.GRAY);

        namePanel.add(fullName);
        namePanel.add(username);
        namePanel.add(time);
        header.add(namePanel);

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
        JButton commentBtn = new JButton("Comment (" + post.getCommentCount() + ")");

        likeBtn.setFont(FONT);
        commentBtn.setFont(FONT);
        likeBtn.setFocusable(false);
        commentBtn.setFocusable(false);
        likeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        commentBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        likeBtn.addActionListener(e -> {
            new PostDao().likePost(post.getId());
            refresh();
        });

        commentBtn.addActionListener(e -> new CommentController().openComments(post.getId()));

        actions.add(likeBtn);
        actions.add(commentBtn);
        card.add(actions, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createCommentCard(CommentPojo c) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(5, 48, 5, 0));

        // ---------- HEADER ----------
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        header.setBackground(Color.WHITE);

        JLabel avatar = new JLabel(DEFAULT_PROFILE);
        header.add(avatar);

        JLabel fullName = new JLabel(c.getFullName());
        fullName.setFont(new Font("Verdana", Font.BOLD, 12));

        JLabel username = new JLabel("@" + c.getUsername());
        username.setFont(COMMENT_FONT);
        username.setForeground(Color.BLUE);
        username.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel time = new JLabel("· " + timeAgo(c.getCreatedAt()));
        time.setFont(COMMENT_FONT);
        time.setForeground(Color.GRAY);

        header.add(fullName);
        header.add(username);
        header.add(time);

        p.add(header, BorderLayout.NORTH);

        // ---------- CONTENT ----------
        JTextArea content = new JTextArea(c.getContent());
        content.setFont(COMMENT_FONT);
        content.setLineWrap(true);
        content.setWrapStyleWord(true);
        content.setEditable(false);
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(2, 48, 2, 0));

        p.add(content, BorderLayout.CENTER);

        return p;
    }

    private String timeAgo(java.sql.Timestamp ts) {
        if (ts == null) return "";
        Duration d = Duration.between(ts.toInstant(), Instant.now());
        if (d.toMinutes() < 1) return "just now";
        if (d.toHours() < 1) return d.toMinutes() + "m";
        if (d.toDays() < 1) return d.toHours() + "h";
        return d.toDays() + "d";
    }
}
