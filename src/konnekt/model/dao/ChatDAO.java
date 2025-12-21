package konnekt.model.dao;

import konnekt.model.pojo.ChatPojo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatDAO {

    private Connection connection;

    // Constructor to initialize DAO with DB connection
    public ChatDAO(Connection connection) {
        this.connection = connection;
    }

    // Insert a new chat message
    public boolean insertChat(ChatPojo chat) {
        String sql = "INSERT INTO chats (sender_email, receiver_email, message, sent_at, is_read) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, chat.getSenderEmail());
            ps.setString(2, chat.getReceiverEmail());
            ps.setString(3, chat.getMessage());
            ps.setTimestamp(4, chat.getSentAt());
            ps.setBoolean(5, chat.isRead());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    chat.setId(generatedKeys.getInt(1));
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get a chat by ID
    public ChatPojo getChatById(int id) {
        String sql = "SELECT * FROM chats WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToChat(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all chats between two users
    public List<ChatPojo> getChatsBetweenUsers(String user1, String user2) {
        String sql = "SELECT * FROM chats WHERE (sender_email = ? AND receiver_email = ?) " +
                     "OR (sender_email = ? AND receiver_email = ?) ORDER BY sent_at ASC";
        List<ChatPojo> chats = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user1);
            ps.setString(2, user2);
            ps.setString(3, user2);
            ps.setString(4, user1);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    chats.add(mapResultSetToChat(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chats;
    }

    // Mark a chat as read
    public boolean markAsRead(int chatId) {
        String sql = "UPDATE chats SET is_read = TRUE WHERE_
