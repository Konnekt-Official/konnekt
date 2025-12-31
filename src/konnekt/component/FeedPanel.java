package konnekt.component;

import konnekt.controller.PostController;
import konnekt.model.dao.PostDao;
import konnekt.model.pojo.PostPojo;
import konnekt.view.NavigatorView;
import konnekt.manager.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import konnekt.controller.NotificationController;

public class FeedPanel extends JPanel {

    private final PostDao postDao = new PostDao();
    private final PostController postController = new PostController();
    private final NotificationController notificationController = new NotificationController();

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
        scrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );

        add(scrollPane, BorderLayout.CENTER);
        refreshFeed();
    }

    // ================= INPUT =================
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
        inputScroll.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );

        JButton postBtn = new JButton("Post");
        postBtn.setFont(FONT);
        postBtn.setFocusable(false);
        postBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        postBtn.addActionListener(e -> {
            String text = input.getText().trim();
            if (!text.isEmpty() && !text.equals("What's on your mind?")) {
                postController.createPost(SessionManager.getCurrentUserId(), text);

                // reset input
                input.setText("What's on your mind?");
                input.setForeground(Color.GRAY);

                // refresh UI
                refreshFeed();
                scrollToTop();

                // optionally refresh profile panel
                NavigatorView.refreshProfile(SessionManager.getCurrentUserId());
            }
        });

        panel.add(inputScroll, BorderLayout.CENTER);
        panel.add(postBtn, BorderLayout.EAST);
        return panel;
    }

    // ================= FEED =================
    public void refreshFeed() {
        postsContainer.removeAll();

        List<PostPojo> posts = postDao.getAllPosts();

        if (posts.isEmpty()) {
            JLabel msg = new JLabel("No posts available.");
            msg.setFont(new Font("Verdana", Font.PLAIN, 12));
            msg.setForeground(Color.GRAY);
            msg.setAlignmentX(Component.CENTER_ALIGNMENT);
            postsContainer.add(Box.createVerticalStrut(20));
            postsContainer.add(msg);
        } else {
            postsContainer.add(Box.createVerticalStrut(10)); // GAP below input panel
            for (PostPojo post : posts) {
                postsContainer.add(createPostCard(post));
                postsContainer.add(Box.createVerticalStrut(10));
            }
        }

        postsContainer.revalidate();
        postsContainer.repaint();
    }

    // ================= POST CARD =================
    private JPanel createPostCard(PostPojo post) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(10, 10, 10, 10));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ---------- HEADER ----------
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setBackground(Color.WHITE);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel avatar = new JLabel(new ImageIcon(
                new ImageIcon(
                        getClass().getResource("/konnekt/resources/images/default_profile-2.png")
                ).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)
        ));

        header.add(avatar);
        header.add(Box.createHorizontalStrut(8));

        JPanel nameRow = new JPanel();
        nameRow.setLayout(new BoxLayout(nameRow, BoxLayout.X_AXIS));
        nameRow.setBackground(Color.WHITE);

        JLabel fullName = new JLabel(post.getFullName());
        fullName.setFont(new Font("Verdana", Font.BOLD, 13));

        JLabel username = new JLabel(" @" + post.getUsername());
        username.setFont(new Font("Verdana", Font.PLAIN, 13));
        username.setForeground(Color.BLUE);
        username.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        username.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                NavigatorView.showProfile(post.getUserId());
            }
        });

        JLabel time = new JLabel(" · " + timeAgo(post.getCreatedAt()));
        time.setFont(FONT);
        time.setForeground(Color.GRAY);

        nameRow.add(fullName);
        nameRow.add(username);
        nameRow.add(time);

        header.add(nameRow);

        header.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, header.getPreferredSize().height)
        );

        card.add(header);
        card.add(Box.createVerticalStrut(6));

        // ---------- CONTENT (TRUE FIT CONTENT) ----------
        JTextArea content = new JTextArea(post.getContent());
        content.setFont(FONT);
        content.setLineWrap(true);
        content.setWrapStyleWord(true);
        content.setEditable(false);
        content.setOpaque(false);

        // align under name (avatar + gap = ~48px)
        content.setBorder(new EmptyBorder(0, 48, 0, 0));
        content.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.setMaximumSize(new Dimension(
                Integer.MAX_VALUE,
                content.getPreferredSize().height
        ));

        card.add(content);
        card.add(Box.createVerticalStrut(6));

        // ---------- ACTIONS ----------
        JPanel actions = new JPanel();
        actions.setLayout(new BoxLayout(actions, BoxLayout.X_AXIS));
        actions.setBackground(Color.WHITE);
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton likeBtn = new JButton("Like (" + post.getLikes() + ")");
        JButton commentBtn = new JButton("Comment (" + post.getCommentCount() + ")");

        likeBtn.setFocusable(false);
        commentBtn.setFocusable(false);
        likeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        commentBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        likeBtn.addActionListener(e -> {
            postController.likePost(post.getId()); // DB increment
            post.setLikes(post.getLikes() + 1);     // UI update
            likeBtn.setText("Like (" + post.getLikes() + ")");
            notificationController.notifyLike(post.getUserId(), post.getId());
        });

        commentBtn.addActionListener(e -> {
            NavigatorView.showComments(post.getId());
        });

        actions.add(Box.createHorizontalStrut(48)); // align with content
        actions.add(likeBtn);
        actions.add(Box.createHorizontalStrut(15));
        actions.add(commentBtn);

        actions.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, actions.getPreferredSize().height)
        );

        card.add(actions);

        card.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height)
        );

        return card;
    }

    public void scrollToTop() {
        SwingUtilities.invokeLater(() -> {
            if (scrollPane.getViewport() != null) {
                scrollPane.getViewport().setViewPosition(new Point(0, 0));
            }
        });
    }

    // ================= TIME =================
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
