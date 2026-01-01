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
import konnekt.utils.AvatarUtil;

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
            msg.setFont(new Font("Verdana", Font.PLAIN, 12));
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

        // Avatar (30x30)
        JLabel avatar = AvatarUtil.avatar(25);
        p.add(avatar);
        p.add(Box.createHorizontalStrut(8));

        // Text Panel to align top
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false); // transparent background
        JLabel text = new JLabel(buildText(n));
        text.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        text.setVerticalAlignment(SwingConstants.CENTER); // ensure center
        textPanel.add(text, BorderLayout.NORTH);

        text.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                switch (n.getType()) {
                    case "FOLLOW" ->
                        NavigatorView.showProfile(n.getSenderId());
                    case "LIKE", "COMMENT" ->
                        NavigatorView.showComments(n.getReferenceId());
                }
                dao.markAllRead(SessionManager.getCurrentUserId());
            }
        });

        // Set max size to wrap properly
        textPanel.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));

        p.add(textPanel);

        // Fit-content height for panel
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, p.getPreferredSize().height));

        return p;
    }

    private String buildText(NotificationPojo n) {
        // Use div to force top alignment in HTML
        String sender = "<b>" + n.getSenderFullName() + "</b> "
                + "<span style='font-weight:normal; color:blue'>@" + n.getSenderUsername() + "</span>";
        String actionText = switch (n.getType()) {
            case "LIKE" ->
                " liked <span style='font-weight:normal;'>your post</span>";
            case "COMMENT" ->
                " commented <span style='font-weight:normal;'>on your post</span>";
            case "FOLLOW" ->
                " followed <span style='font-weight:normal;'>you</span>";
            default ->
                " did something";
        };
        String time = "<span style='font-weight:normal;color:gray'> " + TimeAgo.format(n.getCreatedAt()) + "</span>";

        // Wrap in div to help alignment
        return "<html><div style='vertical-align:top;'>" + sender + actionText + time + "</div></html>";
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
