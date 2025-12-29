package konnekt.component;

import konnekt.controller.NotificationController;
import konnekt.model.dao.FollowDao;
import konnekt.model.dao.PostDao;
import konnekt.model.dao.UserDao;
import konnekt.model.pojo.PostPojo;
import konnekt.model.pojo.UserPojo;
import konnekt.view.NavigatorView;
import konnekt.utils.AvatarUtil;

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
    private final NotificationController notificationController = new NotificationController();

    private JPanel postsContainer;
    private JPanel headerPanel;
    private JButton followBtn;
    private JLabel followInfoLabel;

    private static final Font FONT = new Font("Verdana", Font.PLAIN, 13);
    private static final Font NAME_FONT = new Font("Verdana", Font.BOLD, 16);

    private static final ImageIcon DEFAULT_PROFILE;
    private static final ImageIcon DEFAULT_BANNER;

    static {
        URL avatarUrl = ProfilePanel.class.getClassLoader()
                .getResource("konnekt/resources/images/default_profile-2.png");
        DEFAULT_PROFILE = (avatarUrl != null)
                ? new ImageIcon(new ImageIcon(avatarUrl).getImage()
                        .getScaledInstance(80, 80, Image.SCALE_SMOOTH))
                : new ImageIcon();

        URL bannerUrl = ProfilePanel.class.getClassLoader()
                .getResource("konnekt/resources/images/default_banner-2.jpg");
        DEFAULT_BANNER = (bannerUrl != null)
                ? new ImageIcon(new ImageIcon(bannerUrl).getImage()
                        .getScaledInstance(685, 170, Image.SCALE_SMOOTH))
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

        initHeader();
        refreshPosts();
    }

    public int getProfileUserId() {
        return profileUserId;
    }

    // ---------------- HEADER ----------------
    private void initHeader() {
        headerPanel.removeAll();
        headerPanel.add(createHeaderPanel(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);

        JLabel banner = new JLabel(DEFAULT_BANNER);
        banner.setOpaque(true);
        header.add(banner, BorderLayout.NORTH);

        JPanel info = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        info.setBackground(new Color(250, 250, 250));
        info.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel avatar = new JLabel(DEFAULT_PROFILE);
        info.add(avatar);

        UserPojo user = userDao.getUserById(profileUserId);

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBackground(info.getBackground());

        JLabel fullName = new JLabel(user.getFullName());
        fullName.setFont(NAME_FONT);

        JLabel username = new JLabel("@" + user.getUsername());
        username.setForeground(Color.BLUE);
        username.setFont(new Font("Verdana", Font.PLAIN, 13));
        username.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        username.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                NavigatorView.showProfile(profileUserId);
            }
        });

        followInfoLabel = new JLabel(
                followDao.getFollowingCount(profileUserId) + " Following • "
                + followDao.getFollowersCount(profileUserId) + " Followers"
        );

        text.add(fullName);
        text.add(username);
        text.add(Box.createVerticalStrut(5));
        text.add(followInfoLabel);

        info.add(text);

        if (loggedInUserId != profileUserId) {
            followBtn = new JButton(
                    followDao.isFollowing(loggedInUserId, profileUserId)
                    ? "Following" : "Follow"
            );
            followBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
            notificationController.notifyFollow(profileUserId);
        }

        followBtn.setText(followDao.isFollowing(loggedInUserId, profileUserId) ? "Following" : "Follow");
        followInfoLabel.setText(
                followDao.getFollowingCount(profileUserId) + " Following • "
                + followDao.getFollowersCount(profileUserId) + " Followers"
        );
    }

    // ---------------- POSTS ----------------
    public void refreshPosts() {
        postsContainer.removeAll();

        List<PostPojo> posts = postDao.getPostsByUser(profileUserId);

        if (posts.isEmpty()) {
            String msgText = (loggedInUserId == profileUserId)
                    ? "You haven't posted anything yet."
                    : "This user hasn't posted anything yet.";

            JLabel noPosts = new JLabel(msgText);
            noPosts.setFont(FONT);
            noPosts.setForeground(Color.GRAY);
            noPosts.setAlignmentX(Component.CENTER_ALIGNMENT);
            postsContainer.add(Box.createVerticalStrut(20));
            postsContainer.add(noPosts);
            postsContainer.add(Box.createVerticalStrut(20));
        } else {
            for (PostPojo post : posts) {
                postsContainer.add(createPostCard(post));
                postsContainer.add(Box.createVerticalStrut(10));
            }
        }

        postsContainer.revalidate();
        postsContainer.repaint();

        // Scroll to top
        SwingUtilities.invokeLater(() -> {
            JScrollPane scrollPane = (JScrollPane) this.getComponent(1);
            scrollPane.getVerticalScrollBar().setValue(0);
        });
    }

    private JPanel createPostCard(PostPojo post) {
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBackground(Color.WHITE);
        root.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Header: avatar + name + username + time
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        header.setBackground(Color.WHITE);

        JLabel avatar = AvatarUtil.avatar(40);
        header.add(avatar);

        JLabel name = new JLabel(
                "<html><b>" + post.getFullName() + "</b> "
                + "<font color='blue'>@" + post.getUsername() + "</font> "
                + "<font color='gray'>· " + timeAgo(post.getCreatedAt()) + "</font></html>"
        );
        name.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        name.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                NavigatorView.showProfile(post.getUserId());
            }
        });

        header.add(name);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(header);
        root.add(Box.createVerticalStrut(6));

        // --- Content ---
        JLabel body = new JLabel("<html>" + post.getContent() + "</html>");
        body.setFont(FONT);
        body.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(body);
        root.add(Box.createVerticalStrut(6));

        // --- Actions ---
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actions.setBackground(Color.WHITE);

        JButton likeBtn = new JButton("Like (" + post.getLikes() + ")");
        JButton commentBtn = new JButton("Comment (" + post.getCommentCount() + ")");
        likeBtn.setFocusable(false);
        commentBtn.setFocusable(false);
        likeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        commentBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        likeBtn.addActionListener(e -> {
            postDao.incrementLike(post.getId());
            post.setLikes(post.getLikes() + 1);
            likeBtn.setText("Like (" + post.getLikes() + ")");

            notificationController.notifyLike(post.getUserId(), post.getId());
        });

        commentBtn.addActionListener(e -> NavigatorView.showComments(post.getId()));

        actions.add(likeBtn);
        actions.add(Box.createHorizontalStrut(10));
        actions.add(commentBtn);

        if (loggedInUserId == post.getUserId()) {
            JButton deleteBtn = new JButton("Delete");
            deleteBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            deleteBtn.addActionListener(e -> {
                postDao.deletePost(post.getId());
                refreshPosts();
            });
            actions.add(Box.createHorizontalStrut(10));
            actions.add(deleteBtn);
        }

        actions.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(actions);

        // Fit-content height
        root.setMaximumSize(new Dimension(Integer.MAX_VALUE, root.getPreferredSize().height));

        return root;
    }

    public void onShow() {
        refreshPosts();
    }

    private String timeAgo(java.sql.Timestamp ts) {
        if (ts == null) {
            return "";
        }
        long diff = System.currentTimeMillis() - ts.getTime();
        long minutes = diff / (1000 * 60);
        if (minutes < 1) {
            return "Just Now";
        }
        if (minutes < 60) {
            return minutes + "m";
        }
        long hours = minutes / 60;
        if (hours < 24) {
            return hours + "h";
        }
        long days = hours / 24;
        return days + "d";
    }
}
