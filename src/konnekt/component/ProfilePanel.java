package konnekt.component;

import konnekt.model.dao.FollowDao;
import konnekt.model.dao.PostDao;
import konnekt.model.pojo.PostPojo;
import konnekt.model.pojo.UserPojo; // your user pojo

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ProfilePanel extends JPanel {

    private final PostDao postDao = new PostDao();
    private final FollowDao followDao = new FollowDao();

    private final JPanel postsContainer = new JPanel();
    private final JScrollPane scrollPane;
    private final int profileUserId;
    private final int loggedInUserId; // current logged-in user

    private JButton followBtn;

    private static final Font FONT = new Font("Verdana", Font.PLAIN, 13);

    public ProfilePanel(int loggedInUserId, int profileUserId) {
        this.loggedInUserId = loggedInUserId;
        this.profileUserId = profileUserId;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        add(createHeaderPanel(), BorderLayout.NORTH);

        postsContainer.setLayout(new BoxLayout(postsContainer, BoxLayout.Y_AXIS));
        postsContainer.setBackground(new Color(245, 245, 245));

        scrollPane = new JScrollPane(postsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        refreshPosts();
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Banner + Profile picture
        JLabel banner = new JLabel();
        banner.setPreferredSize(new Dimension(800, 150));
        banner.setOpaque(true);
        banner.setBackground(Color.LIGHT_GRAY); // default banner
        header.add(banner, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        infoPanel.setBackground(Color.WHITE);

        JLabel avatar = new JLabel(new ImageIcon(getClass().getResource("/konnekt/resources/images/default_profile.png")));
        avatar.setPreferredSize(new Dimension(80, 80));
        infoPanel.add(avatar);

        JPanel userInfo = new JPanel();
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));
        userInfo.setBackground(Color.WHITE);

        UserPojo user = new UserPojo(); // load user details from dao
        user.setId(profileUserId);
        user.setFullName("User Fullname"); // fetch from DAO
        user.setUsername("username");      // fetch from DAO

        JLabel fullName = new JLabel(user.getFullName());
        fullName.setFont(new Font("Verdana", Font.BOLD, 16));
        userInfo.add(fullName);

        JLabel username = new JLabel("@" + user.getUsername());
        username.setForeground(Color.BLUE);
        userInfo.add(username);

        JLabel followInfo = new JLabel(
                followDao.getFollowingCount(profileUserId) + " Following • " +
                        followDao.getFollowersCount(profileUserId) + " Followers"
        );
        userInfo.add(followInfo);

        infoPanel.add(userInfo);

        // Follow button if not same user
        if (loggedInUserId != profileUserId) {
            followBtn = new JButton(followDao.isFollowing(loggedInUserId, profileUserId) ? "Following" : "Follow");
            followBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            followBtn.addActionListener(e -> toggleFollow());
            infoPanel.add(followBtn);
        }

        header.add(infoPanel, BorderLayout.CENTER);
        return header;
    }

    private void toggleFollow() {
        if (followDao.isFollowing(loggedInUserId, profileUserId)) {
            followDao.unfollowUser(loggedInUserId, profileUserId);
            followBtn.setText("Follow");
        } else {
            followDao.followUser(loggedInUserId, profileUserId);
            followBtn.setText("Following");
        }
        refreshPosts(); // refresh follow count
    }

    private void refreshPosts() {
        postsContainer.removeAll();
        List<PostPojo> posts = postDao.getPostsByUser(profileUserId);
        for (PostPojo post : posts) {
            JPanel postCard = new JPanel(new BorderLayout());
            postCard.setBorder(new EmptyBorder(10, 10, 10, 10));
            postCard.setBackground(Color.WHITE);

            JLabel content = new JLabel("<html><body style='width: 600px;'>" + post.getContent() + "</body></html>");
            content.setFont(FONT);
            postCard.add(content, BorderLayout.CENTER);

            if (loggedInUserId == profileUserId) {
                JButton delete = new JButton("Delete");
                delete.addActionListener(e -> {
                    postDao.deletePost(post.getId());
                    refreshPosts();
                });
                postCard.add(delete, BorderLayout.EAST);
            }

            postsContainer.add(postCard);
            postsContainer.add(Box.createVerticalStrut(10));
        }

        postsContainer.revalidate();
        postsContainer.repaint();
    }
}
