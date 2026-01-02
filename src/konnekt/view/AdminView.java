package konnekt.view;

import konnekt.component.GraphPanel;
import konnekt.model.dao.PostDao;
import konnekt.model.dao.UserDao;
import konnekt.model.pojo.PostPojo;
import konnekt.model.pojo.UserPojo;
import konnekt.utils.AvatarUtil;
import konnekt.utils.TimeAgo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import konnekt.utils.DateRangeUtil;

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

        tabs.addTab("Analytics", createAnalyticsTab());
        tabs.addTab("Users", createUsersTab());
        tabs.addTab("Posts", createPostsTab());
        tabs.addTab("Logout", new JPanel()); // handled by change listener

        tabs.addChangeListener(e -> {
            if (tabs.getSelectedIndex() == 3) {
                dispose();
                new LoginView().setVisible(true);
            }
        });

        return tabs;
    }

    // ================= ANALYTICS =================
    private JPanel createAnalyticsTab() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JComboBox<String> selector = new JComboBox<>(new String[]{"Users", "Posts"});

        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = DateRangeUtil.nextSaturday(start);

        GraphPanel graph = new GraphPanel(
                userDao.getUserCountBetween(start, end)
        );

        selector.addActionListener(e -> {
            if (selector.getSelectedIndex() == 0) {
                graph.setData(
                        userDao.getUserCountBetween(start, end)
                );
            } else {
                graph.setData(
                        postDao.getPostCountBetween(start, end)
                );
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

        List<UserPojo> users = userDao.getAllUsers();

        if (users.isEmpty()) {
            JLabel empty = new JLabel("No users found");
            empty.setFont(new Font("Verdana", Font.ITALIC, 14));
            list.add(empty);
        } else {
            for (UserPojo u : users) {
                list.add(createUserRow(u));
                list.add(Box.createVerticalStrut(6));
            }
        }

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        return scroll;
    }

    private JPanel createUserRow(UserPojo u) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBorder(new EmptyBorder(8, 8, 8, 8));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
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

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(del);

        row.add(right, BorderLayout.EAST);
        return row;
    }

    // ================= POSTS =================
    private JComponent createPostsTab() {
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        List<PostPojo> posts = postDao.getAllPosts();

        if (posts.isEmpty()) {
            JLabel empty = new JLabel("No posts found");
            empty.setFont(new Font("Verdana", Font.ITALIC, 14));
            list.add(empty);
        } else {
            for (PostPojo p : posts) {
                list.add(createPostRow(p));
                list.add(Box.createVerticalStrut(8));
            }
        }

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        return scroll;
    }

    private JPanel createPostRow(PostPojo post) {
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBackground(Color.WHITE);
        root.setBorder(new EmptyBorder(10, 10, 10, 10));

        // ---------- HEADER ----------
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        header.setBackground(Color.WHITE);

        JLabel avatar = AvatarUtil.avatar(40);
        header.add(avatar);

        JLabel name = new JLabel(
                "<html><b>" + post.getFullName() + "</b> "
                + "<span style='font-weight:normal; color:blue'>@" + post.getUsername() + "</span> "
                + "<span style='font-weight:normal; color:gray'>" + TimeAgo.format(post.getCreatedAt()) + "</span></html>"
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

        // ---------- CONTENT ----------
        JLabel body = new JLabel("<html>" + post.getContent() + "</html>");
        body.setFont(new Font("Verdana", Font.PLAIN, 13));
        body.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(body);
        root.add(Box.createVerticalStrut(6));

        // ---------- ACTIONS (Like/Comment labels only) ----------
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setBackground(Color.WHITE);

        JLabel likeLabel = new JLabel("Like (" + post.getLikes() + ")");
        JLabel commentLabel = new JLabel("Comment (" + post.getCommentCount() + ")");

        actions.add(likeLabel);
        actions.add(Box.createHorizontalStrut(10));
        actions.add(commentLabel);

        actions.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(actions);

        // ---------- DELETE BUTTON ----------
        JButton del = new JButton("Delete");
        del.setForeground(Color.WHITE);
        del.setBackground(Color.RED);
        del.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        del.addActionListener(e -> {
            postDao.deletePost(post.getId());
            refresh(); // refresh the Admin posts tab
        });

        JPanel deleteWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        deleteWrapper.setOpaque(false);
        deleteWrapper.add(del);
        deleteWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(deleteWrapper);

        // ---------- FIT-CONTENT HEIGHT ----------
        root.setMaximumSize(new Dimension(Integer.MAX_VALUE, root.getPreferredSize().height));

        return root;
    }

    // ================= HELPERS =================
    private void refresh() {
        dispose();
        new AdminView();
    }
}
