package konnekt.component;

import konnekt.model.dao.PostDao;
import konnekt.model.dao.UserDao;
import konnekt.model.pojo.PostPojo;
import konnekt.model.pojo.UserPojo;
import konnekt.view.NavigatorView;
import konnekt.utils.AvatarUtil;
import konnekt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class SearchPanel extends JPanel {

    private final JTextField searchInput = new JTextField();
    private final JTabbedPane tabs = new JTabbedPane();

    private final JPanel userList = new JPanel();
    private final JPanel postList = new JPanel();

    private final UserDao userDao = new UserDao();
    private final PostDao postDao = new PostDao();

    public SearchPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        add(createSearchBar(), BorderLayout.NORTH);
        add(createTabs(), BorderLayout.CENTER);
    }

    // ---------- SEARCH BAR ----------
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        // Set placeholder text
        searchInput.setText("Search users or posts...");
        searchInput.setForeground(Color.GRAY);

        searchInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchInput.getText().equals("Search users or posts...")) {
                    searchInput.setText("");
                    searchInput.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchInput.getText().isEmpty()) {
                    searchInput.setText("Search users or posts...");
                    searchInput.setForeground(Color.GRAY);
                }
            }
        });

        // Increase height
        searchInput.setPreferredSize(new Dimension(200, 35));
        searchInput.setFont(new Font("Verdana", Font.PLAIN, 14));
        searchInput.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        searchInput.addActionListener(e -> search());

        panel.add(searchInput, BorderLayout.CENTER);
        return panel;
    }

    // ---------- TABS ----------
    private JTabbedPane createTabs() {
        userList.setLayout(new BoxLayout(userList, BoxLayout.Y_AXIS));
        userList.setAlignmentY(Component.TOP_ALIGNMENT);
        userList.setAlignmentX(Component.LEFT_ALIGNMENT);

        postList.setLayout(new BoxLayout(postList, BoxLayout.Y_AXIS));
        postList.setAlignmentY(Component.TOP_ALIGNMENT);
        postList.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane userScroll = new JScrollPane(userList);
        userScroll.setBorder(BorderFactory.createEmptyBorder());
        userScroll.getVerticalScrollBar().setUnitIncrement(16);

        JScrollPane postScroll = new JScrollPane(postList);
        postScroll.setBorder(BorderFactory.createEmptyBorder());
        postScroll.getVerticalScrollBar().setUnitIncrement(16);

        tabs.add("Users", userScroll);
        tabs.add("Posts", postScroll);

        return tabs;
    }

    // ---------- SEARCH LOGIC ----------
    private void search() {
        String keyword = searchInput.getText().trim();
        if (keyword.isEmpty() || keyword.equals("Search Users or Posts...")) {
            return;
        }

        loadUsers(keyword);
        loadPosts(keyword);
    }

    // ---------- USERS ----------
    private void loadUsers(String keyword) {
        userList.removeAll();

        List<UserPojo> users = userDao.searchUsers(keyword);

        if (users.isEmpty()) {
            JLabel msg = new JLabel("No users found.");
            msg.setForeground(Color.GRAY);
            msg.setFont(new Font("Verdana", Font.PLAIN, 12));
            msg.setAlignmentX(Component.CENTER_ALIGNMENT);
            userList.add(Box.createVerticalStrut(20));
            userList.add(msg);
        } else {
            for (UserPojo u : users) {
                userList.add(createUserItem(u));
                userList.add(Box.createVerticalStrut(5));
            }
        }

        refresh(userList);
    }

    private JPanel createUserItem(UserPojo u) {
        JPanel root = new JPanel(new BorderLayout(10, 0));
        root.setBackground(Color.WHITE);
        root.setBorder(new EmptyBorder(8, 8, 8, 8));
        root.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // avatar
        JLabel avatar = AvatarUtil.avatar(20);
        root.add(avatar, BorderLayout.WEST);

        // user info
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);

        JLabel name = new JLabel(
                "<html><b>" + u.getFullName() + "</b> <span style='font-weight:normal; color:blue'>@" + u.getUsername() + "</span></html>"
        );
        name.setAlignmentX(Component.LEFT_ALIGNMENT);
        info.add(name);

        root.add(info, BorderLayout.CENTER);

        root.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                NavigatorView.showProfile(u.getId());
            }
        });

        root.setMaximumSize(new Dimension(Integer.MAX_VALUE, root.getPreferredSize().height));
        return root;
    }

    // ---------- POSTS ----------
    private void loadPosts(String keyword) {
        postList.removeAll();

        List<PostPojo> posts = postDao.searchPosts(keyword);

        if (posts.isEmpty()) {
            JLabel msg = new JLabel("No posts found.");
            msg.setForeground(Color.GRAY);
            msg.setFont(new Font("Verdana", Font.PLAIN, 12));
            msg.setAlignmentX(Component.CENTER_ALIGNMENT);
            postList.add(Box.createVerticalStrut(20));
            postList.add(msg);
        } else {
            for (PostPojo p : posts) {
                postList.add(createPostItem(p));
                postList.add(Box.createVerticalStrut(5));
            }
        }

        refresh(postList);
    }

    private JPanel createPostItem(PostPojo post) {
        JPanel root = new JPanel(new BorderLayout(10, 0));
        root.setBackground(Color.WHITE);
        root.setBorder(new EmptyBorder(8, 8, 8, 8));
        root.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel avatar = AvatarUtil.avatar(40);
        root.add(avatar, BorderLayout.WEST);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setAlignmentY(Component.TOP_ALIGNMENT);

        JLabel header = new JLabel(
                "<html><b>" + post.getFullName() + "</b> <span style='font-weight:normal; color:blue'>@" + post.getUsername() + "</span></html>"
        );

        JLabel body = new JLabel("<html>" + post.getContent() + "</html>");
        body.setFont(new Font("Verdana", Font.PLAIN, 13));

        JLabel meta = new JLabel("Like (" + post.getLikes() + ")   Comment (" + post.getCommentCount() + ")");
        meta.setForeground(Color.GRAY);
        meta.setFont(new Font("Verdana", Font.PLAIN, 11));

        content.add(header);
        content.add(body);
        content.add(Box.createVerticalStrut(5));
        content.add(meta);

        root.add(content, BorderLayout.CENTER);

        root.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                NavigatorView.showComments(post.getId());
            }
        });

        root.setMaximumSize(new Dimension(Integer.MAX_VALUE, root.getPreferredSize().height));
        return root;
    }

    // ---------- REFRESH ----------
    private void refresh(JPanel panel) {
        panel.revalidate();
        panel.repaint();
    }
}