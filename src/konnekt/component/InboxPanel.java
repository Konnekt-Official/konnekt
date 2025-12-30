package konnekt.component;

import konnekt.controller.ChatController;
import konnekt.manager.SessionManager;
import konnekt.model.dao.UserDao;
import konnekt.model.pojo.ChatPojo;
import konnekt.model.pojo.UserPojo;
import konnekt.view.NavigatorView;
import konnekt.utils.AvatarUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class InboxPanel extends JPanel {

    private final JPanel userListContainer = new JPanel();
    private final ChatController chatController = new ChatController();
    private final int currentUserId = SessionManager.getCurrentUserId();

    public InboxPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        userListContainer.setLayout(new BoxLayout(userListContainer, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(userListContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        loadInbox();
    }

    public void loadInbox() {
        userListContainer.removeAll();

        // Load all users except current
        List<UserPojo> allUsers = new UserDao().getAllUsers();
        allUsers.removeIf(u -> u.getId() == currentUserId);

        if (allUsers.isEmpty()) {
            JLabel msg = new JLabel("No users available.");
            msg.setFont(new Font("Verdana", Font.PLAIN, 12));
            msg.setForeground(Color.GRAY);
            msg.setAlignmentX(Component.CENTER_ALIGNMENT);
            userListContainer.add(Box.createVerticalStrut(20));
            userListContainer.add(msg);
        } else {
            for (UserPojo user : allUsers) {
                ChatPojo latestMsg = chatController.getLatestMessageBetween(currentUserId, user.getId());
                userListContainer.add(createUserItem(user, latestMsg));
                userListContainer.add(Box.createVerticalStrut(5));
            }
        }

        userListContainer.revalidate();
        userListContainer.repaint();
    }

    private JPanel createUserItem(UserPojo user, ChatPojo latestMsg) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setBackground(Color.WHITE);
        row.setBorder(new EmptyBorder(8, 8, 8, 8));
        row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel avatar = AvatarUtil.avatar(50);
        row.add(avatar, BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);

        JLabel fullName = new JLabel(user.getFullName());
        fullName.setFont(new Font("Verdana", Font.BOLD, 14));

        JLabel username = new JLabel("@" + user.getUsername());
        username.setFont(new Font("Verdana", Font.PLAIN, 12));
        username.setForeground(Color.BLUE);

        JLabel latestMessage = new JLabel(latestMsg != null ? latestMsg.getContent() : "");
        latestMessage.setFont(new Font("Verdana", Font.PLAIN, 12));
        latestMessage.setForeground(Color.GRAY);

        info.add(fullName);
        info.add(username);
        info.add(Box.createVerticalStrut(2));
        info.add(latestMessage);

        row.add(info, BorderLayout.CENTER);

        row.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                NavigatorView.showChat(user.getId());
                // optional: refresh inbox when returning
                SwingUtilities.invokeLater(InboxPanel.this::loadInbox);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                row.setBackground(new Color(230, 230, 230));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                row.setBackground(Color.WHITE);
            }
        });

        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, row.getPreferredSize().height));
        return row;
    }
}
