package konnekt.component;

import konnekt.dao.ChatDao;
import konnekt.dao.UserDao;
import konnekt.model.pojo.UserPojo;
import konnekt.manager.SessionManager;
import konnekt.view.NavigatorView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import konnekt.model.dao.ChatDao;
import konnekt.model.dao.UserDao;

public class InboxPanel extends JPanel {

    private final JPanel usersContainer = new JPanel();
    private final JScrollPane scrollPane;

    private final UserDao userDao = new UserDao();
    private final ChatDao chatDao = new ChatDao();

    public InboxPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        usersContainer.setLayout(new BoxLayout(usersContainer, BoxLayout.Y_AXIS));
        usersContainer.setBackground(new Color(245, 245, 245));

        scrollPane = new JScrollPane(usersContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );

        add(scrollPane, BorderLayout.CENTER);

        loadUsers();
    }

    private void loadUsers() {
        usersContainer.removeAll();

        int currentUserId = SessionManager.getCurrentUserId();
        List<UserPojo> users = userDao.getAllUsers();

        for (UserPojo user : users) {
            // ignore the logged-in user
            if (user.getId() == currentUserId) continue;

            usersContainer.add(createUserItem(user));
            usersContainer.add(Box.createVerticalStrut(5));
        }

        usersContainer.revalidate();
        usersContainer.repaint();
    }

    private JPanel createUserItem(UserPojo user) {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(new EmptyBorder(5, 10, 5, 10));
        panel.setBackground(Color.WHITE);
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // User name
        JLabel nameLabel = new JLabel(user.getFullName() != null ? user.getFullName() : user.getUsername());
        nameLabel.setFont(new Font("Verdana", Font.BOLD, 13));

        // Latest message snippet
        String latestMsg = chatDao.getLatestMessageSnippet(SessionManager.getCurrentUserId(), user.getId());
        JLabel msgLabel = new JLabel(latestMsg != null ? latestMsg : "");
        msgLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
        msgLabel.setForeground(Color.GRAY);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(nameLabel);
        textPanel.add(msgLabel);

        panel.add(textPanel, BorderLayout.CENTER);

        // Click to open chat
        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                NavigatorView.showChat(user.getId());
            }
        });

        return panel;
    }
}
