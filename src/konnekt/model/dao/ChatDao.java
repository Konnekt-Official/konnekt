package konnekt.model.dao;

import konnekt.connection.DatabaseConnection;
import konnekt.model.pojo.ChatPojo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatDao {

    public boolean sendMessage(int senderId, int receiverId, String content) {
        String sql = "INSERT INTO chat(sender_id, receiver_id, content) VALUES (?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, senderId);
            ps.setInt(2, receiverId);
            ps.setString(3, content);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // get messages **between two users**
    public List<ChatPojo> getMessagesBetween(int userId1, int userId2) {
        List<ChatPojo> list = new ArrayList<>();
        String sql = """
            SELECT * FROM chat
            WHERE (sender_id=? AND receiver_id=?) OR (sender_id=? AND receiver_id=?)
            ORDER BY created_at ASC
        """;

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId1);
            ps.setInt(2, userId2);
            ps.setInt(3, userId2);
            ps.setInt(4, userId1);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChatPojo c = new ChatPojo();
                c.setId(rs.getInt("id"));
                c.setSenderId(rs.getInt("sender_id"));
                c.setReceiverId(rs.getInt("receiver_id"));
                c.setContent(rs.getString("content"));
                c.setCreatedAt(rs.getTimestamp("created_at"));
                c.setRead(rs.getBoolean("is_read"));
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Returns list of distinct users who chatted with current user
    public List<Integer> getChatUserIds(int currentUserId) {
        List<Integer> userIds = new ArrayList<>();
        String sql = """
        SELECT DISTINCT 
            CASE WHEN sender_id = ? THEN receiver_id ELSE sender_id END AS other_user
        FROM chat
        WHERE sender_id = ? OR receiver_id = ?
    """;
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, currentUserId);
            ps.setInt(2, currentUserId);
            ps.setInt(3, currentUserId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userIds.add(rs.getInt("other_user"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userIds;
    }

    // mark messages as read **from sender to receiver**
    public void markMessagesAsRead(int senderId, int receiverId) {
        String sql = "UPDATE chat SET is_read=TRUE WHERE sender_id=? AND receiver_id=? AND is_read=FALSE";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, senderId);
            ps.setInt(2, receiverId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Returns the latest chat between two users
    public ChatPojo getLatestMessageBetween(int user1Id, int user2Id) {
        String sql = """
        SELECT * FROM chat 
        WHERE (sender_id = ? AND receiver_id = ?) 
           OR (sender_id = ? AND receiver_id = ?)
        ORDER BY created_at DESC
        LIMIT 1
    """;
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, user1Id);
            ps.setInt(2, user2Id);
            ps.setInt(3, user2Id);
            ps.setInt(4, user1Id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ChatPojo chat = new ChatPojo();
                chat.setId(rs.getInt("id"));
                chat.setSenderId(rs.getInt("sender_id"));
                chat.setReceiverId(rs.getInt("receiver_id"));
                chat.setContent(rs.getString("content"));
                chat.setCreatedAt(rs.getTimestamp("created_at"));
                chat.setRead(rs.getBoolean("is_read"));
                return chat;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
