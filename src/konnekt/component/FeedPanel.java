package konnekt.component;

import konnekt.controller.PostController;
import konnekt.model.dao.PostDao;
import konnekt.model.pojo.PostPojo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FeedPanel extends JPanel {

    private final PostDao postDao;
    private final PostController postController;
    private final JPanel postsContainer;
    private final JScrollPane scrollPane;

    public FeedPanel() {
        this.postDao = new PostDao();
        this.postController = new PostController();

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // ========================
        // Top input panel
        // ========================
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(Color.WHITE);

        JTextField contentField = new JTextField();
        JButton postBtn = new JButton("Post");
        postBtn.setBackground(new Color(30, 144, 255));
        postBtn.setForeground(Color.WHITE);
        postBtn.setFocusPainted(false);

        inputPanel.add(contentField, BorderLayout.CENTER);
        inputPanel.add(postBtn, BorderLayout.EAST);
        add(inputPanel, BorderLayout.NORTH);

        postBtn.addActionListener(e -> {
            String content = contentField.getText().trim();
            if (!content.isEmpty()) {
                postController.createPost(this, content); // image null for now
                contentField.setText("");
                refreshFeed();
            }
        });

        // ========================
        // Posts container (scrollable)
        // ========================
        postsContainer = new JPanel();
        postsContainer.setLayout(new BoxLayout(postsContainer, BoxLayout.Y_AXIS));
        postsContainer.setBackground(new Color(245, 245, 245));

        scrollPane = new JScrollPane(postsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        refreshFeed();
    }

    // ========================
    // Refresh posts
    // ========================
    public void refreshFeed() {
        postsContainer.removeAll();
        List<PostPojo> posts = postDao.getAllPosts();
        for (PostPojo post : posts) {
            postsContainer.add(createPostCard(post));
            postsContainer.add(Box.createRigidArea(new Dimension(0, 10))); // spacing
        }
        postsContainer.revalidate();
        postsContainer.repaint();
    }

    // ========================
    // Create post card
    // ========================
    private JPanel createPostCard(PostPojo post) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // ------------------------
        // Top row: profile + name + username + time
        // ------------------------
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topPanel.setBackground(Color.WHITE);

        // Profile image
        JLabel profileLabel = new JLabel();
        ImageIcon profileIcon = new ImageIcon(getClass().getResource("/konnekt/resources/images/default_profile.png"));
        Image scaledProfile = profileIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        profileLabel.setIcon(new ImageIcon(scaledProfile));
        topPanel.add(profileLabel);

        // Name + username + time
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        namePanel.setBackground(Color.WHITE);

        JLabel fullNameLabel = new JLabel(post.getFullName());
        fullNameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel usernameLabel = new JLabel("@" + post.getUsername());
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        usernameLabel.setForeground(Color.BLUE);
        usernameLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        usernameLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(namePanel, "Redirect to @" + post.getUsername() + " profile");
            }
        });

        JLabel timeLabel = new JLabel(" · " + getTimeAgo(post.getCreatedAt()));
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        timeLabel.setForeground(Color.GRAY);

        namePanel.add(fullNameLabel);
        namePanel.add(usernameLabel);
        namePanel.add(timeLabel);

        topPanel.add(namePanel);
        card.add(topPanel);

        // ------------------------
        // Second row: content
        // ------------------------
        JTextArea contentArea = new JTextArea(post.getContent());
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setFont(new Font("Arial", Font.PLAIN, 13));
        contentArea.setBackground(Color.WHITE);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(5, 60, 5, 10)); // aligns after profile
        contentPanel.add(contentArea, BorderLayout.CENTER);
        card.add(contentPanel);

        // ------------------------
        // Third row: actions
        // ------------------------
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setBackground(Color.WHITE);
        actions.setBorder(new EmptyBorder(0, 60, 0, 0)); // align with content

        JButton likeBtn = new JButton("Like (" + post.getLikes() + ")");
        likeBtn.setFocusPainted(false);
        likeBtn.setBackground(new Color(30, 144, 255));
        likeBtn.setForeground(Color.WHITE);
        likeBtn.addActionListener(e -> {
            postController.likePost(post.getId());
            refreshFeed();
        });

        JButton commentBtn = new JButton("Comment");
        commentBtn.setFocusPainted(false);
        commentBtn.setBackground(new Color(100, 149, 237));
        commentBtn.setForeground(Color.WHITE);
        commentBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(actions, "Redirect to post comments");
        });

        actions.add(likeBtn);
        actions.add(commentBtn);
        card.add(actions);

        return card;
    }

    // ========================
    // Format time as "1h ago", "5m ago"
    // ========================
    private String getTimeAgo(java.sql.Timestamp createdAt) {
        if (createdAt == null) return "";
        long diff = System.currentTimeMillis() - createdAt.getTime();
        long minutes = diff / (1000 * 60);
        if (minutes < 60) return minutes + "m ago";
        long hours = minutes / 60;
        if (hours < 24) return hours + "h ago";
        long days = hours / 24;
        return days + "d ago";
    }
}
