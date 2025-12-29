package konnekt.component;

import konnekt.model.dao.*;
import konnekt.model.pojo.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class ProfilePanel extends JPanel {

    private final int profileUserId;
    private final int loggedInUserId;

    private final PostDao postDao = new PostDao();
    private final FollowDao followDao = new FollowDao();
    private final UserDao userDao = new UserDao();

    private JPanel postsContainer;
    private JPanel headerPanel;
    private JButton followBtn;

    private static final Font FONT = new Font("Verdana", Font.PLAIN, 13);
    private static final Font NAME_FONT = new Font("Verdana", Font.BOLD, 16);

    private static final ImageIcon DEFAULT_PROFILE;

    static {
        URL url = ProfilePanel.class.getClassLoader()
                .getResource("konnekt/resources/images/default_profile.png");
        DEFAULT_PROFILE = (url != null)
                ? new ImageIcon(new ImageIcon(url).getImage()
                .getScaledInstance(80, 80, Image.SCALE_SMOOTH))
                : new ImageIcon();
    }

    public ProfilePanel(int loggedInUserId, int profileUserId) {
        this.loggedInUserId = loggedInUserId;
        this.profileUserId = profileUserId;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        headerPanel = new JPanel(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);

        postsContainer = new JPanel();
        postsContainer.setLayout(new BoxLayout(postsContainer, BoxLayout.Y_AXIS));
        postsContainer.setBackground(new Color(245, 245, 245));

        JScrollPane scrollPane = new JScrollPane(postsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        refresh();
    }

    private void refresh() {
        headerPanel.removeAll();
        headerPanel.add(createHeaderPanel(), BorderLayout.CENTER);

        refreshPosts();

        revalidate();
        repaint();
    }

    // ---------------- HEADER ----------------
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);

        JLabel banner = new JLabel();
        banner.setPreferredSize(new Dimension(800, 150));
        banner.setOpaque(true);
        banner.setBackground(Color.LIGHT_GRAY);
        header.add(banner, BorderLayout.NORTH);

        JPanel info = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        info.setBackground(Color.WHITE);
        info.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel avatar = new JLabel(DEFAULT_PROFILE);
        info.add(avatar);

        UserPojo user = userDao.getUserById(profileUserId);

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBackground(Color.WHITE);

        JLabel fullName = new JLabel(user.getFullName());
        fullName.setFont(NAME_FONT);

        JLabel username = new JLabel("@" + user.getUsername());
        username.setForeground(Color.BLUE);

        JLabel followInfo = new JLabel(
                followDao.getFollowingCount(profileUserId) + " Following • " +
                followDao.getFollowersCount(profileUserId) + " Followers"
        );

        text.add(fullName);
        text.add(username);
        text.add(Box.createVerticalStrut(5));
        text.add(followInfo);

        info.add(text);

        if (loggedInUserId != profileUserId) {
            followBtn = new JButton(
                    followDao.isFollowing(loggedInUserId, profileUserId)
                            ? "Following" : "Follow"
            );
            followBtn.addActionListener(e -> toggleFollow());
            info.add(followBtn);
        }

        header.add(info, BorderLayout.CENTER);
        return header;
    }

    private void toggleFollow() {
        if (followDao.isFollowing(loggedInUserId, profileUserId)) {
            followDao.unfollowUser(loggedInUserId, profileUserId);
        } else {
            followDao.followUser(loggedInUserId, profileUserId);
        }
        refresh();
    }

    // ---------------- POSTS ----------------
    private void refreshPosts() {
        postsContainer.removeAll();

        List<PostPojo> posts = postDao.getPostsByUser(profileUserId);
        for (PostPojo post : posts) {
            postsContainer.add(createPostCard(post));
            postsContainer.add(Box.createVerticalStrut(10));
        }

        postsContainer.revalidate();
        postsContainer.repaint();
    }

    private JPanel createPostCard(PostPojo post) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextArea content = new JTextArea(post.getContent());
        content.setFont(FONT);
        content.setLineWrap(true);
        content.setWrapStyleWord(true);
        content.setEditable(false);
        content.setOpaque(false);

        card.add(content, BorderLayout.CENTER);

        if (loggedInUserId == profileUserId) {
            JButton delete = new JButton("Delete");
            delete.addActionListener(e -> {
                postDao.deletePost(post.getId());
                refreshPosts();
            });
            card.add(delete, BorderLayout.EAST);
        }

        return card;
    }
}
