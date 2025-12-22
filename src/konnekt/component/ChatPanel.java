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
import java.util.List;

public class ChatPanel extends JPanel {

    private final ChatController chatController = new ChatController();
    private final int currentUserId = SessionManager.getCurrentUserId();
    private UserPojo otherUser;

    private final JPanel messageContainer = new JPanel();
    private final JScrollPane scrollPane = new JScrollPane();
    private final JTextArea inputField = new JTextArea();
    private final JButton sendButton = new JButton("SEND");

    public ChatPanel(int otherUserId) {
        this.otherUser = chatController.getUserById(otherUserId);
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        add(createHeader(), BorderLayout.NORTH);
        add(createMessageScroll(), BorderLayout.CENTER);
        add(createInputPanel(), BorderLayout.SOUTH);

        // Initial load of messages
        loadMessagesSmooth();
        chatController.markMessagesAsRead(otherUserId, currentUserId);

        // Auto-refresh every 2 seconds
        int refreshInterval = 2000; // milliseconds
        new Timer(refreshInterval, e -> loadMessagesSmooth()).start();
    }

    // ---------- HEADER ----------
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Left side: avatar + name
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);

        JLabel avatar = AvatarUtil.avatar(40);
        left.add(avatar);

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setOpaque(false);

        JLabel name = new JLabel(otherUser.getFullName());
        name.setFont(new Font("Verdana", Font.BOLD, 14));

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

        namePanel.add(name);
        namePanel.add(username);
        left.add(namePanel);

        header.add(left, BorderLayout.WEST);

        // Right side: Video Call button
        JButton videoCall = new JButton("VIDEO CALL");
        videoCall.setForeground(Color.WHITE);
        videoCall.setBackground(new Color(0,153,255));
        videoCall.setFont(new Font("Verdana", Font.BOLD, 12));
        videoCall.setPreferredSize(new Dimension(115, 30));
        videoCall.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        header.add(videoCall, BorderLayout.EAST);

        return header;
    }

    // ---------- MESSAGE SCROLL ----------
    private JScrollPane createMessageScroll() {
        messageContainer.setLayout(new BoxLayout(messageContainer, BoxLayout.Y_AXIS));
        messageContainer.setBackground(new Color(245, 245, 245));

        scrollPane.setViewportView(messageContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    // ---------- INPUT PANEL ----------
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.setBackground(Color.WHITE);

        inputField.setRows(2);
        inputField.setLineWrap(true);
        inputField.setWrapStyleWord(true);
        inputField.setFont(new Font("Verdana", Font.PLAIN, 13));

        sendButton.setFont(new Font("Verdana", Font.BOLD, 13));
        sendButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sendButton.setForeground(Color.WHITE);
        sendButton.setBackground(Color.BLACK);

        sendButton.addActionListener(e -> sendMessage());
        inputField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !e.isShiftDown()) {
                    e.consume();
                    sendMessage();
                    NavigatorView.refreshInboxPanel();
                }
            }
        });

        panel.add(new JScrollPane(inputField), BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);

        return panel;
    }

    // ---------- SEND MESSAGE ----------
    private void sendMessage() {
        String content = inputField.getText().trim();
        if (!content.isEmpty()) {
            chatController.sendMessage(currentUserId, otherUser.getId(), content);
            inputField.setText("");
            loadMessagesSmooth();
        }
    }

    // ---------- LOAD MESSAGES (SMOOTH AUTO-REFRESH) ----------
    private void loadMessagesSmooth() {
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        boolean atBottom = verticalBar.getValue() + verticalBar.getVisibleAmount() >= verticalBar.getMaximum() - 20;

        List<ChatPojo> messages = chatController.getMessagesBetween(currentUserId, otherUser.getId());
        messageContainer.removeAll();

        int lastSender = -1;
        for (ChatPojo msg : messages) {
            boolean isCurrentUser = msg.getSenderId() == currentUserId;
            boolean sameSender = lastSender == msg.getSenderId();
            lastSender = msg.getSenderId();

            messageContainer.add(createMessageBubble(msg, isCurrentUser, !sameSender));
        }

        messageContainer.revalidate();
        messageContainer.repaint();

        // Scroll only if at bottom
        if (atBottom) {
            SwingUtilities.invokeLater(() ->
                verticalBar.setValue(verticalBar.getMaximum())
            );
        }
    }

    // ---------- MESSAGE BUBBLE ----------
    private JPanel createMessageBubble(ChatPojo msg, boolean isCurrentUser, boolean addTopGap) {
        JPanel wrapper = new JPanel(new FlowLayout(isCurrentUser ? FlowLayout.RIGHT : FlowLayout.LEFT, 5, addTopGap ? 10 : 2));
        wrapper.setOpaque(false);

        JTextArea text = new JTextArea(msg.getContent());
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setFont(new Font("Verdana", Font.PLAIN, 13));
        text.setOpaque(true);
        text.setBackground(isCurrentUser ? new Color(220, 248, 198) : Color.WHITE);
        text.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        text.setMaximumSize(new Dimension(300, Short.MAX_VALUE));

        JLabel time = new JLabel(msg.getCreatedAt().toString());
        time.setFont(new Font("Verdana", Font.PLAIN, 10));
        time.setForeground(Color.GRAY);

        JPanel msgPanel = new JPanel();
        msgPanel.setLayout(new BoxLayout(msgPanel, BoxLayout.Y_AXIS));
        msgPanel.setOpaque(false);
        msgPanel.add(text);
        msgPanel.add(time);

        wrapper.add(msgPanel);
        return wrapper;
    }
}
