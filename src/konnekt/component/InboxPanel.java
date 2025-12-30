package konnekt.component;

import konnekt.controller.ChatController;
import konnekt.controller.UserController;
import konnekt.manager.SessionManager;
import konnekt.model.pojo.ChatPojo;
import konnekt.model.pojo.UserPojo;
import konnekt.view.NavigatorView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class InboxPanel extends JPanel {

    private final ChatController chatController = new ChatController();
    private final UserController userController = new UserController();
    private final JPanel usersContainer = new JPanel();

    public InboxPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        usersContainer.setLayout(new BoxLayout(usersContainer, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(usersContainer);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);

        loadInbox();
    }

    private void loadInbox() {
        usersContainer.removeAll();
        int currentUserId = SessionManager.getCurrentUserId();

        List<UserPojo> users = chatController.getChatUsers(currentUserId);

        for (UserPojo user : users) {
            ChatPojo latestMessage = chatController.getLatestMessageBetween(currentUserId, user.getId());
            usersContainer.add(createUserItem(user, latestMessage));
            usersContainer.add(Box.createVerticalStrut(5));
        }

        usersContainer.revalidate();
        usersContainer.repaint();
    }

    private JPanel createUserItem(UserPojo user, ChatPojo latestMessage) {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.setBackground(Color.WHITE);
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel avatar = new JLabel(new ImageIcon(
                new ImageIcon(getClass().getResource("/konnekt/resources/images/default_profile.png"))
                        .getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)
        ));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(user.getFullName());
        nameLabel.setFont(new Font("Verdana", Font.BOLD, 14));

        JLabel latestMsgLabel = new JLabel("<html>" + latestMessage.getContent() + "</html>");
        latestMsgLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
        latestMsgLabel.setForeground(Color.GRAY);

        textPanel.add(nameLabel);
        textPanel.add(latestMsgLabel);

        panel.add(avatar, BorderLayout.WEST);
        panel.add(textPanel, BorderLayout.CENTER);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                NavigatorView.showChat(user.getId()); // open ChatPanel for this user
            }
        });

        return panel;
    }
}
