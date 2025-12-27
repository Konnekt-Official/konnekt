package konnekt.component;

import konnekt.controller.PostController;
import konnekt.model.dao.PostDao;
import konnekt.model.pojo.PostPojo;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class FeedPanel extends JPanel {

    private final PostDao postDao;
    private final PostController postController;

    public FeedPanel() {
        this.postDao = new PostDao();
        this.postController = new PostController();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        refreshFeed();
    }

    public void refreshFeed() {
        removeAll();
        List<PostPojo> posts = postDao.getAllPosts();

        for (PostPojo post : posts) {
            add(createPostCard(post));
        }

        revalidate();
        repaint();
    }

    private JPanel createPostCard(PostPojo post) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.setMaximumSize(new Dimension(600, 300));
        panel.setBackground(Color.WHITE);

        // ----------------------------
        // Profile picture + username
        // ----------------------------
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel picLabel = new JLabel();
        JLabel nameLabel = new JLabel(post.getUsername() != null ? post.getUsername() : "Unknown");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Load profile picture safely
        ImageIcon profileIcon = loadImage(post.getProfilePictureUrl(), "/images/default-profile.png", 50, 50);
        picLabel.setIcon(profileIcon);

        topPanel.add(picLabel);
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(nameLabel);

        // ----------------------------
        // Content
        // ----------------------------
        JTextArea contentArea = new JTextArea(post.getContent() != null ? post.getContent() : "");
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setBackground(Color.WHITE);
        contentArea.setFont(new Font("Arial", Font.PLAIN, 14));

        // ----------------------------
        // Optional post image
        // ----------------------------
        JLabel postImageLabel = new JLabel();
        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
            ImageIcon postIcon = loadImage(post.getImageUrl(), null, 500, 200);
            if (postIcon != null) {
                postImageLabel.setIcon(postIcon);
            }
        }

        // ----------------------------
        // Likes
        // ----------------------------
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton likeButton = new JButton("Like (" + post.getLikes() + ")");
        likeButton.addActionListener(e -> {
            postController.likePost(post.getId());
            refreshFeed();
        });
        bottomPanel.add(likeButton);

        // ----------------------------
        // Add components to panel
        // ----------------------------
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(contentArea, BorderLayout.CENTER);
        if (postImageLabel.getIcon() != null) panel.add(postImageLabel, BorderLayout.SOUTH);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Load an image from resources. If not found, use fallback (can be null).
     */
    private ImageIcon loadImage(String path, String fallbackPath, int width, int height) {
        URL url = null;

        if (path != null) {
            url = getClass().getResource(path);
        }
        if (url == null && fallbackPath != null) {
            url = getClass().getResource(fallbackPath);
        }
        if (url != null) {
            Image img = new ImageIcon(url).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        return null; // if nothing found
    }
}