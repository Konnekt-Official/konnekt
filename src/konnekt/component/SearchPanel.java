package konnekt.component;

import konnekt.model.dao.PostDao;
import konnekt.model.dao.UserDao;
import konnekt.model.pojo.PostPojo;
import konnekt.model.pojo.UserPojo;
import konnekt.view.NavigatorView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
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
    private JPanel createSearchBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        searchInput.setFont(new Font("Verdana", Font.PLAIN, 14));
        searchInput.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        searchInput.addActionListener(e -> search());

        panel.add(searchInput, BorderLayout.CENTER);
        return panel;
    }

    // ---------- TABS ----------
    private JTabbedPane createTabs() {
        userList.setLayout(new BoxLayout(userList, BoxLayout.Y_AXIS));
        postList.setLayout(new BoxLayout(postList, BoxLayout.Y_AXIS));

        tabs.add("Users", new JScrollPane(userList));
        tabs.add("Posts", new JScrollPane(postList));

        return tabs;
    }

    // ---------- SEARCH LOGIC ----------
    private void search() {
        String keyword = searchInput.getText().trim();
        if (keyword.isEmpty()) return;

        loadUsers(keyword);
        loadPosts(keyword);
    }

    // ---------- USERS ----------
    private void loadUsers(String keyword) {
        userList.removeAll();

        List<UserPojo> users = userDao.searchUsers(keyword);
        for (UserPojo u : users) {
            userList.add(createUserItem(u));
            userList.add(Box.createVerticalStrut(5));
        }

        refresh(userList);
    }

    private JPanel createUserItem(UserPojo u) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(8, 8, 8, 8));
        p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel name = new JLabel("<html><b>" + u.getFullName() +
                "</b> <font color='blue'>@" + u.getUsername() + "</font></html>");

        p.add(name, BorderLayout.CENTER);

        p.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                NavigatorView.showProfile(u.getId());
            }
        });

        return p;
    }

    // ---------- POSTS ----------
    private void loadPosts(String keyword) {
        postList.removeAll();

        List<PostPojo> posts = postDao.searchPosts(keyword);
        for (PostPojo p : posts) {
            postList.add(createPostItem(p));
            postList.add(Box.createVerticalStrut(5));
        }

        refresh(postList);
    }

    private JPanel createPostItem(PostPojo post) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(8, 8, 8, 8));
        p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel content = new JLabel(
                "<html><b>" + post.getFullName() + "</b> " +
                        "<font color='gray'>@" + post.getUsername() + "</font><br>" +
                        post.getContent() +
                        "</html>"
        );

        p.add(content, BorderLayout.CENTER);

        p.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                NavigatorView.showComments(post.getId());
            }
        });

        return p;
    }

    private void refresh(JPanel panel) {
        panel.revalidate();
        panel.repaint();
    }
}