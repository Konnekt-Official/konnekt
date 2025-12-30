package konnekt.component;

import konnekt.controller.ChatController;
import konnekt.manager.SessionManager;
import konnekt.model.pojo.ChatPojo;
import konnekt.model.pojo.UserPojo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class ChatPanel extends JPanel {

    private final ChatController chatController = new ChatController();
    private final int currentUserId = SessionManager.getCurrentUserId();
    private final int otherUserId;
    private final UserPojo otherUser;

    private final JPanel messagesContainer = new JPanel();
    private final JScrollPane scrollPane;
    private final JTextArea inputField = new JTextArea(3, 1);

    public ChatPanel(int otherUserId) {
        this.otherUserId = otherUserId;
        this.otherUser = chatController.getUserById(otherUserId);

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(createHeader(), BorderLayout.NORTH);

        messagesContainer.setLayout(new BoxLayout(messagesContainer, BoxLayout.Y_AXIS));
        messagesContainer.setBackground(Color.WHITE);
        scrollPane = new JScrollPane(messagesContainer);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        add(createInputPanel(), BorderLayout.SOUTH);

        loadMessages();
        chatController.markMessagesAsRead(otherUserId, currentUserId);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(new EmptyBorder(5, 5, 5, 5));
        header.setBackground(Color.WHITE);

        JLabel avatar = new JLabel(new ImageIcon(
                new ImageIcon(getClass().getResource("/konnekt/resources/images/default_profile.png"))
                        .getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)
        ));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(otherUser.getFullName());
        nameLabel.setFont(new Font("Verdana", Font.BOLD, 14));

        JLabel usernameLabel = new JLabel("@" + otherUser.getUsername());
        usernameLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
        usernameLabel.setForeground(Color.GRAY);

        textPanel.add(nameLabel);
        textPanel.add(usernameLabel);

        JButton videoCallBtn = new JButton("Video Call");
        videoCallBtn.setFocusable(false);

        header.add(avatar, BorderLayout.WEST);
        header.add(textPanel, BorderLayout.CENTER);
        header.add(videoCallBtn, BorderLayout.EAST);

        return header;
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));

        inputField.setLineWrap(true);
        inputField.setWrapStyleWord(true);
        inputField.setFont(new Font("Verdana", Font.PLAIN, 13));

        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !e.isShiftDown()) {
                    e.consume();
                    sendMessage();
                }
            }
        });

        JScrollPane inputScroll = new JScrollPane(inputField);
        inputScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JButton sendBtn = new JButton("Send");
        sendBtn.setFocusable(false);
        sendBtn.addActionListener(e -> sendMessage());

        panel.add(inputScroll, BorderLayout.CENTER);
        panel.add(sendBtn, BorderLayout.EAST);

        return panel;
    }

    private void sendMessage() {
        String text = inputField.getText().trim();
        if (!text.isEmpty()) {
            chatController.sendMessage(currentUserId, otherUserId, text);
            inputField.setText("");
            loadMessages();
        }
    }

    private void loadMessages() {
        messagesContainer.removeAll();
        List<ChatPojo> messages = chatController.getMessagesBetween(currentUserId, otherUserId);

        for (ChatPojo msg : messages) {
            boolean sentByMe = msg.getSenderId() == currentUserId;
            messagesContainer.add(createMessageBubble(msg.getContent(), sentByMe));
            messagesContainer.add(Box.createVerticalStrut(5));
        }

        messagesContainer.revalidate();
        messagesContainer.repaint();
        scrollToBottom();
    }

    private JPanel createMessageBubble(String text, boolean sentByMe) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(sentByMe ? FlowLayout.RIGHT : FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);

        JTextArea bubble = new JTextArea(text);
        bubble.setEditable(false);
        bubble.setLineWrap(true);
        bubble.setWrapStyleWord(true);
        bubble.setFont(new Font("Verdana", Font.PLAIN, 13));
        bubble.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        bubble.setBackground(sentByMe ? new Color(0, 180, 255) : new Color(220, 220, 220));
        bubble.setForeground(sentByMe ? Color.WHITE : Color.BLACK);

        panel.add(bubble);
        return panel;
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()));
    }
}
