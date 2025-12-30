package konnekt.component;

import konnekt.model.dao.UserDao;
import konnekt.model.pojo.UserPojo;
import konnekt.view.NavigatorView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class InboxPanel extends JPanel {

    private JPanel userListPanel;
    private UserDao userDao;

    public InboxPanel() {
        userDao = new UserDao();
        initUI();
        loadUsers();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Inbox");
        title.setFont(new Font("Verdana", Font.BOLD, 18));
        title.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(userListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadUsers() {
        // Load users from DAO
        List<UserPojo> users = userDao.getAllUsers(); // make sure your UserDao has getAllUsers()
        if (users == null) return;

        for (UserPojo user : users) {
            JPanel panel = createUserItem(user);
            userListPanel.add(panel);
        }

        revalidate();
        repaint();
    }

    private JPanel createUserItem(UserPojo user) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        panel.setBackground(new Color(40, 40, 40));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Profile icon
        JLabel avatarLabel = new JLabel();
        avatarLabel.setIcon(new ImageIcon(getClass().getResource("/konnekt/resources/images/user.png"))); // default icon
        avatarLabel.setPreferredSize(new Dimension(50, 50));
        panel.add(avatarLabel, BorderLayout.WEST);

        // Fullname
        JLabel nameLabel = new JLabel(user.getFullName() != null ? user.getFullName() : "Default Profile");
        nameLabel.setForeground(Color.WHITE);
        panel.add(nameLabel, BorderLayout.CENTER);

        // On click → open chat
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                NavigatorView.showChat(user.getId());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(60, 60, 60));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(new Color(40, 40, 40));
            }
        });

        return panel;
    }
}
