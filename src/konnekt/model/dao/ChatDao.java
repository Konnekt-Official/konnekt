package konnekt.model.dao;

import konnekt.connection.DatabaseConnection;
import konnekt.model.pojo.ChatPojo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatDao {

    // Send a message
    public boolean sendMessage(ChatPojo chat) {
        String sql = "INSERT INTO chat(sender_id, receiver_id, content) VALUES (?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, chat.getSenderId());
            ps.setInt(2, chat.getReceiverId());
            ps.setString(3, chat.getContent());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get latest messages grouped by sender for Inbox
    public List<ChatPojo> getLatestMessages(int userId) {
        List<ChatPojo> list = new ArrayList<>();
        String sql = """
            SELECT c1.*
            FROM chat c1
            INNER JOIN (
                SELECT 
                    CASE WHEN sender_id = ? THEN receiver_id ELSE sender_id END AS user_id,
                    MAX(created_at) AS max_created
                FROM chat
                WHERE sender_id = ? OR receiver_id = ?
                GROUP BY user_id
            ) c2
            ON ((c1.sender_id = ? AND c1.receiver_id = c2.user_id)
            OR  (c1.sender_id = c2.user_id AND c1.receiver_id = ?))
            AND c1.created_at = c2.max_created
            ORDER BY c1.created_at DESC
        """;

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, userId);
            ps.setInt(4, userId);
            ps.setInt(5, userId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChatPojo chat = new ChatPojo();
                chat.setId(rs.getInt("id"));
                chat.setSenderId(rs.getInt("sender_id"));
                chat.setReceiverId(rs.getInt("receiver_id"));
                chat.setContent(rs.getString("content"));
                chat.setCreatedAt(rs.getTimestamp("created_at"));
                chat.setRead(rs.getBoolean("is_read"));
                list.add(chat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get full conversation between two users
    public List<ChatPojo> getConversation(int user1, int user2) {
        List<ChatPojo> list = new ArrayList<>();
        String sql = """
            SELECT * FROM chat 
            WHERE (sender_id = ? AND receiver_id = ?) 
               OR (sender_id = ? AND receiver_id = ?)
            ORDER BY created_at ASC
        """;
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, user1);
            ps.setInt(2, user2);
            ps.setInt(3, user2);
            ps.setInt(4, user1);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChatPojo chat = new ChatPojo();
                chat.setId(rs.getInt("id"));
                chat.setSenderId(rs.getInt("sender_id"));
                chat.setReceiverId(rs.getInt("receiver_id"));
                chat.setContent(rs.getString("content"));
                chat.setCreatedAt(rs.getTimestamp("created_at"));
                chat.setRead(rs.getBoolean("is_read"));
                list.add(chat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Mark all messages as read
    public boolean markAsRead(int senderId, int receiverId) {
        String sql = "UPDATE chat SET is_read = TRUE WHERE sender_id = ? AND receiver_id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, senderId);
            ps.setInt(2, receiverId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
