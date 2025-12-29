package konnekt.component;

import konnekt.model.dao.NotificationDao;
import konnekt.model.pojo.NotificationPojo;
import konnekt.utils.TimeAgo;
import konnekt.utils.SoundPlayer;
import konnekt.manager.SessionManager;
import konnekt.view.NavigatorView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class NotificationPanel extends JPanel {

    private final NotificationDao dao = new NotificationDao();
    private final JPanel container = new JPanel();

    public NotificationPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245,245,245));

        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(245,245,245));

        add(new JScrollPane(container), BorderLayout.CENTER);
        refresh();
        startRealtime();
    }

    public void refresh() {
        container.removeAll();
        List<NotificationPojo> list =
                dao.groupedForUser(SessionManager.getCurrentUserId());

        for (NotificationPojo n : list) {
            container.add(card(n));
            container.add(Box.createVerticalStrut(6));
        }
        revalidate();
        repaint();
    }

    private JPanel card(NotificationPojo n) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(8,8,8,8));

        JLabel avatar = new JLabel(new ImageIcon(
                getClass().getResource("/konnekt/resources/images/default_profile.png")
        ));
        p.add(avatar);

        JLabel text = new JLabel(buildText(n));
        text.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        text.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if ("FOLLOW".equals(n.getType()))
                    NavigatorView.showProfile(n.getSenderId());
                else
                    NavigatorView.showComments(n.getReferenceId());

                dao.markAllRead(SessionManager.getCurrentUserId());
            }
        });

        p.add(text);
        return p;
    }

    private String buildText(NotificationPojo n) {
        return "<html><b>" + n.getSenderFullName() + "</b> " +
                "<font color='blue'>@" + n.getSenderUsername() + "</font> " +
                "<b>" + n.getType().toLowerCase() + "</b> your post " +
                "<span style='color:gray'>" +
                TimeAgo.format(n.getCreatedAt()) +
                "</span></html>";
    }

    // ---------- REALTIME ----------
    private void startRealtime() {
        new Timer(5000, e -> {
            int before = dao.unreadCount(SessionManager.getCurrentUserId());
            refresh();
            int after = dao.unreadCount(SessionManager.getCurrentUserId());
            if (after > before) SoundPlayer.playNotification();
        }).start();
    }
}
