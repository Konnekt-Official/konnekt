package konnekt.component;

import javax.swing.*;
import java.awt.*;

public class ChatPanel extends JPanel {

    private final int receiverId;

    public ChatPanel(int receiverId) {
        this.receiverId = receiverId;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Chat with user ID: " + receiverId);
        title.setFont(new Font("Verdana", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setBackground(new Color(30, 30, 30));
        chatArea.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        JTextField messageField = new JTextField();
        JButton sendButton = new JButton("Send");

        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> {
            String msg = messageField.getText().trim();
            if (!msg.isEmpty()) {
                chatArea.append("Me: " + msg + "\n");
                messageField.setText("");
                // TODO: send message to backend / DB
            }
        });
    }
}
