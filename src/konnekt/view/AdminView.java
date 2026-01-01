package konnekt.view;

import konnekt.model.dao.PostDao;
import konnekt.model.dao.UserDao;
import konnekt.model.pojo.PostPojo;
import konnekt.model.pojo.UserPojo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class AdminView extends BaseFrame {

    private final UserDao userDao = new UserDao();
    private final PostDao postDao = new PostDao();

    private static final String DEFAULT_AVATAR =
            "/konnekt/resources/images/default_profile.png";

    public AdminView() {
        setTitle("Admin Panel");
        setLayout(new BorderLayout());
        setSize(1000, 650);
        setLocationRelativeTo(null);

        add(createTabs(), BorderLayout.CENTER);

        setVisible(true);
    }

    // =========================
    // TABS
    // =========================
    private JTabbedPane createTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Verdana", Font.BOLD, 13));

        tabs.add("Analytics", createAnalyticsTab());
        tabs.add("Users", createUsersTab());
        tabs.add("Posts", createPostsTab());

        return tabs;
    }

    // =========================
    // ANALYTICS TAB (basic)
    // =========================
    private JPanel createAnalyticsTab() {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel users = new JLabel("Total Users: " + userDao.getAllUsers().size());
        JLabel posts = new JLabel("Total Posts: " + postDao.getAllPosts().size());

        users.setFont(new Font("Verdana", Font.BOLD, 16));
        posts.setFont(new Font("Verdana", Font.BOLD, 16));

        panel.add(users);
        panel.add(Box.createVerticalStrut(10));
        panel.add(posts);

        return panel;
    }

    // =========================
    // USERS TAB
    // =========================
    private JPanel createUsersTab() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);

        List<UserPojo> users = userDao.getAllUsers();

        for (UserPojo user : users) {
            container.add(createUserRow(user));
            container.add(Box.createVerticalStrut(8));
        }

        JScrollPane scroll = new JScrollPane(container);
        scroll.setBorder(null);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(scroll, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel createUserRow(UserPojo user) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBorder(new EmptyBorder(10, 10, 10, 10));
        row.setBackground(new Color(245, 245, 245));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        // ---------- AVATAR ----------
        JLabel avatar = new JLabel(loadAvatar(42));
        row.add(avatar, BorderLayout.WEST);

        // ---------- NAME ----------
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);

        JLabel fullName = new JLabel(user.getFullName());
        fullName.setFont(new Font("Verdana", Font.BOLD, 14));

        JLabel username = new JLabel("@" + user.getUsername());
        username.setFont(new Font("Verdana", Font.PLAIN, 12));
        username.setForeground(Color.GRAY);

        info.add(fullName);
        info.add(username);

        row.add(info, BorderLayout.CENTER);

        // ---------- DELETE ----------
        JButton delete = new JButton("Delete");
        delete.setFocusPainted(false);
        delete.setBackground(new Color(220, 53, 69));
        delete.setForeground(Color.WHITE);

        delete.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Delete user " + user.getUsername() + "?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                // you probably already have cascade delete
                userDao.deleteUser(user.getId());
                refresh();
            }
        });

        row.add(delete, BorderLayout.EAST);

        return row;
    }

    // =========================
    // POSTS TAB
    // =========================
    private JPanel createPostsTab() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);

        List<PostPojo> posts = postDao.getAllPosts();

        for (PostPojo post : posts) {
            container.add(createPostRow(post));
            container.add(Box.createVerticalStrut(8));
        }

        JScrollPane scroll = new JScrollPane(container);
        scroll.setBorder(null);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(scroll, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel createPostRow(PostPojo post) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBorder(new EmptyBorder(10, 10, 10, 10));
        row.setBackground(new Color(245, 245, 245));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel content = new JLabel("<html>" + post.getContent() + "</html>");
        content.setFont(new Font("Verdana", Font.PLAIN, 13));

        JButton delete = new JButton("Delete");
        delete.setBackground(new Color(220, 53, 69));
        delete.setForeground(Color.WHITE);
        delete.setFocusPainted(false);

        delete.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Delete this post?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                postDao.deletePost(post.getId());
                refresh();
            }
        });

        row.add(content, BorderLayout.CENTER);
        row.add(delete, BorderLayout.EAST);

        return row;
    }

    // =========================
    // UTIL
    // =========================
    private ImageIcon loadAvatar(int size) {
        ImageIcon icon = new ImageIcon(
                getClass().getResource(DEFAULT_AVATAR)
        );
        Image img = icon.getImage().getScaledInstance(
                size, size, Image.SCALE_SMOOTH
        );
        return new ImageIcon(img);
    }

    private void refresh() {
        dispose();
        new AdminView();
    }
}
