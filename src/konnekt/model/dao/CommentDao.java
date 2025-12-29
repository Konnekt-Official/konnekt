package konnekt.model.dao;

import konnekt.connection.DatabaseConnection;
import konnekt.model.pojo.CommentPojo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDao {

    // ================= CREATE COMMENT =================
    public void addComment(int postId, int userId, String content) {
        String sql = """
            INSERT INTO comment (post_id, user_id, content)
            VALUES (?, ?, ?)
        """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ps.setInt(2, userId);
            ps.setString(3, content);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= FETCH COMMENTS BY POST =================
    public List<CommentPojo> getCommentsByPost(int postId) {
        List<CommentPojo> comments = new ArrayList<>();

        String sql = """
            SELECT c.*, u.full_name, u.username
            FROM comment c
            JOIN user u ON c.user_id = u.id
            WHERE c.post_id = ?
            ORDER BY c.created_at ASC
        """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CommentPojo c = new CommentPojo();
                c.setId(rs.getInt("id"));
                c.setPostId(rs.getInt("post_id"));
                c.setUserId(rs.getInt("user_id"));
                c.setContent(rs.getString("content"));
                c.setCreatedAt(rs.getTimestamp("created_at"));
                c.setFullName(rs.getString("full_name"));
                c.setUsername(rs.getString("username"));
                comments.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return comments;
    }
}