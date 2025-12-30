package konnekt.component;

import konnekt.manager.SessionManager;
import konnekt.model.dao.ChatDao;
import konnekt.model.pojo.ChatPojo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class ChatPanel extends JPanel {

    private final ChatDao chatDao = new ChatDao();
    private final JPanel messagesContainer = new JPanel();
    private final JScrollPane scrollPane;
    private final JTextArea input;
    private final int otherUserId;
    private Timer refreshTimer;

    private static final Font FONT = new Font("Verdana", Font.PLAIN, 13);

    public ChatPanel(int otherUserId) {
        this.otherUserId = otherUserId;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Messages container
        messagesContainer.setLayout(new BoxLayout(messagesContainer, BoxLayout.Y_AXIS));
        messagesContainer.setBackground(new Color(245, 245, 245));
        scrollPane = new JScrollPane(messagesContainer);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout(8, 8));
        inputPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        inputPanel.setBackground(Color.WHITE);

        input = new JTextArea(2, 1);
        input.setFont(FONT);
        input.setLineWrap(true);
        input.setWrapStyleWord(true);
        input.setText("Type a message...");
        input.setForeground(Color.GRAY);

        input.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (input.getText().equals("Type a message...")) {
                    input.setText("");
                    input.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (input.getText().isEmpty()) {
                    input.setText("Type a message...");
                    input.setForeground(Color.GRAY);
                }
            }
        });

        input.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !e.isShiftDown()) {
                    e.consume(); // prevent newline
                    sendMessage();
                }
            }
        });

        JScrollPane inputScroll = new JScrollPane(input);
        inputScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        inputScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JButton sendBtn = new JButton("Send");
        sendBtn.setFont(FONT);
        sendBtn.setFocusable(false);
        sendBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sendBtn.addActionListener(e -> sendMessage());

        inputPanel.add(inputScroll, BorderLayout.CENTER);
        inputPanel.add(sendBtn, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        // Load messages initially
        loadMessages();

        // Auto-refresh every 2 seconds
        refreshTimer = new Timer(2000, e -> loadMessages());
        refreshTimer.start();
    }

    private void sendMessage() {
        String text = input.getText().trim();
        if (!text.isEmpty() && !text.equals("Type a message...")) {
            ChatPojo chat = new ChatPojo();
            chat.setSenderId(SessionManager.getCurrentUserId());
            chat.setReceiverId(otherUserId);
            chat.setContent(text);
            chatDao.sendMessage(chat);

            input.setText("");
            loadMessages();
        }
    }

    private void loadMessages() {
        messagesContainer.removeAll();
        int currentUserId = SessionManager.getCurrentUserId();
        List<ChatPojo> chats = chatDao.getConversation(currentUserId, otherUserId);

        for (ChatPojo chat : chats) {
            boolean isMine = chat.getSenderId() == currentUserId;
            messagesContainer.add(createMessageBubble(chat.getContent(), isMine));
            messagesContainer.add(Box.createVerticalStrut(5));
        }

        messagesContainer.revalidate();
        messagesContainer.repaint();
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()));

        // Mark all messages from other user as read
        chatDao.markAsRead(otherUserId, currentUserId);
    }

    private JPanel createMessageBubble(String text, boolean isMine) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(isMine ? FlowLayout.RIGHT : FlowLayout.LEFT));
        panel.setBackground(new Color(245, 245, 245));

        JLabel label = new JLabel("<html><body style='width:200px;'>" + text + "</body></html>");
        label.setFont(FONT);
        label.setOpaque(true);
        label.setBackground(isMine ? new Color(173, 216, 230) : new Color(220, 220, 220));
        label.setBorder(new EmptyBorder(5, 10, 5, 10));

        panel.add(label);
        return panel;
    }

    public void stopAutoRefresh() {
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
    }
}
