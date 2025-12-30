package konnekt.component;

import konnekt.model.dao.NotificationDao;
import konnekt.model.pojo.NotificationPojo;
import konnekt.utils.TimeAgo;
import konnekt.manager.SessionManager;
import konnekt.view.NavigatorView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class NotificationPanel extends JPanel {

    private final NotificationDao dao = new NotificationDao();
    private final JPanel container = new JPanel();
    private final JScrollPane scrollPane;

    public NotificationPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setAlignmentY(Component.TOP_ALIGNMENT);
        container.setBackground(new Color(245, 245, 245));

        scrollPane = new JScrollPane(container);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {
        container.removeAll();

        List<NotificationPojo> list = dao.allForUser(SessionManager.getCurrentUserId());

        if (list.isEmpty()) {
            JLabel msg = new JLabel("No notifications.");
            msg.setFont(new Font("Verdana", Font.ITALIC, 12));
            msg.setForeground(Color.GRAY);
            msg.setAlignmentX(Component.CENTER_ALIGNMENT);
            container.add(Box.createVerticalStrut(20));
            container.add(msg);
        } else {
            for (NotificationPojo n : list) {
                container.add(card(n));
                container.add(Box.createVerticalStrut(6));
            }
        }

        container.revalidate();
        container.repaint();
    }

    private JPanel card(NotificationPojo n) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(8, 8, 8, 8));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Avatar
        JLabel avatar = new JLabel(new ImageIcon(
                getClass().getResource("/konnekt/resources/images/default_profile.png")
        ));
        p.add(avatar);
        p.add(Box.createHorizontalStrut(8));

        // Text
        JLabel text = new JLabel(buildText(n));
        text.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        text.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                switch (n.getType()) {
                    case "FOLLOW", "MESSAGE" -> NavigatorView.showProfile(n.getSenderId());
                    case "LIKE", "COMMENT" -> NavigatorView.showComments(n.getReferenceId());
                }
                dao.markAllRead(SessionManager.getCurrentUserId());
            }
        });

        // Wrap text, fit-content width/height
        text.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));
        text.setAlignmentY(Component.TOP_ALIGNMENT);

        p.add(text);

        // Fit-content height for panel
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, p.getPreferredSize().height));

        return p;
    }

    private String buildText(NotificationPojo n) {
        String sender = "<b>" + n.getSenderFullName() + "</b> <font color='blue'>@" + n.getSenderUsername() + "</font>";
        String time = "<span style='color:gray'>" + TimeAgo.format(n.getCreatedAt()) + "</span>";

        return switch (n.getType()) {
            case "LIKE" -> "<html>" + sender + " liked your post " + time + "</html>";
            case "COMMENT" -> "<html>" + sender + " commented on your post " + time + "</html>";
            case "FOLLOW" -> "<html>" + sender + " followed you " + time + "</html>";
            case "MESSAGE" -> "<html>" + sender + " sent you a message " + time + "</html>";
            default -> "<html>" + sender + " did something " + time + "</html>";
        };
    }

    // Optional: scroll to top
    public void scrollToTop() {
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
    }

    // Optional: get unread count if needed
    public int getUnreadCount() {
        return dao.unreadCount(SessionManager.getCurrentUserId());
    }
}
