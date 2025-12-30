package konnekt.model.dao;

import konnekt.model.pojo.NotificationPojo;
import konnekt.connection.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
             PreparedStatement ps = c.prepareStatement(
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

    // ---------- FETCH ALL (NO GROUPING) ----------
    public List<NotificationPojo> allForUser(int userId) {
        List<NotificationPojo> list = new ArrayList<>();
        String sql = """
            SELECT n.*, u.full_name, u.username
            FROM notification n
            LEFT JOIN user u ON u.id = n.sender_id
            WHERE n.user_id = ?
            ORDER BY created_at DESC
        """;

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                NotificationPojo n = new NotificationPojo();
                n.setId(rs.getInt("id"));
                n.setUserId(rs.getInt("user_id"));
                n.setSenderId(rs.getInt("sender_id"));
                n.setSenderFullName(rs.getString("full_name"));
                n.setSenderUsername(rs.getString("username"));
                n.setType(rs.getString("type"));
                n.setReferenceId(rs.getInt("reference_id"));
                n.setRead(rs.getBoolean("is_read"));
                n.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(n);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ---------- MARK ALL READ ----------
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
