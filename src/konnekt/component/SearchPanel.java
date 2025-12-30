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
import konnekt.utils.AvatarUtil;

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
        if (keyword.isEmpty()) {
            return;
        }

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

    private JPanel createPostItem(PostPojo post) {
        JPanel root = new JPanel(new BorderLayout(10, 0));
        root.setBackground(Color.WHITE);
        root.setBorder(new EmptyBorder(8, 8, 8, 8));
        root.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // avatar
        root.add(AvatarUtil.avatar(40), BorderLayout.WEST);

        // content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        JLabel header = new JLabel(
                "<html><b>" + post.getFullName()
                + "</b> <font color='gray'>@" + post.getUsername() + "</font></html>"
        );

        JLabel body = new JLabel("<html>" + post.getContent() + "</html>");

        JLabel meta = new JLabel(
                "❤️ " + post.getLikes()
                + "   💬 " + post.getCommentCount()
        );
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

        return root;
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

    private JPanel createUserItem(UserPojo u) {
        JPanel root = new JPanel(new BorderLayout(10, 0));
        root.setBackground(Color.WHITE);
        root.setBorder(new EmptyBorder(8, 8, 8, 8));
        root.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // avatar
        root.add(AvatarUtil.avatar(40), BorderLayout.WEST);

        // text
        JLabel name = new JLabel(
                "<html><b>" + u.getFullName() + "</b><br>"
                + "<font color='blue'>@" + u.getUsername() + "</font></html>"
        );

        root.add(name, BorderLayout.CENTER);

        root.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                NavigatorView.showProfile(u.getId());
            }
        });

        return root;
    }

    private void refresh(JPanel panel) {
        panel.revalidate();
        panel.repaint();
    }
}
