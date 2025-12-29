package konnekt.component;

import konnekt.controller.CommentController;
import konnekt.model.dao.CommentDao;
import konnekt.model.pojo.CommentPojo;
import konnekt.model.pojo.PostPojo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class CommentPanel extends JPanel {

    private int postId;
    private final JPanel commentList = new JPanel();
    private final CommentDao commentDao = new CommentDao();
    private final CommentController controller = new CommentController();

    private static final Font FONT = new Font("Verdana", Font.PLAIN, 13);

    public CommentPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245,245,245));

        commentList.setLayout(new BoxLayout(commentList, BoxLayout.Y_AXIS));
        commentList.setBackground(new Color(245,245,245));

        add(new JScrollPane(commentList), BorderLayout.CENTER);
        add(createInput(), BorderLayout.SOUTH);
    }

    public void loadPost(PostPojo post) {
        this.postId = post.getId();
        refresh();
    }

    private JPanel createInput() {
        JPanel p = new JPanel(new BorderLayout(5,5));
        p.setBorder(new EmptyBorder(10,10,10,10));

        JTextArea input = new JTextArea(2,1);
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

        p.add(new JScrollPane(input), BorderLayout.CENTER);
        p.add(send, BorderLayout.EAST);
        return p;
    }

    private void refresh() {
        commentList.removeAll();

        List<CommentPojo> comments = commentDao.getCommentsByPost(postId);
        for (CommentPojo c : comments) {
            commentList.add(commentCard(c));
            commentList.add(Box.createVerticalStrut(8));
        }

        commentList.revalidate();
        commentList.repaint();
    }

    private JPanel commentCard(CommentPojo c) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(8,8,8,8));

        JLabel header = new JLabel(
                c.getFullName() + " @" + c.getUsername() + " · " + timeAgo(c.getCreatedAt())
        );
        header.setFont(new Font("Verdana", Font.BOLD, 12));

        JTextArea body = new JTextArea(c.getContent());
        body.setFont(FONT);
        body.setLineWrap(true);
        body.setWrapStyleWord(true);
        body.setEditable(false);
        body.setOpaque(false);

        p.add(header, BorderLayout.NORTH);
        p.add(body, BorderLayout.CENTER);
        return p;
    }

    private String timeAgo(java.sql.Timestamp ts) {
        Duration d = Duration.between(ts.toInstant(), Instant.now());
        if (d.toMinutes() < 1) return "now";
        if (d.toHours() < 1) return d.toMinutes() + "m";
        if (d.toDays() < 1) return d.toHours() + "h";
        return d.toDays() + "d";
    }
}
