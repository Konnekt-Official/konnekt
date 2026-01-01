package konnekt.view;

import konnekt.component.GraphPanel;
import konnekt.model.dao.PostDao;
import konnekt.model.dao.UserDao;
import konnekt.model.pojo.PostPojo;
import konnekt.model.pojo.UserPojo;
import konnekt.utils.AvatarUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;

public class AdminView extends BaseFrame {

    private final UserDao userDao = new UserDao();
    private final PostDao postDao = new PostDao();

    public AdminView() {
        setTitle("Admin Panel");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createTabs(), BorderLayout.CENTER);
        setVisible(true);
    }

    private JTabbedPane createTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Analytics", createAnalyticsTab());
        tabs.add("Users", createUsersTab());
        tabs.add("Posts", createPostsTab());
        return tabs;
    }

    // ================= ANALYTICS =================
    private JPanel createAnalyticsTab() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JComboBox<String> selector = new JComboBox<>(new String[]{"Users", "Posts"});
        GraphPanel graph = new GraphPanel(userDao.getUserCount());

        selector.addActionListener(e -> {
            if (selector.getSelectedIndex() == 0) {
                graph.setData(userDao.getUserCount());
            } else {
                graph.setData(postDao.getPostCount());
            }
        });

        panel.add(selector, BorderLayout.NORTH);
        panel.add(graph, BorderLayout.CENTER);

        return panel;
    }

    // ================= USERS =================
    private JComponent createUsersTab() {
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        for (UserPojo u : userDao.getAllUsers()) {
            JPanel row = new JPanel(new BorderLayout(10, 0));
            row.setBorder(new EmptyBorder(10, 10, 10, 10));
            row.setBackground(new Color(245, 245, 245));

            row.add(AvatarUtil.avatar(40), BorderLayout.WEST);

            JLabel name = new JLabel(u.getFullName() + "  @" + u.getUsername());
            name.setFont(new Font("Verdana", Font.BOLD, 14));
            row.add(name, BorderLayout.CENTER);

            JButton del = new JButton("Delete");
            del.addActionListener(e -> {
                userDao.deleteUser(u.getId());
                refresh();
            });
            row.add(del, BorderLayout.EAST);

            list.add(row);
            list.add(Box.createVerticalStrut(8));
        }

        return new JScrollPane(list);
    }

    // ================= POSTS =================
    private JComponent createPostsTab() {
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        for (PostPojo p : postDao.getAllPosts()) {
            list.add(createPostRow(p));
            list.add(Box.createVerticalStrut(10));
        }

        return new JScrollPane(list);
    }

    private JPanel createPostRow(PostPojo post) {
        UserPojo user = userDao.getUserById(post.getUserId());

        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBorder(new EmptyBorder(10, 10, 10, 10));
        row.setBackground(new Color(245, 245, 245));

        // LEFT
        row.add(AvatarUtil.avatar(42), BorderLayout.WEST);

        // CENTER
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        header.setOpaque(false);

        JLabel name = new JLabel(user.getFullName());
        name.setFont(new Font("Verdana", Font.BOLD, 14));

        JLabel username = new JLabel("@" + user.getUsername());
        username.setForeground(Color.GRAY);

        JLabel time = new JLabel("· " + timeAgo(post.getCreatedAt()));
        time.setForeground(Color.GRAY);

        header.add(name);
        header.add(username);
        header.add(time);

        JLabel content = new JLabel("<html>" + post.getContent() + "</html>");

        JLabel stats = new JLabel("❤️ " + post.getLikes() + "   💬 " + post.getCommentCount());
        stats.setForeground(Color.GRAY);

        center.add(header);
        center.add(Box.createVerticalStrut(5));
        center.add(content);
        center.add(Box.createVerticalStrut(5));
        center.add(stats);

        row.add(center, BorderLayout.CENTER);

        JButton del = new JButton("Delete");
        del.addActionListener(e -> {
            postDao.deletePost(post.getId());
            refresh();
        });

        row.add(del, BorderLayout.EAST);
        return row;
    }

    private String timeAgo(Timestamp ts) {
        Duration d = Duration.between(ts.toInstant(), Instant.now());
        if (d.toMinutes() < 60) return d.toMinutes() + "m";
        if (d.toHours() < 24) return d.toHours() + "h";
        return d.toDays() + "d";
    }

    private void refresh() {
        dispose();
        new AdminView();
    }
}
