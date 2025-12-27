package konnekt.component;

import konnekt.controller.PostController;
import konnekt.model.dao.PostDao;
import konnekt.model.pojo.PostPojo;

import javax.swing.*;
import java.awt.*;
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

        // Profile picture + username
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ImageIcon icon;
        try {
            icon = new ImageIcon(getClass().getResource(post.getProfilePictureUrl()));
        } catch (Exception e) {
            icon = new ImageIcon(getClass().getResource("/images/default-profile.png"));
        }
        Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        JLabel picLabel = new JLabel(new ImageIcon(img));
        JLabel nameLabel = new JLabel(post.getUsername());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(picLabel);
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(nameLabel);

        // Content
        JTextArea contentArea = new JTextArea(post.getContent());
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setBackground(Color.WHITE);
        contentArea.setFont(new Font("Arial", Font.PLAIN, 14));

        // Optional image
        JLabel postImageLabel = new JLabel();
        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
            ImageIcon postIcon;
            try {
                postIcon = new ImageIcon(getClass().getResource(post.getImageUrl()));
                Image imgPost = postIcon.getImage().getScaledInstance(500, 200, Image.SCALE_SMOOTH);
                postImageLabel.setIcon(new ImageIcon(imgPost));
            } catch (Exception e) {
                // ignore if image not found
            }
        }

        // Likes
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton likeButton = new JButton("Like (" + post.getLikes() + ")");
        likeButton.addActionListener(e -> {
            postController.likePost(post.getId());
            refreshFeed();
        });
        bottomPanel.add(likeButton);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(contentArea, BorderLayout.CENTER);
        if (postImageLabel.getIcon() != null) panel.add(postImageLabel, BorderLayout.SOUTH);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }
}