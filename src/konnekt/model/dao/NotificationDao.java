package konnekt.model.dao;

import konnekt.model.pojo.NotificationPojo;

import java.sql.*;
import java.util.*;
import konnekt.connection.DatabaseConnection;

public class NotificationDao {

    // ---------- CREATE ----------
    public void create(int userId, int senderId, String type, Integer refId) {
        String sql = """
            INSERT INTO notification(user_id, sender_id, type, reference_id)
            VALUES (?,?,?,?)
        """;
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, senderId);
            ps.setString(3, type);
            if (refId == null) ps.setNull(4, Types.INTEGER);
            else ps.setInt(4, refId);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- UNREAD COUNT ----------
    public int unreadCount(int userId) {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps =
                     c.prepareStatement(
                             "SELECT COUNT(*) FROM notification WHERE user_id=? AND is_read=0"
                     )) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ---------- GROUPED FETCH ----------
    public List<NotificationPojo> groupedForUser(int userId) {
        List<NotificationPojo> list = new ArrayList<>();

        String sql = """
            SELECT 
                MAX(n.id) id,
                n.type,
                n.reference_id,
                COUNT(*) cnt,
                MAX(n.created_at) created_at,
                u.full_name,
                u.username,
                n.sender_id,
                MIN(n.is_read) is_read
            FROM notification n
            JOIN user u ON u.id = n.sender_id
            WHERE n.user_id = ?
            GROUP BY n.type, n.reference_id, n.sender_id
            ORDER BY created_at DESC
        """;

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                NotificationPojo n = new NotificationPojo();
                n.setId(rs.getInt("id"));
                n.setType(rs.getString("type"));
                n.setReferenceId(rs.getInt("reference_id"));
                n.setSenderId(rs.getInt("sender_id"));
                n.setSenderFullName(rs.getString("full_name"));
                n.setSenderUsername(rs.getString("username"));
                n.setRead(rs.getBoolean("is_read"));
                n.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void markAllRead(int userId) {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps =
                     c.prepareStatement("UPDATE notification SET is_read=1 WHERE user_id=?")) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
