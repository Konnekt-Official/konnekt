package konnekt.component;

import konnekt.controller.ChatController;
import konnekt.manager.SessionManager;
import konnekt.model.pojo.ChatPojo;
import konnekt.model.pojo.UserPojo;
import konnekt.view.NavigatorView;
import konnekt.utils.AvatarUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class ChatPanel extends JPanel {

    private final int currentUserId = SessionManager.getCurrentUserId();
    private UserPojo otherUser;

    private final ChatController chatController = new ChatController();

    private final JPanel messagesContainer = new JPanel();
    private final JScrollPane scrollPane = new JScrollPane(messagesContainer);

    private final JTextArea inputArea = new JTextArea(2, 1);

    public ChatPanel(int otherUserId) {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        otherUser = chatController.getUserById(otherUserId);

        add(createTopPanel(), BorderLayout.NORTH);
        add(createMessagesPanel(), BorderLayout.CENTER);
        add(createInputPanel(), BorderLayout.SOUTH);

        loadMessages();
        chatController.markMessagesAsRead(otherUserId, currentUserId);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel avatar = AvatarUtil.avatar(50);
        panel.add(avatar, BorderLayout.WEST);

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setOpaque(false);

        JLabel fullName = new JLabel(otherUser.getFullName());
        fullName.setFont(new Font("Verdana", Font.BOLD, 14));

        JLabel username = new JLabel("@" + otherUser.getUsername());
        username.setFont(new Font("Verdana", Font.PLAIN, 12));
        username.setForeground(Color.BLUE);
        username.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        username.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                NavigatorView.showProfile(otherUser.getId());
            }
        });

        namePanel.add(Box.createVerticalStrut(8)); // larger gap between avatar and name
        namePanel.add(fullName);
        namePanel.add(username);

        panel.add(namePanel, BorderLayout.CENTER);

        JButton videoCall = new JButton("Video Call");
        videoCall.setPreferredSize(new Dimension(100, 30));
        videoCall.setFocusable(false);
        videoCall.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.add(videoCall, BorderLayout.EAST);

        return panel;
    }

    private JScrollPane createMessagesPanel() {
        messagesContainer.setLayout(new BoxLayout(messagesContainer, BoxLayout.Y_AXIS));
        messagesContainer.setOpaque(false);

        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        return scrollPane;
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.setBackground(Color.WHITE);

        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setFont(new Font("Verdana", Font.PLAIN, 13));

        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JButton sendBtn = new JButton("Send");
        sendBtn.setFocusable(false);
        sendBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sendBtn.addActionListener(e -> sendMessage());

        inputArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !e.isShiftDown()) {
                    e.consume();
                    sendMessage();
                }
            }
        });

        panel.add(inputScroll, BorderLayout.CENTER);
        panel.add(sendBtn, BorderLayout.EAST);

        return panel;
    }

    private void sendMessage() {
        String text = inputArea.getText().trim();
        if (!text.isEmpty()) {
            chatController.sendMessage(currentUserId, otherUser.getId(), text);
            inputArea.setText("");
            loadMessages();
        }
    }

    public void loadMessages() {
        messagesContainer.removeAll();

        List<ChatPojo> messages = chatController.getMessagesBetween(currentUserId, otherUser.getId());

        for (ChatPojo msg : messages) {
            messagesContainer.add(createMessageBubble(msg));
            messagesContainer.add(Box.createVerticalStrut(6)); // gap between messages
        }

        messagesContainer.revalidate();
        messagesContainer.repaint();

        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()));
    }

    private JPanel createMessageBubble(ChatPojo msg) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        String timeText = timeAgo(msg.getCreatedAt());

        JLabel messageLabel = new JLabel("<html>" + msg.getContent() + "</html>");
        messageLabel.setFont(new Font("Verdana", Font.PLAIN, 13));
        messageLabel.setBorder(new EmptyBorder(6, 10, 6, 10));
        messageLabel.setOpaque(true);

        JLabel timeLabel = new JLabel(timeText);
        timeLabel.setFont(new Font("Verdana", Font.PLAIN, 10));
        timeLabel.setForeground(Color.GRAY);

        if (msg.getSenderId() == currentUserId) {
            messageLabel.setBackground(new Color(0, 132, 255)); // blue bubble
            messageLabel.setForeground(Color.WHITE);
            panel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        } else {
            messageLabel.setBackground(new Color(230, 230, 230)); // gray bubble
            messageLabel.setForeground(Color.BLACK);
            panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        panel.add(messageLabel);
        panel.add(timeLabel);

        return panel;
    }

    private String timeAgo(java.sql.Timestamp ts) {
        if (ts == null) return "";
        Duration d = Duration.between(ts.toInstant(), Instant.now());
        if (d.toMinutes() < 1) return "Just now";
        if (d.toHours() < 1) return d.toMinutes() + "m";
        if (d.toDays() < 1) return d.toHours() + "h";
        return d.toDays() + "d";
    }
}
