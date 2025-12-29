package konnekt.component;

import konnekt.controller.CommentController;
import konnekt.model.dao.CommentDao;
import konnekt.model.dao.PostDao;
import konnekt.model.pojo.CommentPojo;
import konnekt.model.pojo.PostPojo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
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

    private static final Font FONT = new Font("Verdana", Font.PLAIN, 13);

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
        input.setFont(FONT);
        input.setLineWrap(true);

        JButton send = new JButton("Comment");
        send.setFont(FONT);
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

        // ---------- POST ----------
        if (post != null) {
            commentList.add(createPostCard(post));
            commentList.add(Box.createVerticalStrut(15));
        }

        // ---------- COMMENTS ----------
        List<CommentPojo> comments = commentDao.getCommentsByPost(postId);
        for (CommentPojo c : comments) {
            commentList.add(createCommentCard(c));
            commentList.add(Box.createVerticalStrut(8));
        }

        commentList.revalidate();
        commentList.repaint();
    }

    private JPanel createPostCard(PostPojo post) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel header = new JLabel(post.getFullName() + " @" + post.getUsername());
        header.setFont(new Font("Verdana", Font.BOLD, 14));
        // message: clicking username should switch to profile panel

        JTextArea body = new JTextArea(post.getContent());
        body.setFont(FONT);
        body.setLineWrap(true);
        body.setWrapStyleWord(true);
        body.setEditable(false);
        body.setOpaque(false);

        panel.add(header, BorderLayout.NORTH);
        panel.add(body, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCommentCard(CommentPojo c) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));

        JLabel header = new JLabel(
                c.getFullName() + " @" + c.getUsername() + " · " + timeAgo(c.getCreatedAt())
        );
        header.setFont(new Font("Verdana", Font.BOLD, 12));
        // message: clicking username should switch to profile panel

        JTextArea body = new JTextArea(c.getContent());
        body.setFont(FONT);
        body.setLineWrap(true);
        body.setWrapStyleWord(true);
        body.setEditable(false);
        body.setOpaque(false);

        panel.add(header, BorderLayout.NORTH);
        panel.add(body, BorderLayout.CENTER);
        return panel;
    }

    private String timeAgo(java.sql.Timestamp ts) {
        Duration d = Duration.between(ts.toInstant(), Instant.now());
        if (d.toMinutes() < 1) return "now";
        if (d.toHours() < 1) return d.toMinutes() + "m";
        if (d.toDays() < 1) return d.toHours() + "h";
        return d.toDays() + "d";
    }
}
