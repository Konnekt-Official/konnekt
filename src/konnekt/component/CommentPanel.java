package konnekt.component;

import konnekt.controller.CommentController;
import konnekt.model.dao.CommentDao;
import konnekt.model.dao.PostDao;
import konnekt.model.pojo.CommentPojo;
import konnekt.model.pojo.PostPojo;
import konnekt.utils.AvatarUtil;
import konnekt.view.NavigatorView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import konnekt.controller.NotificationController;

public class CommentPanel extends JPanel {

    private final JPanel list = new JPanel();
    private final CommentDao commentDao = new CommentDao();
    private final PostDao postDao = new PostDao();
    private final CommentController controller = new CommentController();
    private final NotificationController notificationController = new NotificationController();

    private int postId;

    public CommentPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setAlignmentX(Component.LEFT_ALIGNMENT);
        list.setBackground(new Color(245, 245, 245));

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);
        add(createInputPanel(), BorderLayout.SOUTH);
    }

    public void loadPost(int postId) {
        this.postId = postId;
        refresh();
    }

    // ---------- INPUT ----------
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));
        panel.setBackground(Color.WHITE);

        JTextArea input = new JTextArea(2, 1);
        input.setLineWrap(true);
        input.setWrapStyleWord(true);
        input.setFont(new Font("Verdana", Font.PLAIN, 13));

        JButton send = new JButton("Comment");
        send.setFocusable(false);
        send.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        send.setForeground(Color.WHITE);
        send.setBackground(Color.BLACK);
        send.setFont(new Font("Verdana", Font.BOLD, 12));

        send.addActionListener(e -> {
            String text = input.getText().trim();
            if (!text.isEmpty()) {
                controller.addComment(postId, text);
                input.setText("");
                notificationController.notifyComment(postDao.getPostById(postId).getUserId(), postId);
                refresh();
            }
        });

        panel.add(new JScrollPane(input), BorderLayout.CENTER);
        panel.add(send, BorderLayout.EAST);
        return panel;
    }

    // ---------- REFRESH ----------
    private void refresh() {
        list.removeAll();

        PostPojo post = postDao.getPostById(postId);
        if (post != null) {
            list.add(createPostItem(post));
            list.add(Box.createVerticalStrut(10));
        }

        List<CommentPojo> comments = commentDao.getCommentsByPost(postId);
        for (CommentPojo c : comments) {
            list.add(createCommentItem(c));
            list.add(Box.createVerticalStrut(5));
        }

        list.revalidate();
        list.repaint();
    }

    // ---------- POST ----------
    private JPanel createPostItem(PostPojo post) {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        root.setBorder(new EmptyBorder(6, 8, 6, 8));

        // ---------- HEADER ----------
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        header.setBackground(Color.WHITE);

        JLabel avatar = AvatarUtil.avatar(40);
        header.add(avatar);

        JLabel name = new JLabel(
                "<html><b>" + post.getFullName() + "</b> "
                + "<span style='font-weight:normal; color:blue'>@" + post.getUsername() + "</span> "
                + "<span style='font-weight:normal; color:gray'>• " + timeAgo(post.getCreatedAt()) + "</font></html>"
        );
        name.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        name.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                NavigatorView.showProfile(post.getUserId());
            }
        });

        header.add(name);
        root.add(header, BorderLayout.NORTH);

        // ---------- CONTENT ----------
        JPanel contentWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        contentWrapper.setBackground(Color.WHITE);

        JLabel body = new JLabel("<html>" + post.getContent() + "</html>");
        body.setFont(new Font("Verdana", Font.PLAIN, 13));
        int maxWidth = 500; // adjust as needed
        body.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));
        body.setPreferredSize(new Dimension(maxWidth, body.getPreferredSize().height));

        contentWrapper.add(body);
        root.add(contentWrapper, BorderLayout.CENTER);

        // ---------- ACTIONS ----------
        JPanel meta = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        meta.setBackground(Color.WHITE);

        JButton like = new JButton("Like (" + post.getLikes() + ")");
        JButton comment = new JButton("Comment (" + post.getCommentCount() + ")");
        like.setFocusable(false);
        comment.setFocusable(false);
        like.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        comment.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        like.addActionListener(e -> {
            postDao.incrementLike(post.getId());
            notificationController.notifyLike(post.getUserId(), post.getId());
            refresh();
        });

        comment.addActionListener(e -> NavigatorView.showComments(post.getId()));

        meta.add(like);
        meta.add(comment);
        root.add(meta, BorderLayout.SOUTH);

        // Fit-content height
        root.setMaximumSize(new Dimension(Integer.MAX_VALUE, root.getPreferredSize().height));

        return root;
    }

    // ---------- COMMENT ----------
    private JPanel createCommentItem(CommentPojo c) {
        JPanel root = new JPanel(new BorderLayout(10, 0));
        root.setBackground(Color.WHITE);
        root.setBorder(new EmptyBorder(6, 48, 6, 8));

        JLabel avatar = AvatarUtil.avatar(32);
        root.add(avatar, BorderLayout.WEST);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        // Header with clickable username
        JLabel header = new JLabel(
                "<html><b>" + c.getFullName() + "</b> "
                + "<span style='font-weight:normal; color:blue'>@" + c.getUsername() + "</span> "
                + "<span style='font-weight:normal; color:gray'>• " + timeAgo(c.getCreatedAt()) + "</span></html>"
        );
        header.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                NavigatorView.showProfile(c.getUserId());
            }
        });

        // Comment text normal
        JLabel body = new JLabel("<html>" + c.getContent() + "</html>");
        body.setFont(new Font("Verdana", Font.PLAIN, 13));

        content.add(header);
        content.add(Box.createVerticalStrut(4)); // small gap between header and text
        content.add(body);

        root.add(content, BorderLayout.CENTER);
        root.setMaximumSize(new Dimension(Integer.MAX_VALUE, root.getPreferredSize().height));

        return root;
    }

    // ---------- TIME ----------
    private String timeAgo(java.sql.Timestamp ts) {
        if (ts == null) {
            return "";
        }
        Duration d = Duration.between(ts.toInstant(), Instant.now());
        if (d.toMinutes() < 1) {
            return "Just Now";
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
