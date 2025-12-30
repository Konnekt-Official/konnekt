package konnekt.model.dao;

import konnekt.connection.DatabaseConnection;
import konnekt.model.pojo.ChatPojo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatDao {

    // Send a message
    public boolean sendMessage(ChatPojo chat) {
        String sql = "INSERT INTO chat(sender_id, receiver_id, content) VALUES(?,?,?)";
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

    // Get full conversation between two users
    public List<ChatPojo> getConversation(int user1, int user2) {
        List<ChatPojo> list = new ArrayList<>();
        String sql = "SELECT * FROM chat "
                + "WHERE (sender_id=? AND receiver_id=?) OR (sender_id=? AND receiver_id=?) "
                + "ORDER BY created_at ASC";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, user1);
            ps.setInt(2, user2);
            ps.setInt(3, user2);
            ps.setInt(4, user1);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChatPojo c = new ChatPojo();
                c.setId(rs.getInt("id"));
                c.setSenderId(rs.getInt("sender_id"));
                c.setReceiverId(rs.getInt("receiver_id"));
                c.setContent(rs.getString("content"));
                c.setRead(rs.getBoolean("is_read"));
                c.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get latest message for each user (for Inbox)
    public List<ChatPojo> getLatestMessages(int userId) {
        List<ChatPojo> list = new ArrayList<>();
        String sql = """
            SELECT * FROM chat c
            INNER JOIN (
                SELECT 
                    CASE 
                        WHEN sender_id=? THEN receiver_id
                        ELSE sender_id
                    END AS other_user,
                    MAX(created_at) AS latest
                FROM chat
                WHERE sender_id=? OR receiver_id=?
                GROUP BY other_user
            ) t ON ( (c.sender_id = ? AND c.receiver_id = t.other_user) OR (c.sender_id = t.other_user AND c.receiver_id = ?) )
            AND c.created_at = t.latest
            ORDER BY c.created_at DESC
        """;

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, userId);
            ps.setInt(4, userId);
            ps.setInt(5, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChatPojo c = new ChatPojo();
                c.setId(rs.getInt("id"));
                c.setSenderId(rs.getInt("sender_id"));
                c.setReceiverId(rs.getInt("receiver_id"));
                c.setContent(rs.getString("content"));
                c.setRead(rs.getBoolean("is_read"));
                c.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getLatestMessageSnippet(int currentUserId, int otherUserId) {
        String sql = """
        SELECT content
        FROM chat
        WHERE (sender_id = ? AND receiver_id = ?) 
           OR (sender_id = ? AND receiver_id = ?)
        ORDER BY created_at DESC
        LIMIT 1
    """;

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, currentUserId);
            ps.setInt(2, otherUserId);
            ps.setInt(3, otherUserId);
            ps.setInt(4, currentUserId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String content = rs.getString("content");
                return content.length() > 30 ? content.substring(0, 30) + "..." : content;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Mark messages as read
    public void markAsRead(int senderId, int receiverId) {
        String sql = "UPDATE chat SET is_read=TRUE WHERE sender_id=? AND receiver_id=? AND is_read=FALSE";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, senderId);
            ps.setInt(2, receiverId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
